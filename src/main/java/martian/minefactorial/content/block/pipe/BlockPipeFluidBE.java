package martian.minefactorial.content.block.pipe;

import martian.minefactorial.content.registry.MFBlockEntityTypes;
import martian.minefactorial.foundation.block.AbstractSingleTankBE;
import martian.minefactorial.foundation.block.ITickableBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class BlockPipeFluidBE extends AbstractSingleTankBE implements ITickableBE {
	private Set<BlockPos> outputs = null;

	public BlockPipeFluidBE(BlockPos pos, BlockState blockState) {
		super(MFBlockEntityTypes.FLUID_PIPE.get(), 1000, pos, blockState);
	}

	@Override
	public void serverTick() {
		if (getTank().getFluidAmount() <= 0) {
			return;
		}

		// Only do something if we have fluid
		checkOutputs();

		assert level != null;

		if (outputs.isEmpty()) {
			return;
		}

		// Distribute fluid over all outputs
		int amount = getTank().getFluidAmount() / outputs.size();
		FluidStack stack = getTank().getFluid().copyWithAmount(amount);
		for (BlockPos p : outputs) {
			BlockEntity te = level.getBlockEntity(p);
			if (te != null) {
				IFluidHandler storage = level.getCapability(Capabilities.FluidHandler.BLOCK, te.getBlockPos(), null);
				if (storage != null) {
					int received = storage.fill(stack, IFluidHandler.FluidAction.EXECUTE);
					getTank().drain(received, IFluidHandler.FluidAction.EXECUTE);
					setChanged();
				}
			}
		}
	}

	// This function will cache all outputs for this cable network. It will do this
	// by traversing all cables connected to this cable and then check for all energy
	// receivers around those cables.
	private void checkOutputs() {
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
				if (te != null && !(te instanceof BlockPipeFluidBE)) {
					IFluidHandler storage = level.getCapability(Capabilities.FluidHandler.BLOCK, te.getBlockPos(), null);
					if (storage != null) {
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
	private void traverse(BlockPos pos, Consumer<BlockPipeFluidBE> consumer) {
		Set<BlockPos> traversed = new HashSet<>();
		traversed.add(pos);
		consumer.accept(this);
		traverse(pos, traversed, consumer);
	}

	private void traverse(BlockPos pos, Set<BlockPos> traversed, Consumer<BlockPipeFluidBE> consumer) {
		assert level != null;
		for (Direction direction : Direction.values()) {
			BlockPos p = pos.relative(direction);
			if (!traversed.contains(p)) {
				traversed.add(p);
				if (level.getBlockEntity(p) instanceof BlockPipeFluidBE pipe) {
					consumer.accept(pipe);
					pipe.traverse(p, traversed, consumer);
				}
			}
		}
	}

	@Override
	public int getMaxFluidExtract() {
		return 0;
	}

	@Override
	public int getMaxFluidReceive() {
		return 100;
	}
}
