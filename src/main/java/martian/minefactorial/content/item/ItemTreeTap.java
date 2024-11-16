package martian.minefactorial.content.item;

import martian.minefactorial.content.block.foliage.BlockRubberWood;
import martian.minefactorial.content.registry.MFItems;
import martian.minefactorial.foundation.item.MFItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ItemTreeTap extends MFItem {
	public ItemTreeTap(Properties properties, String... hoverText) {
		super(properties, hoverText);
	}

	@Override
	public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);

		if (state.hasProperty(BlockRubberWood.HAS_RUBBER) && state.getValue(BlockRubberWood.HAS_RUBBER)) {
			if (!level.isClientSide) {
				Player player = context.getPlayer();
				if (player == null) {
					return InteractionResult.FAIL;
				}

				// Consume durability
				EquipmentSlot slot = context.getHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
				context.getItemInHand().hurtAndBreak(1, player, slot);
				// Remove the rubber from the interacted block
				level.setBlockAndUpdate(pos, state.setValue(BlockRubberWood.HAS_RUBBER, false));
				// Spawn one raw rubber
				Vec3 spawnPos = pos.relative(context.getClickedFace()).getCenter();
				level.addFreshEntity(new ItemEntity(level, spawnPos.x, spawnPos.y, spawnPos.z, MFItems.RAW_RUBBER.toStack()));
			}
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.FAIL;
	}
}
