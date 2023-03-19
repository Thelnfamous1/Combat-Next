package com.infamous.combat_next.mixin;

import com.infamous.combat_next.config.ConfigUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class FoodDataMixin {

    private int healingFoodLevelDecreaseTimer = ConfigUtil.getNaturalHealingTicksBeforeFoodLevelDecrease();
    @Shadow private int foodLevel;

    @Shadow private int tickTimer;

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 20, ordinal = 0))
    private int getFoodLevelForFastHealing(int constant){
        return this.foodLevel + 1; // since "this.foodLevel == this.foodLevel + 1" will fail, no fast healing
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 18, ordinal = 0))
    private int getFoodLevelForNaturalHealing(int constant){
        return ConfigUtil.getNaturalHealingMinFoodLevel();
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 80, ordinal = 0))
    private int getTicksBeforeNaturalHealing(int constant){
        return ConfigUtil.getNaturalHealingTicksBeforeHeal();
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;addExhaustion(F)V", ordinal = 1), cancellable = true)
    private void handleTick(Player player, CallbackInfo ci){
        ci.cancel();
        this.healingFoodLevelDecreaseTimer--;
        if(this.healingFoodLevelDecreaseTimer <= 0){
            this.healingFoodLevelDecreaseTimer = ConfigUtil.getNaturalHealingTicksBeforeFoodLevelDecrease();
            this.foodLevel--;
        }
        // set after addExhaustion was called originally, need to also do it here
        this.tickTimer = 0;
    }
}
