package com.infamous.combat_next.mixin;

import com.infamous.combat_next.util.CombatUtil;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {

    protected MobMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    /*
    @Redirect(method = "doHurtTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getDamageBonus(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/MobType;)F"))
    private float wrapGetDamageBonus(ItemStack stack, MobType mobType, Entity entity){
        float damageBonus = EnchantmentHelper.getDamageBonus(stack, mobType);
        damageBonus = CombatUtil.recalculateDamageBonus(stack, damageBonus, entity);
        damageBonus = CombatUtil.scaleDamageBonus(this, damageBonus);
        return damageBonus;
    }
     */

    @ModifyExpressionValue(method = "doHurtTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getDamageBonus(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/MobType;)F"))
    private float modifyGetDamageBonus(float damageBonus, Entity entity){
        damageBonus = CombatUtil.recalculateDamageBonus(this.getMainHandItem(), damageBonus, entity);
        damageBonus = CombatUtil.scaleDamageBonus(this, damageBonus);
        return damageBonus;
    }
}
