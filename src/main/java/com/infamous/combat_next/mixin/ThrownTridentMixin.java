package com.infamous.combat_next.mixin;

import com.infamous.combat_next.config.RangedCombatConfigs;
import com.infamous.combat_next.util.CombatUtil;
import com.infamous.combat_next.util.VoidReturn;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow implements VoidReturn {

    protected ThrownTridentMixin(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
    }

    @Shadow @Final private static EntityDataAccessor<Byte> ID_LOYALTY;

    @Shadow private ItemStack tridentItem;

    @Shadow private boolean dealtDamage;

    /*
    @Override
    public void outOfWorld() {
        if(!this.returnFromVoid()){
            super.outOfWorld();
        }
    }
     */

    @Override
    public boolean returnFromVoid() {
        int loyalty = this.entityData.get(ID_LOYALTY);
        if(loyalty > 0 && RangedCombatConfigs.getTridentLoyaltyReturnFromVoid().get()) {
            this.dealtDamage = true;
            return true;
        } else{
            return false;
        }
    }

    /*
    @Redirect(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getDamageBonus(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/MobType;)F"))
    private float wrapGetDamageBonus(ItemStack stack, MobType mobType, EntityHitResult entityHitResult){
        float damageBonus = EnchantmentHelper.getDamageBonus(stack, mobType);
        damageBonus = CombatUtil.recalculateDamageBonus(stack, damageBonus, entityHitResult.getEntity());
        return damageBonus;
    }
     */

    @ModifyExpressionValue(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getDamageBonus(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/MobType;)F"))
    private float modifyGetDamageBonus(float damageBonus, EntityHitResult entityHitResult){
        damageBonus = CombatUtil.recalculateDamageBonus(this.tridentItem, damageBonus, entityHitResult.getEntity());
        return damageBonus;
    }
}
