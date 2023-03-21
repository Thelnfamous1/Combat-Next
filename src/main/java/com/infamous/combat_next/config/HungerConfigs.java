package com.infamous.combat_next.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class HungerConfigs {
    private static ForgeConfigSpec.IntValue naturalHealingMinFoodLevel;
    private static ForgeConfigSpec.IntValue naturalHealingTicksBeforeFoodLevelDecrease;
    private static ForgeConfigSpec.IntValue naturalHealingTicksBeforeHeal;
    private static ForgeConfigSpec.BooleanValue naturalHealingExhaustionPrevented;
    private static ForgeConfigSpec.BooleanValue naturalHealingFastHealingPrevented;

    static void createServerConfigs(ForgeConfigSpec.Builder builder) {
        CNConfig.createConfigCategory(builder, " This category holds configs that affect hunger.", "Hunger Config Options", b -> {
            naturalHealingExhaustionPrevented = b
                    .comment("""
                            Toggles exhaustion not being added during (normal) natural healing.
                            For vanilla, this value is false.
                            """)
                    .define("natural_healing_exhaustion_prevented", true);
            naturalHealingMinFoodLevel = b
                    .comment("""
                            Adjusts the minimum food level (in half-drumsticks) that will allow natural healing.
                            For vanilla, this value is 18.
                            """)
                    .defineInRange("natural_healing_min_food_level", 7, 0, 20);
            naturalHealingFastHealingPrevented = b
                    .comment("""
                            Toggles the prevention of fast natural healing when at maximum food level of 20 (in half-drumsticks).
                            For vanilla, this value is false.
                            """)
                    .define("natural_healing_fast_healing_prevented", true);
            naturalHealingTicksBeforeFoodLevelDecrease = b
                    .comment("""
                            Adjusts the time in ticks (1/20 seconds) before food level is decreased by 1 during natural healing.
                            Note: The "natural_healing_exhaustion_prevented" config value must be set to true.
                            """)
                    .defineInRange("natural_healing_ticks_before_food_level_decrease", 2, 0, 20);
            naturalHealingTicksBeforeHeal = b
                    .comment("""
                            Adjusts the time in ticks (1/20 seconds) between natural healing events.
                            For vanilla, this value is 80.
                            """)
                    .defineInRange("natural_healing_ticks_before_heal", 40, 0, 600);
        });
    }

    public static ForgeConfigSpec.IntValue getNaturalHealingMinFoodLevel() {
        return naturalHealingMinFoodLevel;
    }

    public static ForgeConfigSpec.IntValue getNaturalHealingTicksBeforeFoodLevelDecrease() {
        return naturalHealingTicksBeforeFoodLevelDecrease;
    }

    public static ForgeConfigSpec.IntValue getNaturalHealingTicksBeforeHeal() {
        return naturalHealingTicksBeforeHeal;
    }

    public static ForgeConfigSpec.BooleanValue getNaturalHealingExhaustionPrevented() {
        return naturalHealingExhaustionPrevented;
    }

    public static ForgeConfigSpec.BooleanValue getNaturalHealingFastHealingPrevented() {
        return naturalHealingFastHealingPrevented;
    }
}
