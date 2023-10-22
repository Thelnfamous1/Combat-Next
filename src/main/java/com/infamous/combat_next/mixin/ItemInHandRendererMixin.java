package com.infamous.combat_next.mixin;

import com.infamous.combat_next.config.AnimCombatConfigs;
import com.infamous.combat_next.config.MeleeCombatConfigs;
import com.infamous.combat_next.util.CombatUtil;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @ModifyVariable(method = "tick", at = @At(value = "STORE"))
    private boolean playReequipAnimation(boolean original){
        if(!original){
            //noinspection ConstantConditions
            return !CombatUtil.isSupercharged(Minecraft.getInstance().player, 1.0F);
        }
        return true;
    }
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F"))
    private float changeStrengthScale(LocalPlayer instance, float f, Operation<Float> original) {
        if (AnimCombatConfigs.getArmHeightRaisesToCharged().get()) {
            return CombatUtil.getSuperchargedAttackStrengthScale(instance, f);
        } else
            return original.call(instance, f);
    }

    //This works, trust us
    @ModifyVariable(method = "tick", slice = @Slice(
            from = @At(value = "JUMP", ordinal = 3)
    ), at = @At(value = "FIELD", ordinal = 0))
    private float modifyArmHeight(float f) {
        f *= 1 / MeleeCombatConfigs.getAttackStrengthScaleSuperchargeThreshold().get().floatValue();
        f = AnimCombatConfigs.getArmHeightAnim().get() ? f * f * f * 0.25F + 0.75F : f * f * f;
        return f;
    }
}
