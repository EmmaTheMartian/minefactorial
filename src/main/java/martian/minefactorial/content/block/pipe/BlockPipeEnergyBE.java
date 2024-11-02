package martian.minefactorial.content.block.pipe;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.block.AbstractEnergyBE;
import martian.minefactorial.foundation.block.ITickableBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class BlockPipeEnergyBE extends AbstractEnergyBE implements ITickableBE {
	private Set<BlockPos> outputs = null;

	public BlockPipeEnergyBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.ENERGY_PIPE.get(), pos, blockState);
	}

	@Override
	public void serverTick() {
		if (getEnergyStored() <= 0) {
			return;
		}

		findOutputs();
		if (outputs.isEmpty()) {
			return;
		}

		assert level != null;
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
	private void findOutputs() {
		if (outputs != null) {
			return;
		}

		assert level != null;
		outputs = new HashSet<>();
		traverse(worldPosition, pipe -> {
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
		traverse(worldPosition, pipe -> pipe.outputs = null);
		super.setChanged();
	}

	// This is a generic function that will traverse all cables connected to this cable
	// and call the given consumer for each cable.
	private void traverse(BlockPos pos, Consumer<BlockPipeEnergyBE> consumer) {
		Set<BlockPos> traversed = new HashSet<>();
		traversed.add(pos);
		consumer.accept(this);
		traverse(pos, traversed, consumer);
	}

	private void traverse(BlockPos pos, Set<BlockPos> traversed, Consumer<BlockPipeEnergyBE> consumer) {
		assert level != null;
		for (Direction direction : Direction.values()) {
			BlockPos p = pos.relative(direction);
			if (!traversed.contains(p)) {
				traversed.add(p);
				if (level.getBlockEntity(p) instanceof BlockPipeEnergyBE pipe) {
					consumer.accept(pipe);
					pipe.traverse(p, traversed, consumer);
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
