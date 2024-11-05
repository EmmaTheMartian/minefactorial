package martian.minefactorial.foundation;

import martian.minefactorial.Minefactorial;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;

import javax.annotation.Nullable;
import java.util.List;

public final class FakePlayerHelpers {
	public static final ArgLazy<FakePlayer, ServerLevel> FAKE_PLAYER = new ArgLazy<>(FakePlayerFactory::getMinecraft);

	private FakePlayerHelpers() { }

	public static List<ItemStack> breakBlockAndGetDrops(ServerLevel serverLevel, BlockState blockState, BlockPos pos) {
		FakePlayer fakePlayer = FAKE_PLAYER.get(serverLevel);
		BlockEntity blockentity = blockState.hasBlockEntity() ? serverLevel.getBlockEntity(pos) : null;
		List<ItemStack> drops = blockState.getDrops(new LootParams.Builder(serverLevel)
				.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
				.withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
				.withParameter(LootContextParams.THIS_ENTITY, fakePlayer)
				.withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockentity));
		serverLevel.destroyBlock(pos, false, fakePlayer);
		return drops;
	}

	public static List<ItemStack> breakBlockAndGetDrops(ServerLevel serverLevel, BlockPos pos) {
		return breakBlockAndGetDrops(serverLevel, serverLevel.getBlockState(pos), pos);
	}

	public static void placeBlockItem(ServerLevel level, BlockPos pos, ItemStack blockItemStack, @Nullable Direction direction) {
		FakePlayer fakePlayer = FAKE_PLAYER.get(level);
		if (blockItemStack.getItem() instanceof BlockItem blockItem) {
			blockItem.place(new BlockPlaceContext(
					fakePlayer,
					InteractionHand.MAIN_HAND,
					blockItemStack,
					new BlockHitResult(Vec3.ZERO, direction == null ? Direction.UP : direction, pos, true)
			));
		} else {
			Minefactorial.LOGGER.error("FakePlayerHelper#placeBlockItem was provided an item stack that was not a BlockItem! ItemStack: {}", blockItemStack);
		}
	}
}
