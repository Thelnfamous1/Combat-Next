package com.infamous.combat_next.mixin;

import com.infamous.combat_next.config.GeneralCombatConfigs;
import com.infamous.combat_next.util.HitboxInflationType;
import com.infamous.combat_next.util.VoidReturn;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract float getBbWidth();

    @Shadow public abstract float getBbHeight();

    @Inject(method = "onBelowWorld", at = @At("HEAD"), cancellable = true)
    private void handleOutOfWorld(CallbackInfo ci){
        if(this instanceof VoidReturn voidReturn && voidReturn.returnFromVoid()){
            ci.cancel();
        }
    }

    @ModifyReturnValue(method = "getPickRadius", at = @At(value = "RETURN"))
    public float inflateBoxes(float original) {
        if (GeneralCombatConfigs.getHitboxAdjustmentType().get() == HitboxInflationType.CTS) {
            float greatestDimension = Math.max(getBbWidth(), getBbHeight());
            if (greatestDimension < GeneralCombatConfigs.getHitboxMinSizeForHitscan().get()) {
                return (float) ((GeneralCombatConfigs.getHitboxMinSizeForHitscan().get() - greatestDimension) * 0.5F);
            }
        }
        return original;
    }
}
