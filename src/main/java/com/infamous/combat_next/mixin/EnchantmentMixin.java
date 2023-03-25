package com.infamous.combat_next.mixin;

import com.infamous.combat_next.config.MeleeCombatConfigs;
import com.infamous.combat_next.util.CombatUtil;
import com.infamous.combat_next.util.MixinUtil;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

    /*
    @Inject(method = "canEnchant", at = @At("HEAD"), cancellable = true)
    private void handleCanEnchant(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        if(MixinUtil.isSweepingEdge(this) && stack.getItem() instanceof AxeItem && !cir.getReturnValue()){
            cir.setReturnValue(MeleeCombatConfigs.getSweepingEdgeOnAxes().get());
        }
    }
     */

    @ModifyReturnValue(method = "canEnchant", at = @At("RETURN"))
    private boolean modifyCanEnchant(boolean original, ItemStack stack){
        if(!original && MixinUtil.isSweepingEdge(this) && CombatUtil.isAxe(stack)){
            return MeleeCombatConfigs.getSweepingEdgeOnAxes().get();
        }
        return original;
    }
}
