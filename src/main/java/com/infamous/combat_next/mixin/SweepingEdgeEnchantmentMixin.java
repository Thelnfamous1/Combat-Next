package com.infamous.combat_next.mixin;

import com.infamous.combat_next.util.CombatUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.SweepingEdgeEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SweepingEdgeEnchantment.class)
public abstract class SweepingEdgeEnchantmentMixin extends Enchantment {

    protected SweepingEdgeEnchantmentMixin(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] slots) {
        super(rarity, category, slots);
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.getItem() instanceof AxeItem || super.canEnchant(stack);
    }

    @Inject(method = "getSweepingDamageRatio", at = @At("RETURN"), cancellable = true)
    private static void handleGetSweepingDamageRatio(int level, CallbackInfoReturnable<Float> cir){
        cir.setReturnValue(cir.getReturnValue() * CombatUtil.SWEEPING_DAMAGE_SCALE);
    }
}
