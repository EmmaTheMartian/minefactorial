package martian.minefactorial.mixins;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import martian.minefactorial.foundation.entity.IMixinLivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Monster.class)
public class MixinMonster extends PathfinderMob implements Enemy {
	protected MixinMonster(EntityType<? extends PathfinderMob> entityType, Level level) {
		super(entityType, level);
	}

	@WrapMethod(method = "shouldDropLoot")
	protected boolean minefactorial$shouldDropLoot(Operation<Boolean> original) {
		if (((IMixinLivingEntity) this).minefactorial$shouldSkipDrops()) {
			return false;
		}
		return original.call();
	}
}
