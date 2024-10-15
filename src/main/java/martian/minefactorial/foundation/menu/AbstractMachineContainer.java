package martian.minefactorial.foundation.menu;

import martian.minefactorial.foundation.block.AbstractMachineBE;
import martian.minefactorial.foundation.block.IEnergyBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMachineContainer<T extends BlockEntity> extends AbstractMFContainer {
	public final BlockPos pos;
	public final T blockEntity;
	protected final Block block;
	protected int power, work, idle;

	public AbstractMachineContainer(@Nullable MenuType<?> menuType, Block block, int slotCount, int containerId, Inventory playerInventory, BlockPos pos) {
		super(menuType, slotCount, containerId);

		this.block = block;
		this.pos = pos;
		//noinspection unchecked
		this.blockEntity = (T) (playerInventory.player.level().getBlockEntity(pos));
	}

	protected void addEnergySlot(IEnergyBE energyBE) {
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return energyBE.getEnergyStored();
			}

			@Override
			public void set(int value) {
				AbstractMachineContainer.this.power = value;
			}
		});
	}

	protected void addWorkSlot(AbstractMachineBE machineBE) {
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return machineBE.getCurrentWork();
			}

			@Override
			public void set(int value) {
				AbstractMachineContainer.this.work = value;
			}
		});
	}

	protected void addIdleSlot(AbstractMachineBE machineBE) {
		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return machineBE.getCurrentIdleTime();
			}

			@Override
			public void set(int value) {
				AbstractMachineContainer.this.idle = value;
			}
		});
	}

	public int getPower() { return power; }
	public int getWork() { return work; }
	public int getIdle() { return idle; }

	@Override
	public boolean stillValid(@NotNull Player player) {
		return stillValid(ContainerLevelAccess.create(player.level(), this.pos), player, this.block);
	}
}
