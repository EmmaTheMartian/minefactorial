package martian.minefactorial.content.block.logistics;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import top.girlkisser.lazuli.api.block.AbstractEnergyBE;
import top.girlkisser.lazuli.api.block.ITickableBE;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class BlockPipeEnergyBE extends AbstractEnergyBE implements ITickableBE {
	private Set<BlockPos> outputs = null;

	public BlockPipeEnergyBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.ENERGY_PIPE.get(), pos, blockState);
	}

	@Override
	public void serverTick(ServerLevel level) {
		if (getEnergyStored() <= 0) {
			return;
		}

		findOutputs(level);
		if (outputs.isEmpty()) {
			return;
		}

		// Distribute energy over all outputs
		int amount = getEnergyStored() / outputs.size();
		for (BlockPos p : outputs) {
			BlockEntity be = level.getBlockEntity(p);
			if (be != null) {
				IEnergyStorage storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, be.getBlockPos(), null);
				if (storage != null && storage.canReceive()) {
					int received = storage.receiveEnergy(amount, false);
					getEnergyStorage().forceExtractEnergy(received, false);
				}
			}
		}
	}

	// This function will cache all outputs for this cable network. It will do this
	// by traversing all cables connected to this cable and then check for all energy
	// receivers around those cables.
	private void findOutputs(ServerLevel level) {
		if (outputs != null) {
			return;
		}

		outputs = new HashSet<>();
		traverse(level, worldPosition, pipe -> {
			// Check for all energy receivers around this position (ignore cables)
			for (Direction direction : Direction.values()) {
				BlockPos p = pipe.getBlockPos().relative(direction);
				BlockEntity te = level.getBlockEntity(p);
				if (te != null && !(te instanceof BlockPipeEnergyBE)) {
					IEnergyStorage storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, te.getBlockPos(), null);
					if (storage != null && storage.canReceive()) {
						outputs.add(p);
					}
				}
			}
		});
	}

	@Override
	public void setChanged() {
		traverse(level, worldPosition, pipe -> pipe.outputs = null);
		super.setChanged();
	}

	// This is a generic function that will traverse all cables connected to this cable
	// and call the given consumer for each cable.
	private void traverse(Level level, BlockPos pos, Consumer<BlockPipeEnergyBE> consumer) {
		Set<BlockPos> traversed = new HashSet<>();
		traversed.add(pos);
		consumer.accept(this);
		traverse(level, pos, traversed, consumer);
	}

	private void traverse(Level level, BlockPos pos, Set<BlockPos> traversed, Consumer<BlockPipeEnergyBE> consumer) {
		for (Direction direction : Direction.values()) {
			BlockPos p = pos.relative(direction);
			if (!traversed.contains(p)) {
				traversed.add(p);
				if (level.getBlockEntity(p) instanceof BlockPipeEnergyBE pipe) {
					consumer.accept(pipe);
					pipe.traverse(level, p, traversed, consumer);
				}
			}
		}
	}

	@Override
	public int getMaxEnergy() {
		return 800;
	}

	@Override
	public int getMaxEnergyExtract() {
		return 0;
	}

	@Override
	public int getMaxEnergyReceive() {
		return 100;
	}
}
