package com.infamous.combat_next.mixin;

import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @ModifyVariable(method = "tick", at = @At(value = "STORE"))
    private boolean playReequipAnimation(boolean original){
        if(!original){
            //noinspection ConstantConditions
            return !CombatUtil.isAttackAvailable(Minecraft.getInstance().player, 1.0F);
        }
        return true;
    }
}
