package martian.minefactorial.foundation.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class AbstractBlockEntityContainer<T extends BlockEntity> extends AbstractMFContainer {
	public final BlockPos pos;
	public final @NotNull T blockEntity;
	protected final Block block;

	public AbstractBlockEntityContainer(@Nullable MenuType<?> menuType, Block block, int slotCount, int containerId, Inventory playerInventory, BlockPos pos) {
		super(menuType, slotCount, containerId);

		this.block = block;
		this.pos = pos;
		//noinspection unchecked,resource
		this.blockEntity = (T) Objects.requireNonNull(
				playerInventory.player.level().getBlockEntity(pos),
				"AbstractMachineContainer created without a block entity"
		);
	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		return stillValid(ContainerLevelAccess.create(player.level(), this.pos), player, this.block);
	}
}
