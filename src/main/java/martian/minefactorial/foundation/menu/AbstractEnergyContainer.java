package martian.minefactorial.foundation.menu;

import martian.minefactorial.foundation.block.AbstractEnergyBE;
import martian.minefactorial.foundation.block.IEnergyBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class AbstractEnergyContainer<T extends AbstractEnergyBE> extends AbstractBlockEntityContainer<T> {
	public final BlockPos pos;
	public final @NotNull T blockEntity;
	protected final Block block;
	protected int power;

	public AbstractEnergyContainer(@Nullable MenuType<?> menuType, Block block, int slotCount, int containerId, Inventory playerInventory, BlockPos pos) {
		super(menuType, block, slotCount, containerId, playerInventory, pos);

		this.block = block;
		this.pos = pos;
		//noinspection unchecked,resource
		this.blockEntity = (T) Objects.requireNonNull(
				playerInventory.player.level().getBlockEntity(pos),
				"AbstractMachineContainer created without a block entity"
		);
	}

	protected void addEnergySlot(IEnergyBE energyBE) {
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return energyBE.getEnergyStored();
			}

			@Override
			public void set(int value) {
				AbstractEnergyContainer.this.power = value;
			}
		});
	}

	public int getPower() { return power; }
}
