package martian.minefactorial.content.item;

import martian.minefactorial.foundation.block.AbstractMachineBE;
import martian.minefactorial.foundation.block.IScrewdriverFunctionality;
import martian.minefactorial.foundation.item.MFItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ItemScrewdriver extends MFItem {
	public ItemScrewdriver(Item.Properties properties, String... hoverText) {
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

		// If the block manually defines screwdriver functionality, use that
		if (block instanceof IScrewdriverFunctionality screwdriverFunctionality) {
			screwdriverFunctionality.onUseScrewdriver(context.getPlayer(), (ServerLevel) level, state, pos);
			return InteractionResult.SUCCESS_NO_ITEM_USED;
		}

		// If the block is a machine, we toggle auto-eject
		if (level.getBlockEntity(pos) instanceof AbstractMachineBE machineBE) {
			machineBE.autoEject = !machineBE.autoEject;
			Player player = context.getPlayer();
			if (player != null) {
				if (machineBE.autoEject) {
					player.sendSystemMessage(Component.translatable("messages.minefactorial.enabled_auto_eject"));
				} else {
					player.sendSystemMessage(Component.translatable("messages.minefactorial.disabled_auto_eject"));
				}
			}
			machineBE.setChanged();
			return InteractionResult.SUCCESS_NO_ITEM_USED;
		}

		return InteractionResult.SUCCESS_NO_ITEM_USED;
	}
}
