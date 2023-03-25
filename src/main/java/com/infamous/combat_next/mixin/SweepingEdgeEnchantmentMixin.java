package com.infamous.combat_next.mixin;

import com.infamous.combat_next.config.MeleeCombatConfigs;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.SweepingEdgeEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SweepingEdgeEnchantment.class)
public abstract class SweepingEdgeEnchantmentMixin extends Enchantment {

    protected SweepingEdgeEnchantmentMixin(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] slots) {
        super(rarity, category, slots);
    }

    /*
    @Override
    public boolean canEnchant(ItemStack stack) {
        boolean canEnchantAxe = stack.getItem() instanceof AxeItem && MeleeCombatConfigs.getSweepingEdgeOnAxes().get();
        return canEnchantAxe || super.canEnchant(stack);
    }
     */

    /*
    @Inject(method = "getSweepingDamageRatio", at = @At("RETURN"), cancellable = true)
    private static void handleGetSweepingDamageRatio(int level, CallbackInfoReturnable<Float> cir){
        cir.setReturnValue(cir.getReturnValue() * MeleeCombatConfigs.getSweepingDamageScale().get().floatValue());
    }
     */

    @ModifyReturnValue(method = "getSweepingDamageRatio", at = @At("RETURN"))
    private static float modifyGetSweepingDamageRatio(float original, int level){
        return original * MeleeCombatConfigs.getSweepingDamageScale().get().floatValue();
    }
}
