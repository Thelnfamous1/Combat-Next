package com.infamous.combat_next.util;

import com.infamous.combat_next.CombatNext;
import com.infamous.combat_next.client.ClientCombatUtil;
import com.infamous.combat_next.mixin.ItemAccessor;
import com.infamous.combat_next.mixin.ThrownTridentAccessor;
import com.infamous.combat_next.network.CNNetwork;
import com.infamous.combat_next.network.ServerboundMissPacket;
import com.infamous.combat_next.registry.EnchantmentRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

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
    private static final double MIN_HITBOX_SIZE_FOR_ATTACK = 0.9D;

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

    public static void attackEmpty(Player player) {
        if(player.level.isClientSide){
            ClientCombatUtil.ensureHasSentCarriedItem();
            CNNetwork.INSTANCE.sendToServer(ServerboundMissPacket.createMissPacket());
        }
        if (!player.isSpectator()) {
            sweepAttack(player);
            player.resetAttackStrengthTicker();
        }
    }

    private static void sweepAttack(Player player){
        float attackDamage = (float)player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float damageBonus = EnchantmentHelper.getDamageBonus(player.getMainHandItem(), MobType.UNDEFINED);

        float attackStrengthScale = player.getAttackStrengthScale(0.5F);
        attackDamage *= 0.2F + attackStrengthScale * attackStrengthScale * 0.8F;
        damageBonus *= attackStrengthScale;
        if (attackDamage > 0.0F || damageBonus > 0.0F) {
            boolean fullStrength = attackStrengthScale > 0.9F;
            boolean sprintAttack = player.isSprinting() && fullStrength;
            boolean critAttack = fullStrength && player.fallDistance > 0.0F && !player.isOnGround() && !player.onClimbable() && !player.isInWater() && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger();
            critAttack = critAttack && !player.isSprinting();

            attackDamage += damageBonus;

            boolean sweepAttack = false;
            double walkDistD = player.walkDist - player.walkDistO;
            if (fullStrength && !critAttack && !sprintAttack && player.isOnGround() && walkDistD < (double)player.getSpeed()) {
                ItemStack itemInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
                sweepAttack = itemInHand.getEnchantmentLevel(Enchantments.SWEEPING_EDGE) > 0;
            }

            if(sweepAttack){
                float sweepDamage = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * attackDamage;

                for(LivingEntity sweepTarget : player.level.getEntitiesOfClass(LivingEntity.class, getSweepHitBox(player, player.getItemInHand(InteractionHand.MAIN_HAND)))) {
                    if (sweepTarget != player && !player.isAlliedTo(sweepTarget) && (!(sweepTarget instanceof ArmorStand armorStand) || !armorStand.isMarker()) && player.canHit(sweepTarget, 0)) { // Original check was dist < 3, range is 3, so vanilla used padding=0
                        sweepTarget.knockback(0.4D, Mth.sin(player.getYRot() * ((float)Math.PI / 180F)), -Mth.cos(player.getYRot() * ((float)Math.PI / 180F)));
                        sweepTarget.hurt(DamageSource.playerAttack(player), sweepDamage);
                    }
                }

                player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
                player.sweepAttack();
            }
        }
    }

    private static AABB getSweepHitBox(Player player, ItemStack weapon) {
        double xShift = (-Mth.sin(player.yBodyRot * ((float)Math.PI / 180F))) * 2.0D;
        double zShift = Mth.cos(player.yBodyRot * ((float)Math.PI / 180F)) * 2.0D;
        // we use the player as the "target", so weapons with custom sweep hitboxes work
        return weapon.getSweepHitBox(player, player).move(xShift, 0.0, zShift);
    }

    public static boolean onAttackCooldown(Player player, float partialTick) {
        return player.getAttackStrengthScale(partialTick) < 1.0F;
    }

    @NotNull
    public static AABB getAttackableBoundingBox(Entity instance) {
        AABB boundingBox = instance.getBoundingBox();
        if(boundingBox.getSize() < MIN_HITBOX_SIZE_FOR_ATTACK){
            double xAdjust = adjustSize(boundingBox.getXsize());
            double yAdjust = adjustSize(boundingBox.getYsize());
            double zAdjust = adjustSize(boundingBox.getZsize());
            boundingBox = boundingBox.inflate(xAdjust, yAdjust, zAdjust);
        }
        return boundingBox;
    }

    private static double adjustSize(double size) {
        return size < MIN_HITBOX_SIZE_FOR_ATTACK ? (MIN_HITBOX_SIZE_FOR_ATTACK - size) / 2.0D : 0.0D;
    }

    public static boolean hitThroughBlock(Level level, BlockPos blockPos, Player player, Predicate<Player> hitEntity){
        BlockState clickedBlock = level.getBlockState(blockPos);
        if (!clickedBlock.getCollisionShape(level, blockPos).isEmpty() || clickedBlock.getDestroySpeed(level, blockPos) != 0.0F) {
            return false;
        }
        return hitEntity.test(player);
    }

    public static boolean hitEntity(Player player) {
        double blockReach = player.getReachDistance();
        Vec3 eyePosition = player.getEyePosition(1.0F);
        double reach;
        double entityReach = player.getAttackRange();
        if (player.isCreative()) {
            reach = Math.max(blockReach, entityReach);
            blockReach = reach;
        } else {
            blockReach = reach = Math.max(blockReach, entityReach); // Pick entities with the max of the reach distance and attack range.
        }

        double reachSqr = Mth.square(reach);

        Vec3 from = player.getViewVector(1.0F);
        Vec3 to = eyePosition.add(from.x * blockReach, from.y * blockReach, from.z * blockReach);
        AABB aabb = player.getBoundingBox().expandTowards(from.scale(blockReach)).inflate(1.0D, 1.0D, 1.0D);
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(player, eyePosition, to, aabb, (e) -> !e.isSpectator() && e.isPickable(), reachSqr);
        if (entityHitResult != null) {
            return isValidHit(entityHitResult, eyePosition, reachSqr, Mth.square(entityReach));
        }
        return false;
    }

    public static boolean isValidHit(EntityHitResult entityHitResult, Vec3 eyePosition, double reachSqr, double entityReachSqr) {
        Vec3 hitLocation = entityHitResult.getLocation();
        double distanceToSqr = eyePosition.distanceToSqr(hitLocation);
        return distanceToSqr <= entityReachSqr && distanceToSqr <= reachSqr;
    }
}
