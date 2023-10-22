package com.infamous.combat_next.mixin;

import com.infamous.combat_next.config.AnimCombatConfigs;
import com.infamous.combat_next.config.MeleeCombatConfigs;
import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
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
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F"))
    private float changeStrengthScale(LocalPlayer player, float f) {
        return CombatUtil.getSuperchargedAttackStrengthScale(player, f);
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
