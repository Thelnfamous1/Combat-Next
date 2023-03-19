package com.infamous.combat_next.mixin;

import com.infamous.combat_next.config.ConfigUtil;
import com.infamous.combat_next.data.CNTags;
import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract boolean is(TagKey<Item> tagKey);

    @Shadow public abstract UseAnim getUseAnimation();

    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
    private void handleGetUseDuration(CallbackInfoReturnable<Integer> cir){
        if(CombatUtil.isEatOrDrink(this.getUseAnimation()) && this.is(CNTags.FAST_DRINKS)){
            cir.setReturnValue(ConfigUtil.getDrinkUseDurationTicks());
        }
    }
}
