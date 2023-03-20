package com.infamous.combat_next.mixin;

import com.infamous.combat_next.config.RangedCombatConfigs;
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

    @ModifyConstant(method = "releaseUsing", constant = @Constant(floatValue = 1.0F, ordinal = 0))
    private float getInaccuracy(float original, ItemStack stack, Level level, LivingEntity livingEntity, int useItemRemainingTicks){
        if(RangedCombatConfigs.getBowOverdrawing().get()){
            int useTicks = this.getUseDuration(stack) - useItemRemainingTicks;
            return useTicks <= RangedCombatConfigs.getBowTicksBeforeOverdrawn().get() ? 0.0F : RangedCombatConfigs.getBowOverdrawnArrowInaccuracy().get().floatValue();
        } else{
            return original;
        }
    }

    @ModifyVariable(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;setCritArrow(Z)V"))
    private AbstractArrow modifyArrowCrit(AbstractArrow arrow, ItemStack stack, Level level, LivingEntity livingEntity, int useItemRemainingTicks){
        if(RangedCombatConfigs.getBowOverdrawing().get()){
            int useTicks = this.getUseDuration(stack) - useItemRemainingTicks;
            if(useTicks > RangedCombatConfigs.getBowTicksBeforeOverdrawn().get()){
                arrow.setCritArrow(false);
            }
        }
        return arrow;
    }


}
