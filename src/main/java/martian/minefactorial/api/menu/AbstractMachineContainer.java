package martian.minefactorial.api.menu;

import martian.minefactorial.api.block.AbstractMachineBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMachineContainer<T extends AbstractMachineBE> extends AbstractEnergyContainer<T> {
	protected int work, idle, maxWork, idleTime;

	public AbstractMachineContainer(@Nullable MenuType<?> menuType, Block block, int slotCount, int containerId, Inventory playerInventory, BlockPos pos) {
		super(menuType, block, slotCount, containerId, playerInventory, pos);

		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return blockEntity.getMaxWork();
			}

			@Override
			public void set(int value) {
				maxWork = value;
			}
		});

		addDataSlot(new DataSlot() {
			@Override
			public int get() {
				return blockEntity.getIdleTime();
			}

			@Override
			public void set(int value) {
				idleTime = value;
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

	public int getWork() { return work; }
	public int getIdle() { return idle; }
	public int getMaxWork() { return maxWork; }
	public int getIdleTime() { return idleTime; }
}
