package com.infamous.combat_next.mixin;

import com.infamous.combat_next.config.RangedCombatConfigs;
import com.infamous.combat_next.util.CombatUtil;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(BowItem.class)
public abstract class BowItemMixin {

    @Shadow public abstract int getUseDuration(ItemStack p_40680_);

    /*
    @ModifyConstant(method = "releaseUsing", constant = @Constant(floatValue = 1.0F, ordinal = 0))
    private float getInaccuracy(float original, ItemStack stack, Level level, LivingEntity livingEntity, int useItemRemainingTicks){
        if(RangedCombatConfigs.getBowOverdrawing().get()){
            int useTicks = this.getUseDuration(stack) - useItemRemainingTicks;
            return useTicks <= RangedCombatConfigs.getBowTicksBeforeOverdrawn().get() ? 0.0F : RangedCombatConfigs.getBowOverdrawnArrowInaccuracyMax().get().floatValue();
        } else{
            return original;
        }
    }
     */

    @ModifyExpressionValue(method = "releaseUsing", at = @At(value = "CONSTANT", args = "floatValue=1.0F", ordinal = 0))
    private float modifyInaccuracy(float original, ItemStack stack, Level level, LivingEntity livingEntity, int useItemRemainingTicks){
        if(RangedCombatConfigs.getBowOverdrawing().get()){
            int useTicks = this.getUseDuration(stack) - useItemRemainingTicks;
            return CombatUtil.getFatigueForTime(useTicks) * RangedCombatConfigs.getBowOverdrawnArrowInaccuracyDefault().get().floatValue();
        } else{
            return original;
        }
    }

    @ModifyVariable(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;setCritArrow(Z)V", shift = At.Shift.AFTER))
    private AbstractArrow modifyArrowCrit(AbstractArrow arrow, ItemStack stack, Level level, LivingEntity livingEntity, int useItemRemainingTicks){
        if(RangedCombatConfigs.getBowOverdrawing().get()){
            int useTicks = this.getUseDuration(stack) - useItemRemainingTicks;
            if(CombatUtil.getFatigueForTime(useTicks) > 0.5F){
                arrow.setCritArrow(false);
            }
        }
        return arrow;
    }


}
