package martian.minefactorial.mixins;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import martian.minefactorial.foundation.entity.IMixinLivingEntity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public class MixinLivingEntity implements IMixinLivingEntity {
	@Unique
	private boolean minefactorial$shouldSkipDrops = false;

	@WrapMethod(method = "shouldDropLoot")
	protected boolean minefactorial$shouldDropLoot(Operation<Boolean> original) {
		if (minefactorial$shouldSkipDrops()) {
			return false;
		}
		return original.call();
	}

	@Unique
	public void minefactorial$setShouldSkipDrops(boolean value) {
		minefactorial$shouldSkipDrops = value;
	}

	@Unique
	public boolean minefactorial$shouldSkipDrops() {
		return minefactorial$shouldSkipDrops;
	}
}
