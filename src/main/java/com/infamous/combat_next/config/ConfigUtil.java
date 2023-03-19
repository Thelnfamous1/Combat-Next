package com.infamous.combat_next.config;

public class ConfigUtil {
    static final double ARROW_INSTANT_EFFECT_SCALE = 0.375D;
    static final double ATTACK_BONUS_REACH_WHEN_SUPERCHARGED = 1.0D;
    static final int ATTACK_MISS_COOLDOWN_TICKS = 4;
    static final float ATTACK_SCALE_SUPERCHARGE_THRESHOLD = 2.0F;
    static final float BOW_ARROW_INACCURACY = 0.25F;
    static final int BOW_TICKS_BEFORE_OVERDRAWN = 60;
    static final int DRINK_USE_DURATION_TICKS = 20;
    static final double HITBOX_MIN_SIZE_FOR_HITSCAN = 0.9D;
    static final int INSTANT_EFFECT_BASE_HEAL_AMOUNT = 6;
    static final int NATURAL_HEALING_MIN_FOOD_LEVEL = 7;
    static final int NATURAL_HEALING_TICKS_BEFORE_FOOD_LEVEL_DECREASE = 2;
    static final int NATURAL_HEALING_TICKS_BEFORE_HEAL = 40;
    static final int POTION_MAX_STACK_SIZE = 16;
    static final float SHIELD_KNOCKBACK_SCALE = 0.5F;
    static final float SHIELD_MAX_BLOCKED_DAMAGE = 5.0F;
    static final float SHIELD_PROTECTION_ARC = 100.0F;
    static final int SHIELD_DISABLE_TICKS_BASE = 32;
    static final int SHIELD_DISABLE_TICKS_CLEAVING = 10;
    static final int SNOWBALL_MAX_STACK_SIZE = 64;
    static final double STRENGTH_EFFECT_MODIFIER_VALUE = 0.2D;
    static final float SWEEPING_DAMAGE_SCALE = 0.5F;
    static final int THROWABLE_ITEM_COOLDOWN = 4;

    public static double getStrengthEffectModifierValue() {
        return CNConfig.strengthEffectModifierValue.get();
    }

    public static int getShieldDisableTicksBase() {
        return CNConfig.shieldDisableTicksBase.get();
    }

    public static int getShieldDisableTicksCleaving() {
        return CNConfig.shieldDisableTicksCleaving.get();
    }

    public static float getShieldProtectionArc() {
        return CNConfig.shieldProtectionArc.get().floatValue();
    }

    public static int getInstantEffectBaseHealAmount() {
        return CNConfig.instantEffectBaseHealAmount.get();
    }

    public static int getSnowballMaxStackSize() {
        return CNConfig.snowballMaxStackSize.get();
    }

    public static int getPotionMaxStackSize() {
        return CNConfig.potionMaxStackSize.get();
    }

    public static int getDrinkUseDurationTicks() {
        return CNConfig.drinkUseDurationTicks.get();
    }

    public static int getNaturalHealingMinFoodLevel() {
        return CNConfig.naturalHealingMinFoodLevel.get();
    }

    public static int getNaturalHealingTicksBeforeHeal() {
        return CNConfig.naturalHealingTicksBeforeHeal.get();
    }

    public static int getNaturalHealingTicksBeforeFoodLevelDecrease() {
        return CNConfig.naturalHealingTicksBeforeFoodLevelDecrease.get();
    }

    public static double getHitboxMinSizeForHitscan() {
        return CNConfig.hitboxMinSizeForHitscan.get();
    }

    public static int getBowTicksBeforeOverdrawn() {
        return CNConfig.bowTicksBeforeOverdrawn.get();
    }

    public static float getBowArrowInaccuracy() {
        return CNConfig.bowArrowInaccuracy.get().floatValue();
    }

    public static int getAttackMissCooldownTicks() {
        return CNConfig.attackMissCooldownTicks.get();
    }

    public static double getArrowInstantEffectScale() {
        return CNConfig.arrowInstantEffectScale.get();
    }

    public static float getShieldMaxBlockedDamage() {
        return CNConfig.shieldMaxBlockedDamage.get().floatValue();
    }

    public static int getThrowableItemCooldown() {
        return CNConfig.throwableItemCooldown.get();
    }

    public static float getShieldKnockbackScale() {
        return CNConfig.shieldKnockbackScale.get().floatValue();
    }

    public static float getAttackScaleSuperchargeThreshold() {
        return CNConfig.attackScaleSuperchargeThreshold.get().floatValue();
    }

    public static double getAttackBonusReachWhenSupercharged() {
        return CNConfig.attackBonusReachWhenSupercharged.get();
    }

    public static float getSweepingDamageScale() {
        return CNConfig.sweepingDamageScale.get().floatValue();
    }
}
