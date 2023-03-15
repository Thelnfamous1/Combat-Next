package com.infamous.combat_next.util;

import com.infamous.combat_next.CombatNext;
import com.infamous.combat_next.mixin.ItemAccessor;
import com.infamous.combat_next.mixin.ThrownTridentAccessor;
import com.infamous.combat_next.registry.EnchantmentRegistry;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.Map;
import java.util.function.BiConsumer;

public class CombatUtil {

    private static final String DAMAGE_BOOST_MODIFIER_UUID = "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9";
    private static final double DAMAGE_BOOST_MODIFIER_VALUE = 0.2D;
    private static final int DEFAULT_SHIELD_STUN_TICKS = 32;
    private static final int CLEAVING_TICKS_PER_LEVEL = 10;
    public static final double SHIELD_ARC = -5.0D / 18.0D; // -5/18 * pi = -50 degrees
    public static final int BASE_HEAL_AMOUNT = 6;
    private static final int SNOWBALL_MAX_STACK_SIZE = 64;
    private static final int POTION_MAX_STACK_SIZE = 16;
    public static final int DRINK_USE_DURATION = 20;
    public static final int FOOD_LEVEL_FOR_FOOD_HEALING = 7;
    public static final int TICKS_BEFORE_FOOD_HEALING = 40;
    public static final int HEALING_FOOD_LEVEL_DECREASE_TIME = 2;

    public static void registerTridentDispenseBehavior(){
        DispenserBlock.registerBehavior(Items.TRIDENT, new AbstractProjectileDispenseBehavior() {
            protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
                ThrownTrident thrownTrident = new ThrownTrident(EntityType.TRIDENT, level);
                ((ThrownTridentAccessor)thrownTrident).setTridentItem(stack.copy());
                thrownTrident.getEntityData().set(ThrownTridentAccessor.getID_LOYALTY(), (byte) EnchantmentHelper.getLoyalty(stack));
                thrownTrident.getEntityData().set(ThrownTridentAccessor.getID_FOIL(), stack.hasFoil());
                thrownTrident.setPos(position.x(), position.y(), position.z());
                thrownTrident.pickup = AbstractArrow.Pickup.ALLOWED;
                return thrownTrident;
            }
        });
        CombatNext.LOGGER.info("Registered DispenseItemBehavior for Item {}", "minecraft:trident");
    }

    public static void modifyStrengthEffect(){
        MobEffects.DAMAGE_BOOST.addAttributeModifier(Attributes.ATTACK_DAMAGE, DAMAGE_BOOST_MODIFIER_UUID, DAMAGE_BOOST_MODIFIER_VALUE, AttributeModifier.Operation.MULTIPLY_TOTAL);
        CombatNext.LOGGER.info("Modified MobEffect {} to have an AttributeModifier for Attribute {} with UUID {}, value of {}, and Operation of {}",
                "minecraft:strength", Attributes.ATTACK_DAMAGE, DAMAGE_BOOST_MODIFIER_UUID, DAMAGE_BOOST_MODIFIER_VALUE,
                AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public static void modifyItemMaxStackSizes(){
        ((ItemAccessor)Items.SNOWBALL).setMaxStackSize(SNOWBALL_MAX_STACK_SIZE);
        CombatNext.LOGGER.info("Changed max stack size of {} to {}", "minecraft:snowball", SNOWBALL_MAX_STACK_SIZE);
        ((ItemAccessor)Items.POTION).setMaxStackSize(POTION_MAX_STACK_SIZE);
        CombatNext.LOGGER.info("Changed max stack size of {} to {}", "minecraft:potion", POTION_MAX_STACK_SIZE);
    }

    public static boolean canInterruptConsumption(DamageSource source){
        return source.getDirectEntity() instanceof LivingEntity || (source.isProjectile() && source.getEntity() instanceof LivingEntity);
    }

    public static float getDamageBonusRedirect(ItemStack stack, Entity target) {
        if (target instanceof LivingEntity victim) {
            return getDamageBonus(stack, victim);
        } else{
            return EnchantmentHelper.getDamageBonus(stack, MobType.UNDEFINED);
        }
    }

    public static float getDamageBonus(ItemStack stack, LivingEntity target) {
        MutableFloat mutablefloat = new MutableFloat();
        runIterationOnItem((enchantment, level) -> {
            if(enchantment == Enchantments.IMPALING){
                mutablefloat.add(getImpalingDamageBonus(level, target));
            } else{
                mutablefloat.add(enchantment.getDamageBonus(level, target.getMobType(), stack));
            }
        }, stack);
        return mutablefloat.floatValue();
    }

    private static void runIterationOnItem(BiConsumer<Enchantment, Integer> enchantmentVisitor, ItemStack stack) {
        if (!stack.isEmpty()) {
            // forge: redirect enchantment logic to allow non-NBT enchants
            for (Map.Entry<Enchantment, Integer> entry : stack.getAllEnchantments().entrySet()) {
                enchantmentVisitor.accept(entry.getKey(), entry.getValue());
            }
        }
    }
    public static float getImpalingDamageBonus(int level, LivingEntity target){
        return target.isInWaterOrRain() ? (float)level * 2.5F : 0.0F;
    }

    public static void newDisableShield(Player victim, LivingEntity attacker){
        int cleavingLevel = attacker.getMainHandItem().getEnchantmentLevel(EnchantmentRegistry.CLEAVING.get());
        int cleavingTicks = CLEAVING_TICKS_PER_LEVEL * cleavingLevel;
        victim.getCooldowns().addCooldown(victim.getUseItem().getItem(), DEFAULT_SHIELD_STUN_TICKS + cleavingTicks);
        victim.stopUsingItem();
        victim.level.broadcastEntityEvent(victim, (byte)30);
    }
}
