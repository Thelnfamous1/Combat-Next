package com.infamous.combat_next.mixin;

import com.infamous.combat_next.config.ConfigUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BowItem.class)
public abstract class BowItemMixin {

    @Shadow public abstract int getUseDuration(ItemStack p_40680_);

    @ModifyConstant(method = "releaseUsing", constant = @Constant(floatValue = 1.0F, ordinal = 0))
    private float getInaccuracy(float constant, ItemStack stack, Level level, LivingEntity livingEntity, int useItemRemainingTicks){
        int useTicks = this.getUseDuration(stack) - useItemRemainingTicks;
        return useTicks <= ConfigUtil.getBowTicksBeforeOverdrawn() ? 0.0F : ConfigUtil.getBowArrowInaccuracy();
    }

    @ModifyConstant(method = "releaseUsing", constant = @Constant(floatValue = 1.0F, ordinal = 1))
    private float getMaxPower(float constant, ItemStack stack, Level level, LivingEntity livingEntity, int useItemRemainingTicks){
        int useTicks = this.getUseDuration(stack) - useItemRemainingTicks;
        return useTicks > ConfigUtil.getBowTicksBeforeOverdrawn() ? 5.0F : constant; // 5.0F is just to cause the if check to fail for making an arrow crit
    }


}
