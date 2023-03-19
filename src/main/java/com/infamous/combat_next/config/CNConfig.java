package com.infamous.combat_next.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Consumer;

public class CNConfig {
    //public static final ForgeConfigSpec COMMON_SPEC;
    //public static final ForgeConfigSpec CLIENT_SPEC;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        //COMMON_SPEC = createConfig(CNConfig::setupCommonConfig);
        //CLIENT_SPEC = createConfig(CNConfig::setupClientConfig);
        SERVER_SPEC = createConfig(CNConfig::setupServerConfig);
    }

    public static ForgeConfigSpec.DoubleValue arrowInstantEffectScale;
    public static ForgeConfigSpec.DoubleValue attackBonusReachWhenSupercharged;
    public static ForgeConfigSpec.IntValue attackMissCooldownTicks;
    public static ForgeConfigSpec.DoubleValue attackStrengthScaleSuperchargeThreshold;
    public static ForgeConfigSpec.DoubleValue bowArrowInaccuracy;
    public static ForgeConfigSpec.IntValue bowTicksBeforeOverdrawn;
    public static ForgeConfigSpec.IntValue drinkUseDurationTicks;
    public static ForgeConfigSpec.DoubleValue hitboxMinSizeForHitscan;
    public static ForgeConfigSpec.IntValue instantEffectBaseHealAmount;
    public static ForgeConfigSpec.IntValue naturalHealingMinFoodLevel;
    public static ForgeConfigSpec.IntValue naturalHealingTicksBeforeFoodLevelDecrease;
    public static ForgeConfigSpec.IntValue naturalHealingTicksBeforeHeal;
    public static ForgeConfigSpec.IntValue potionMaxStackSize;
    public static ForgeConfigSpec.DoubleValue shieldKnockbackScale;
    public static ForgeConfigSpec.DoubleValue shieldMaxBlockedDamage;
    public static ForgeConfigSpec.DoubleValue shieldProtectionArc;
    public static ForgeConfigSpec.IntValue shieldDisableTicksBase;
    public static ForgeConfigSpec.IntValue shieldDisableTicksCleaving;
    public static ForgeConfigSpec.IntValue snowballMaxStackSize;
    public static ForgeConfigSpec.DoubleValue strengthEffectModifierValue;
    public static ForgeConfigSpec.DoubleValue sweepingDamageScale;
    public static ForgeConfigSpec.IntValue throwableItemCooldown;
    private static void setupServerConfig(ForgeConfigSpec.Builder builder) {
        createConfigCategory(builder, " This category holds configs that affect general combat.", "General Combat Config Options", b -> {
            drinkUseDurationTicks = b
                    .comment("""
                            Adjusts the time in ticks (1/20 seconds) it takes to consume a drink when used.
                            In vanilla, this value is 32 for stews, milk buckets and potions, and 40 for honey bottles.
                            """)
                    .defineInRange("drink_use_duration_ticks", ConfigUtil.DRINK_USE_DURATION_TICKS, 0, 40);
            hitboxMinSizeForHitscan = b
                    .comment("""
                            Adjusts the minimum size an entity's hitbox can be, in blocks, for hitscan detection.
                            An entity's hitbox with a size smaller than this value will be inflated to it during the hitscan.
                            """)
                    .defineInRange("hitbox_min_size_for_hitscan", ConfigUtil.HITBOX_MIN_SIZE_FOR_HITSCAN, 0.0D, 2.0D);
        });
        createConfigCategory(builder, " This category holds configs that affect hunger.", "Hunger Config Options", b -> {
            naturalHealingMinFoodLevel = b
                    .comment("""
                            Adjusts the minimum food level (in half-drumsticks) that will allow natural healing.
                            In vanilla, this value is 18.
                            """)
                    .defineInRange("natural_healing_min_food_level", ConfigUtil.NATURAL_HEALING_MIN_FOOD_LEVEL, 0, 20);
            naturalHealingTicksBeforeFoodLevelDecrease = b
                    .comment("""
                            Adjusts the time in ticks (1/20 seconds) before food level is decreased by 1 during natural healing.
                            In vanilla, this value is 0.
                            """)
                    .defineInRange("natural_healing_ticks_before_food_level_decrease", ConfigUtil.NATURAL_HEALING_TICKS_BEFORE_FOOD_LEVEL_DECREASE, 0, 20);
            naturalHealingTicksBeforeHeal = b
                    .comment("""
                            Adjusts the time in ticks (1/20 seconds) between natural healing events.
                            In vanilla, this value is 80.
                            """)
                    .defineInRange("natural_healing_ticks_before_heal", ConfigUtil.NATURAL_HEALING_TICKS_BEFORE_HEAL, 0, 600);
        });
        createConfigCategory(builder, " This category holds configs that affect magic combat.", "Magic Combat Config Options", b -> {
            arrowInstantEffectScale = b
                    .comment("""
                            Adjusts the amount an instant effect is scaled by when applied from a tipped arrow.
                            In vanilla, non-instant effects are scaled by 1/8 (0.375).
                            """)
                    .defineInRange("arrow_instant_effect_scale", ConfigUtil.ARROW_INSTANT_EFFECT_SCALE, 0.0D, 1.0D);
            instantEffectBaseHealAmount = b
                    .comment("""
                            Adjusts the base amount of health healed (in half-hearts) by instant effects (Instant Health, Instant Damage) before amplification.
                            In vanilla, this value is 4.
                            """)
                    .defineInRange("instant_effect_base_heal_amount", ConfigUtil.INSTANT_EFFECT_BASE_HEAL_AMOUNT, 1, 20);
            potionMaxStackSize = b
                    .comment("""
                            Adjusts the max number of potions (excluding splash and lingering ones) that can be held in a single stack.
                            In vanilla, this value is 1.
                            """)
                    .defineInRange("potion_max_stack_size", ConfigUtil.POTION_MAX_STACK_SIZE, 1, 64);
            strengthEffectModifierValue = b
                    .comment("""
                            Adjusts the multiplier of the attack damage attribute modifier the Strength effect applies, per level of Strength.
                            Note: A value of "0.2", for example, means total attack damage will be increased by 20%.
                            """)
                    .defineInRange("strength_effect_modifier_value", ConfigUtil.STRENGTH_EFFECT_MODIFIER_VALUE, 0.0D, 1.0D);
        });
        createConfigCategory(builder, " This category holds configs that affect melee combat.", "Melee Combat Config Options", b -> {
            attackBonusReachWhenSupercharged = b
                    .comment("""
                            Adjusts the bonus attack reach given when the held weapon is "supercharged".
                            """)
                    .defineInRange("attack_bonus_reach_when_supercharged", ConfigUtil.ATTACK_BONUS_REACH_WHEN_SUPERCHARGED, 0.0D, 3.0D);
            attackMissCooldownTicks = b
                    .comment("""
                            Adjusts the attack cooldown ticks (1/20 seconds) given when missing an attack.
                            """)
                    .defineInRange("attack_miss_cooldown_ticks", ConfigUtil.ATTACK_MISS_COOLDOWN_TICKS, 0, 100);
            attackStrengthScaleSuperchargeThreshold = b
                    .comment("""
                            Adjusts the threshold at which the player's attack strength scale is considered "supercharged".
                            """)
                    .defineInRange("attack_strength_scale_supercharge_threshold", ConfigUtil.ATTACK_STRENGTH_SCALE_SUPERCHARGE_THRESHOLD, 1.0D, 4.0D);
            sweepingDamageScale = b
                    .comment("""
                            Adjusts how much sweeping damage is scaled by during a sweep attack.
                            In vanilla, this value is 1.0 (unscaled).
                            """)
                    .defineInRange("sweeping_damage_scale", ConfigUtil.SWEEPING_DAMAGE_SCALE, 0.0F, 1.0F);
        });
        
        createConfigCategory(builder, " This category holds configs that affect ranged combat.", "Ranged Combat Config Options", b -> {
            bowArrowInaccuracy = b
                    .comment("""
                            Adjusts the inaccuracy factor for an arrow shot by an overdrawn bow.
                            In vanilla, this value is 1.0.
                            """)
                    .defineInRange("bow_arrow_inaccuracy", ConfigUtil.BOW_ARROW_INACCURACY, 0.0F, 100.0F);
            bowTicksBeforeOverdrawn = b
                    .comment("""
                            Adjusts the amount of ticks (1/20 seconds) a bow can be charged before it is "overdrawn", preventing crits and causing inaccuracy.
                            """)
                    .defineInRange("bow_ticks_before_overdrawn", ConfigUtil.BOW_TICKS_BEFORE_OVERDRAWN, 0, 200);
            throwableItemCooldown = b
                    .comment("""
                            Adjusts the amount of ticks (1/20 seconds) a throwable item (eggs, snowballs, etc) is put on cooldown for after being thrown.
                            In vanilla, this value is 0 (no cooldown).
                            """)
                    .defineInRange("throwable_item_cooldown", ConfigUtil.THROWABLE_ITEM_COOLDOWN, 0, 20);
            snowballMaxStackSize = b
                    .comment("""
                            Adjusts the max number of snowballs that can be held in a single stack.
                            In vanilla, this value is 64.
                            """)
                    .defineInRange("snowball_max_stack_size", ConfigUtil.SNOWBALL_MAX_STACK_SIZE, 1, 64);
        });
        
        createConfigCategory(builder, " This category holds configs that affect shield combat.", "Shield Combat Config Options", b -> {
            shieldKnockbackScale = b
                    .comment("""
                            Adjusts how much received knockback is scaled by when blocking with the vanilla shield.
                            """)
                    .defineInRange("shield_knockback_scale", ConfigUtil.SHIELD_KNOCKBACK_SCALE, 0.0F, 1.0F);
            shieldMaxBlockedDamage = b
                    .comment("""
                            Adjusts the maximum damage (in half-hearts) a vanilla shield will absorb when blocking.
                            """)
                    .defineInRange("shield_max_blocked_damage", ConfigUtil.SHIELD_MAX_BLOCKED_DAMAGE, 0.0F, 1024.0F);
            shieldProtectionArc = b
                    .comment("""
                            Adjusts the arc of protection, in degrees, given when blocking with the vanilla shield.
                            In vanilla, this value is 180.0.
                            """)
                    .defineInRange("shield_protection_arc", ConfigUtil.SHIELD_PROTECTION_ARC, 0.0F, 180.0F);
            shieldDisableTicksBase = b
                    .comment("""
                            Adjusts the base amount of ticks (1/20 seconds) a shield is disabled for.
                            """)
                    .defineInRange("shield_disable_ticks_base", ConfigUtil.SHIELD_DISABLE_TICKS_BASE, 0, 600);
            shieldDisableTicksCleaving = b
                    .comment("""
                            Adjusts the additional amount of ticks (1/20 seconds) a shield is disabled for when hit by an axe, per level of Cleaving.
                            """)
                    .defineInRange("shield_disable_ticks_cleaving", ConfigUtil.SHIELD_DISABLE_TICKS_CLEAVING, 0, 20);
        });
    }

    private static ForgeConfigSpec createConfig(Consumer<ForgeConfigSpec.Builder> setup) {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setup.accept(configBuilder);
        return configBuilder.build();
    }
    private static void createConfigCategory(ForgeConfigSpec.Builder builder, String comment, String path, Consumer<ForgeConfigSpec.Builder> definitions) {
        builder.comment(comment).push(path);
        definitions.accept(builder);
        builder.pop();
    }
}