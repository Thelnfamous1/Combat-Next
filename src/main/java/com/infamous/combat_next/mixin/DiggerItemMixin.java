package com.infamous.combat_next.mixin;

import com.infamous.combat_next.config.MeleeCombatConfigs;
import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DiggerItem.class)
public abstract class DiggerItemMixin extends Item {

    public DiggerItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "hurtEnemy", at = @At("HEAD"), cancellable = true)
    private void handleHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity player, CallbackInfoReturnable<Boolean> cir){
        if(CombatUtil.isAxe(stack) && MeleeCombatConfigs.getAxeHitEnemyChange().get()){
            stack.hurtAndBreak(1, player, (e) -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            cir.setReturnValue(true);
        }
    }

}
