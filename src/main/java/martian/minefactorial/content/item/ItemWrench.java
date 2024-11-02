package martian.minefactorial.content.item;

import martian.minefactorial.foundation.block.IWrenchFunctionality;
import martian.minefactorial.foundation.item.MFItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ItemWrench extends MFItem {
	public ItemWrench(Properties properties, String... hoverText) {
		super(properties, hoverText);
	}

	@Override
	public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
		Level level = context.getLevel();

		if (level.isClientSide)
			return InteractionResult.SUCCESS_NO_ITEM_USED;

		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);
		Block block = state.getBlock();

		// If the block manually defines wrench functionality, use that
		if (block instanceof IWrenchFunctionality wrenchFunctionality) {
			wrenchFunctionality.onWrenched(context.getPlayer(), (ServerLevel) level, state, pos);
			return InteractionResult.SUCCESS_NO_ITEM_USED;
		}

		// Try to rotate to the face that the player clicked
		Optional<Direction> facing = state.getOptionalValue(BlockStateProperties.FACING);
		if (facing.isPresent()) {
			if (facing.get() != context.getClickedFace()) {
				level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.FACING, context.getClickedFace()));
			}
			return InteractionResult.SUCCESS_NO_ITEM_USED;
		}

		// If the block does not support rotating up/down, it may support only north/east/south/west
		facing = state.getOptionalValue(BlockStateProperties.HORIZONTAL_FACING);
		if (facing.isPresent()) {
			if (facing.get() != context.getClickedFace()) {
				level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.FACING, context.getHorizontalDirection()));
			}
			return InteractionResult.SUCCESS_NO_ITEM_USED;
		}

		// Finally, fallback to simply Block#rotate
		BlockState rotated = block.rotate(state, level, pos, Rotation.CLOCKWISE_90);
		if (rotated != state) {
			level.setBlockAndUpdate(pos, rotated);
		}

		return InteractionResult.SUCCESS_NO_ITEM_USED;
	}
}
