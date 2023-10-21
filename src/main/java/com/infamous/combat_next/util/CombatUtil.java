package com.infamous.combat_next.util;

import com.infamous.combat_next.CombatNext;
import com.infamous.combat_next.client.ClientCombatUtil;
import com.infamous.combat_next.config.*;
import com.infamous.combat_next.mixin.ItemAccessor;
import com.infamous.combat_next.mixin.LivingEntityAccessor;
import com.infamous.combat_next.mixin.ThrownTridentAccessor;
import com.infamous.combat_next.network.CNNetwork;
import com.infamous.combat_next.network.ServerboundMissPacket;
import com.infamous.combat_next.registry.EnchantmentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class CombatUtil {
    public static final String SHIELD_STRENGTH_DESCRIPTION_ID = String.format("item.minecraft.shield.%s", "shield_strength");
    private static final UUID BONUS_REACH_MODIFIER_UUID = UUID.fromString("30a9271c-d6b2-4651-b088-800acc43f282");
    private static final String DAMAGE_BOOST_MODIFIER_UUID = "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9";
    private static final String BONUS_REACH_MODIFIER_NAME = new ResourceLocation(CombatNext.MODID, "bonus_reach").toString();
    private static final UUID SHIELD_KNOCKBACK_RESISTANCE_MODIFIER_UUID = UUID.fromString("8e2dc47b-7ada-433b-9418-f99b4fdc1409");
    private static final String SHIELD_KNOCKBACK_RESISTANCE_MODIFIER_NAME = new ResourceLocation(CombatNext.MODID, "shield_knockback_resistance").toString();
    private static final float IMPALING_DAMAGE_SCALE = 2.5F;
    private static final int SHIELD_BREAK_EVENT_ID = 30;
    public static final int DEFAULT_SHIELD_DISABLE_TIME = 32;

    @Nullable
    private static AttributeModifier ORIGINAL_STRENGTH_MODIFIER;

    private static void registerTridentDispenseBehavior(){
        if(RangedCombatConfigs.getTridentShootFromDispenser().get()){
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
    }

    private static void modifyStrengthEffect(){
        if(MagicCombatConfigs.getStrengthEffectChange().get()){
            ORIGINAL_STRENGTH_MODIFIER = new AttributeModifier(UUID.fromString("648D7064-6A60-4F59-8ABE-C2C23A6DD7A9"), MobEffects.DAMAGE_BOOST.getDescriptionId(), 0.0D, AttributeModifier.Operation.ADDITION);
            MobEffects.DAMAGE_BOOST.addAttributeModifier(Attributes.ATTACK_DAMAGE, DAMAGE_BOOST_MODIFIER_UUID, MagicCombatConfigs.getStrengthEffectModifierValue().get(), AttributeModifier.Operation.MULTIPLY_TOTAL);
            CombatNext.LOGGER.info("Modified MobEffect {} to have an AttributeModifier for Attribute {} with UUID {}, value of {}, and Operation of {}",
                    "minecraft:strength", getTranslation(Attributes.ATTACK_DAMAGE.getDescriptionId()), DAMAGE_BOOST_MODIFIER_UUID, MagicCombatConfigs.getStrengthEffectModifierValue().get(),
                    AttributeModifier.Operation.MULTIPLY_TOTAL);
        } else{
            if(ORIGINAL_STRENGTH_MODIFIER != null){
                CombatNext.LOGGER.info("Reset MobEffect {} to have its original Attribute Modifier {}!", "minecraft:strength", ORIGINAL_STRENGTH_MODIFIER);
                MobEffects.DAMAGE_BOOST.getAttributeModifiers().put(Attributes.ATTACK_DAMAGE, ORIGINAL_STRENGTH_MODIFIER);
            }
        }
    }

    private static String getTranslation(String descriptionId) {
        return Language.getInstance().getOrDefault(descriptionId);
    }

    private static void modifyItemMaxStackSizes(){
        ((ItemAccessor)Items.SNOWBALL).setMaxStackSize(RangedCombatConfigs.getSnowballMaxStackSize().get());
        CombatNext.LOGGER.info("Changed max stack size of {} to {}", "minecraft:snowball", RangedCombatConfigs.getSnowballMaxStackSize().get());
        ((ItemAccessor)Items.POTION).setMaxStackSize(MagicCombatConfigs.getPotionMaxStackSize().get());
        CombatNext.LOGGER.info("Changed max stack size of {} to {}", "minecraft:potion", MagicCombatConfigs.getPotionMaxStackSize().get());
    }

    public static boolean isEatOrDrink(UseAnim anim){
        return anim == UseAnim.EAT || anim == UseAnim.DRINK;
    }

    public static boolean canInterruptConsumption(DamageSource source){
        return source.getDirectEntity() instanceof LivingEntity || (source.is(DamageTypeTags.IS_PROJECTILE) && source.getEntity() instanceof LivingEntity);
    }

    public static float recalculateDamageBonus(ItemStack stack, float original, Entity target){
        int impalingLevel = stack.getEnchantmentLevel(Enchantments.IMPALING);
        float result = original;
        if(impalingLevel > 0 && GeneralCombatConfigs.getImpalingChange().get()){
            if(target instanceof LivingEntity victim){
                float originalBonus = Enchantments.IMPALING.getDamageBonus(impalingLevel, victim.getMobType(), stack);
                result -= originalBonus;
            }
            result += getImpalingDamageBonus(impalingLevel, target);
        }
        return result;
    }

    public static float getImpalingDamageBonus(int level, Entity target){
        return target.isInWaterOrRain() ? (float)level * IMPALING_DAMAGE_SCALE : 0.0F;
    }

    public static void newDisableShield(Player victim, LivingEntity attacker){
        int cleavingLevel = attacker.getMainHandItem().getEnchantmentLevel(EnchantmentRegistry.CLEAVING.get());
        int cleavingTicks = ShieldCombatConfigs.getShieldDisableTimeCleaving().get() * cleavingLevel;
        victim.getCooldowns().addCooldown(victim.getUseItem().getItem(), ShieldCombatValues.getDisableTimeBase(victim.getUseItem().getItem()).orElse(DEFAULT_SHIELD_DISABLE_TIME) + cleavingTicks);
        victim.stopUsingItem();
        victim.level().broadcastEntityEvent(victim, (byte) SHIELD_BREAK_EVENT_ID);
    }

    public static void attackEmpty(Player player) {
        if(player.isLocalPlayer()){
            ClientCombatUtil.ensureHasSentCarriedItem();
            CNNetwork.SYNC_CHANNEL.sendToServer(ServerboundMissPacket.createMissPacket());
        }
        if (!player.isSpectator()) {
            if(MeleeCombatConfigs.getAttackMissSweepAttack().get()){
                sweepAttack(player);
            }
            resetAttackStrengthTicker(player, true, MeleeCombatConfigs.getAttackMissSweepAttack().get());
        }
    }
    
    public static void resetAttackStrengthTicker(Player player, boolean miss, boolean allowDefault){
        if(miss && MeleeCombatConfigs.getAttackMissReducedCooldown().get()){
            player.resetAttackStrengthTicker();
            PlayerCombat.cast(player).setMissedAttackRecovery(MeleeCombatConfigs.getAttackMissCooldownTicks().get());
        } else if(miss && allowDefault){
            player.resetAttackStrengthTicker();
        }
    }

    private static void sweepAttack(Player player){
        float attackDamage = (float)player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float damageBonus = EnchantmentHelper.getDamageBonus(player.getMainHandItem(), MobType.UNDEFINED);

        float attackStrengthScale = MeleeCombatConfigs.getAttackCooldownImpactOnDamage().get() ? player.getAttackStrengthScale(0.5F) : 1;
        attackDamage *= 0.2F + attackStrengthScale * attackStrengthScale * 0.8F;
        damageBonus *= attackStrengthScale;
        damageBonus = scaleDamageBonus(player, damageBonus);
        if (attackDamage > 0.0F || damageBonus > 0.0F) {
            boolean fullStrength = attackStrengthScale > 0.9F;
            boolean sprintAttack = player.isSprinting() && fullStrength;
            boolean critAttack = fullStrength && player.fallDistance > 0.0F && !player.onGround() && !player.onClimbable() && !player.isInWater() && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger();
            critAttack = critAttack && !player.isSprinting();

            attackDamage += damageBonus;

            boolean sweepAttack = false;
            double walkDistD = player.walkDist - player.walkDistO;
            if (fullStrength && !critAttack && !sprintAttack && player.onGround() && walkDistD < (double)player.getSpeed()) {
                ItemStack itemInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
                sweepAttack = canSweepAttack(itemInHand);
            }

            if(sweepAttack){
                float sweepDamage = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * attackDamage;

                for(LivingEntity sweepTarget : player.level().getEntitiesOfClass(LivingEntity.class, getSweepHitBox(player, player.getItemInHand(InteractionHand.MAIN_HAND)))) {
                    double entityReachSq = Mth.square(player.getEntityReach()); // Use entity reach instead of constant 9.0. Vanilla uses bottom center-to-center checks here, so don't update this to use canReach, since it uses closest-corner checks.
                    if (sweepTarget != player && !player.isAlliedTo(sweepTarget) && (!(sweepTarget instanceof ArmorStand armorStand) || !armorStand.isMarker()) && player.distanceToSqr(sweepTarget) < entityReachSq) { // Original check was dist < 3, range is 3, so vanilla used padding=0
                        sweepTarget.knockback(0.4D, Mth.sin(player.getYRot() * (Mth.PI / 180F)), -Mth.cos(player.getYRot() * Mth.PI / 180F));
                        sweepTarget.hurt(player.damageSources().playerAttack(player), sweepDamage);
                    }
                }

                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
                player.sweepAttack();
            }
        }
    }

    public static boolean canSweepAttack(ItemStack stack) {
        return MeleeCombatConfigs.getSweepAttackChange().get() ?
                stack.getEnchantmentLevel(Enchantments.SWEEPING_EDGE) > 0 : stack.canPerformAction(ToolActions.SWORD_SWEEP);
    }

    private static AABB getSweepHitBox(Player player, ItemStack weapon) {
        double xShift = (-Mth.sin(player.yBodyRot * (Mth.PI / 180F))) * 2.0D;
        double zShift = Mth.cos(player.yBodyRot * (Mth.PI  / 180F)) * 2.0D;
        // we use the player as the "target", so weapons with custom sweep hitboxes work
        return weapon.getSweepHitBox(player, player).move(xShift, 0.0, zShift);
    }

    public static boolean isAttackAvailable(Player player, float partialTick) {
        if (player.getAttackStrengthScale(partialTick) < 1.0F) {
            LivingEntityAccessor livingEntityAccessor = (LivingEntityAccessor) player;
            return PlayerCombat.cast(player).getMissedAttackRecovery() > -1 && livingEntityAccessor.getAttackStrengthTicker() + partialTick > PlayerCombat.cast(player).getMissedAttackRecovery();
        }
        return true;
    }

    public static boolean onAttackCooldown(Player player, float partialTick) {
        if (!isAttackAvailable(player, partialTick) && MeleeCombatConfigs.getAttackDuringCooldownPrevented().get()) {
            if (!MeleeCombatConfigs.getAttackGracePeriod().get() && player.getAttackStrengthScale(partialTick) < 1.0F)
                return true;
            if (player.getAttackStrengthScale(partialTick) < MeleeCombatConfigs.getAttackGracePeriodTime().get()) {
                return true;
            }

            if (player.getAttackStrengthScale(partialTick) < 1.0F) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> MinecraftCombat.cast(Minecraft.getInstance()).setRetainAttack(true));
                return true;
            }
        }
        return false;
    }

    public static AABB adjustBBForRayTrace(AABB boundingBox) {
        if (GeneralCombatConfigs.getHitboxAdjustmentType().get() == HitboxInflationType.CN) {
            if (boundingBox.getSize() < GeneralCombatConfigs.getHitboxMinSizeForHitscan().get()) {
                double xAdjust = adjustSize(boundingBox.getXsize());
                double yAdjust = adjustSize(boundingBox.getYsize());
                double zAdjust = adjustSize(boundingBox.getZsize());
                boundingBox = boundingBox.inflate(xAdjust, yAdjust, zAdjust);
            }
        } else if (GeneralCombatConfigs.getHitboxAdjustmentType().get() == HitboxInflationType.CTS) {
            double d = Math.max(boundingBox.getXsize(), boundingBox.getYsize());
            if (d < GeneralCombatConfigs.getHitboxMinSizeForHitscan().get()) {
                d = (GeneralCombatConfigs.getHitboxMinSizeForHitscan().get() - d) * 0.5;
            }
            boundingBox = boundingBox.inflate(d);
        }
        return boundingBox;
    }

    private static double adjustSize(double size) {
        return size < GeneralCombatConfigs.getHitboxMinSizeForHitscan().get() ? (GeneralCombatConfigs.getHitboxMinSizeForHitscan().get() - size) / 2.0D : 0.0D;
    }

    public static boolean hitThroughBlock(Level level, BlockPos blockPos, Player player, Predicate<Player> hitEntity){
        BlockState clickedBlock = level.getBlockState(blockPos);
        if (!clickedBlock.getCollisionShape(level, blockPos).isEmpty() || clickedBlock.getDestroySpeed(level, blockPos) != 0.0F) {
            return false;
        }
        return hitEntity.test(player);
    }

    public static boolean hitEntity(Player player){
        return getEntityHit(player).isPresent();
    }

    public static Optional<EntityHitResult> getEntityHit(Player player) {
        double blockReach = player.getBlockReach();
        Vec3 from = player.getEyePosition(1.0F);
        double reach;
        double entityReach = player.getEntityReach();
        blockReach = reach = Math.max(blockReach, entityReach); // Pick entities with the max of the reach distance and attack range.

        double reachSqr = Mth.square(reach);

        Vec3 viewVector = player.getViewVector(1.0F);
        Vec3 to = from.add(viewVector.x * blockReach, viewVector.y * blockReach, viewVector.z * blockReach);
        AABB searchBox = player.getBoundingBox().expandTowards(viewVector.scale(blockReach)).inflate(1.0D, 1.0D, 1.0D);
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(player, from, to, searchBox, (e) -> !e.isSpectator() && e.isPickable(), reachSqr);
        if (entityHitResult != null) {
            return Optional.of(entityHitResult).filter(ehr -> {
                Vec3 hitLocation = ehr.getLocation();
                double distanceToSqr = from.distanceToSqr(hitLocation);
                return distanceToSqr <= Mth.square(entityReach) && distanceToSqr <= reachSqr;
            });
        }
        return Optional.empty();
    }

    public static boolean isSprintCritical(Player player, Entity target) {
        boolean fullStrength = player.getAttackStrengthScale(0.5F) > 0.9F;
        fullStrength |= !MeleeCombatConfigs.getAttackCooldownImpactOnDamage().get();
        boolean canSprintCrit = fullStrength
                && player.fallDistance <= 0.0F // can't sprint and fall
                && player.onGround() // can only sprint on ground
                && !player.onClimbable()
                && !player.isInWater()
                && !player.hasEffect(MobEffects.BLINDNESS)
                && !player.isPassenger()
                && target instanceof LivingEntity;
        canSprintCrit = canSprintCrit && player.isSprinting();
        return canSprintCrit;
    }

    public static void handleBonusAttackReach(Player player, boolean add) {
        AttributeInstance instance = player.getAttribute(ForgeMod.ENTITY_REACH.get());
        if (instance != null) {
            AttributeModifier modifier = instance.getModifier(BONUS_REACH_MODIFIER_UUID);
            if(modifier != null){
                if(!add) {
                    instance.removeModifier(BONUS_REACH_MODIFIER_UUID);
                }
            } else if(add){
                instance.addTransientModifier(
                        new AttributeModifier(BONUS_REACH_MODIFIER_UUID, BONUS_REACH_MODIFIER_NAME, MeleeCombatConfigs.getAttackReachBonusWhenSupercharged().get(), AttributeModifier.Operation.ADDITION));
            }
        }
    }

    public static void handleShieldKnockbackResistance(Player player, boolean add) {
        AttributeInstance instance = player.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
        if (instance != null) {
            AttributeModifier modifier = instance.getModifier(SHIELD_KNOCKBACK_RESISTANCE_MODIFIER_UUID);
            if(modifier != null){
                if(!add) {
                    instance.removeModifier(SHIELD_KNOCKBACK_RESISTANCE_MODIFIER_UUID);
                }
            } else if(add){
                ShieldCombatValues.getKnockbackResistance(player.getUseItem().getItem()).ifPresent(
                        knockbackResistance -> instance.addTransientModifier(
                                new AttributeModifier(SHIELD_KNOCKBACK_RESISTANCE_MODIFIER_UUID, SHIELD_KNOCKBACK_RESISTANCE_MODIFIER_NAME, knockbackResistance, AttributeModifier.Operation.ADDITION))
                );
            }
        }
    }

    public static boolean isSupercharged(Player player, float partialTick) {
        return getSuperchargedAttackStrengthScale(player, partialTick) >= MeleeCombatConfigs.getAttackStrengthScaleSuperchargeThreshold().get().floatValue();
    }

    private static float getSuperchargedAttackStrengthScale(Player player, float partialTick) {
        return Mth.clamp(((float) ((LivingEntityAccessor) player).getAttackStrengthTicker() + partialTick) / player.getCurrentItemAttackStrengthDelay(), 0.0F, MeleeCombatConfigs.getAttackStrengthScaleSuperchargeThreshold().get().floatValue());
    }

    public static float scaleDamageBonus(LivingEntity player, float base) {
        AttributeInstance attributeInstance = player.getAttribute(Attributes.ATTACK_DAMAGE);
        float result = base;

        if(attributeInstance != null && MeleeCombatConfigs.getEnchantmentDamageScalesWithModifiers().get()){
            for(AttributeModifier mod : attributeInstance.getModifiers(AttributeModifier.Operation.MULTIPLY_BASE)) {
                result += base * mod.getAmount();
            }

            for(AttributeModifier mod : attributeInstance.getModifiers(AttributeModifier.Operation.MULTIPLY_TOTAL)) {
                result *= 1.0D + mod.getAmount();
            }
        }
        return result;
    }

    public static void applySyncedConfigs() {
        modifyStrengthEffect();
        modifyItemMaxStackSizes();
        registerTridentDispenseBehavior();
    }

    public static void adjustAttributeBaseValue(Player player, Attribute attribute, double newBaseValue) {
        AttributeInstance attributeInstance = player.getAttribute(attribute);
        if(attributeInstance != null) {
            double oldBaseValue = attributeInstance.getBaseValue();
            if (oldBaseValue != newBaseValue) {
                attributeInstance.setBaseValue(newBaseValue);
            }
            CombatNext.LOGGER.info("Set base value for Attribute {} for Player {} to {}, was {}", getTranslation(attribute.getDescriptionId()), player.getName().getString(), newBaseValue, oldBaseValue);
        }
    }

    public static void setAttributeSyncable(Attribute attribute) {
        attribute.setSyncable(true);
        CombatNext.LOGGER.info("Set Attribute {} to client syncable", getTranslation(attribute.getDescriptionId()));
    }

    public static double getDefaultAttributeBaseValue(Attribute attribute) {
        return DefaultAttributes.getSupplier(EntityType.PLAYER).getBaseValue(attribute);
    }

    public static double getBaseAttackRange() {
        double attackRange = getDefaultAttributeBaseValue(ForgeMod.ENTITY_REACH.get());
        attackRange = MeleeCombatConfigs.getPlayerAttackReachBaseChange().get() ? attackRange - 0.5D : attackRange;
        return attackRange;
    }

    public static double getBaseAttackDamage() {
        double attackDamage = getDefaultAttributeBaseValue(Attributes.ATTACK_DAMAGE);
        attackDamage = MeleeCombatConfigs.getPlayerAttackDamageBaseChange().get() ? attackDamage + 1.0D : attackDamage;
        return attackDamage;
    }

    public static boolean isUsingOffhandShield(Player player){
        return player.getUsedItemHand() == InteractionHand.OFF_HAND && isShield(player.getUseItem());
    }

    public static boolean isShield(ItemStack stack) {
        return stack.canPerformAction(ToolActions.SHIELD_BLOCK);
    }

    public static boolean hasOffhandShield(Player player){
        return isShield(player.getItemInHand(InteractionHand.OFF_HAND));
    }

    public static boolean canShieldOnCrouch(Player player){
        return player.onGround();
    }

    public static InteractionHand getShieldHoldingHand(LivingEntity livingEntity) {
        return CombatUtil.isShield(livingEntity.getOffhandItem()) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
    }

    public static Component getShieldModifierTooltip(AttributeModifier.Operation operation, double value, String descriptionId){
        return Component.literal(" ")
                .append(Component.translatable("attribute.modifier.equals." + operation.toValue(),
                        ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(value),
                        Component.translatable(descriptionId)))
                .withStyle(ChatFormatting.DARK_GREEN);
    }

    public static double getBaseAttackSpeed() {
        return getDefaultAttributeBaseValue(Attributes.ATTACK_SPEED);
    }

    public static boolean isAxe(ItemStack stack) {
        return stack.getItem() instanceof AxeItem || stack.is(ItemTags.AXES);
    }

    public static boolean isOnMaxInvulnerableTime(LivingEntity victim) {
        return victim.invulnerableTime == victim.invulnerableDuration;
    }
}
