package martian.minefactorial.content.entity;

import martian.minefactorial.content.item.safarinet.SafariNetData;
import martian.minefactorial.content.registry.MFDataComponents;
import martian.minefactorial.content.registry.MFEntityTypes;
import martian.minefactorial.content.registry.MFItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ThrownSafariNet extends ThrowableItemProjectile {
	public static final List<Class<? extends Entity>> BLACKLIST = new ArrayList<>();

	static {
		BLACKLIST.add(FallingBlockEntity.class);
	}

	public @Nullable SafariNetData data;

	public ThrownSafariNet(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
		super(entityType, level);
	}

	public ThrownSafariNet(Level level, LivingEntity shooter) {
		super(MFEntityTypes.THROWN_SAFARI_NET.get(), shooter, level);
	}

	protected void onHitEntity(@NotNull EntityHitResult result) {
		Level level = this.level();
		if (level.isClientSide) {
			return;
		}

		// Release
		if (data != null) {
			data.summon((ServerLevel) level, result.getLocation().x, result.getLocation().y, result.getLocation().z);
		}
		// Capture
		else {
			if (getOwner() != null && result.getEntity().is(getOwner())) {
				return;
			}
			EntityType<?> hitType = result.getEntity().getType();
			if (hitType.is(Tags.EntityTypes.BOSSES)) {
				return;
			} else if (hitType.is(Tags.EntityTypes.CAPTURING_NOT_SUPPORTED)) {
				return;
			} else if (BLACKLIST.contains(result.getEntity().getClass())) {
				return;
			}

			ItemStack stack = MFItems.SAFARI_NET.toStack();
			stack.set(MFDataComponents.SAFARI_NET_DATA, new SafariNetData(hitType, result.getEntity().getPersistentData()));
			level.addFreshEntity(new ItemEntity(level, this.position().x, this.position().y, this.position().z, stack));

			result.getEntity().remove(RemovalReason.DISCARDED);
			this.remove(RemovalReason.DISCARDED);
		}
	}

	@Override
	protected void onHitBlock(@NotNull BlockHitResult result) {
		if (data != null) {
			//noinspection resource
			if (this.level() instanceof ServerLevel level) {
				data.summon(level, result.getLocation().x, result.getLocation().y, result.getLocation().z);
			}
		}
	}

	@Override
	protected @NotNull Item getDefaultItem() {
		return MFItems.SAFARI_NET.get();
	}
}
