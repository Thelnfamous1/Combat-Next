package com.infamous.combat_next.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class MagicCombatConfigs {
    private static ForgeConfigSpec.DoubleValue arrowInstantEffectScale;
    private static ForgeConfigSpec.IntValue instantEffectBaseHealAmount;
    private static ForgeConfigSpec.IntValue potionMaxStackSize;
    private static ForgeConfigSpec.DoubleValue strengthEffectModifierValue;
    private static ForgeConfigSpec.BooleanValue strengthEffectChange;

    static void createConfigs(ForgeConfigSpec.Builder builder) {
        CNConfig.createConfigCategory(builder, " This category holds configs that affect magic combat.", "Magic Combat Config Options", b -> {
            arrowInstantEffectScale = b
                    .comment("""
                            Adjusts the amount an instant effect is scaled by when applied from a tipped arrow.
                            For vanilla, non-instant effects are scaled by 1/8 (0.375).
                            """)
                    .defineInRange("arrow_instant_effect_scale", 0.375D, 0.0D, 1.0D);
            instantEffectBaseHealAmount = b
                    .comment("""
                            Adjusts the base amount of health healed (in half-hearts) by instant effects (Instant Health, Instant Damage) before amplification.
                            For vanilla, this value is 4.
                            """)
                    .defineInRange("instant_effect_base_heal_amount", 6, 1, 20);
            potionMaxStackSize = b
                    .comment("""
                            Adjusts the max number of potions (excluding splash and lingering ones) that can be held in a single stack.
                            For vanilla, this value is 1.
                            """)
                    .defineInRange("potion_max_stack_size", 16, 1, 64);
            strengthEffectChange = b
                    .comment("""
                            Toggles the change of the Strength effect's modifier from an additive one to a multiplicative one.
                            For vanilla, this value is false.
                            """)
                    .define("strength_effect_change_enabled", true);
            strengthEffectModifierValue = b
                    .comment("""
                            Adjusts the multiplier of the attack damage attribute modifier the Strength effect applies, per level of Strength.
                            Note: The "strength_effect_change_enabled" config value must be set to true.
                            Note: A value of "0.2", for example, means total attack damage will be increased by 20%.
                            """)
                    .defineInRange("strength_effect_modifier_value", 0.2D, 0.0D, 1.0D);
        });
    }

    public static ForgeConfigSpec.DoubleValue getArrowInstantEffectScale() {
        return arrowInstantEffectScale;
    }

    public static ForgeConfigSpec.IntValue getInstantEffectBaseHealAmount() {
        return instantEffectBaseHealAmount;
    }

    public static ForgeConfigSpec.IntValue getPotionMaxStackSize() {
        return potionMaxStackSize;
    }

    public static ForgeConfigSpec.DoubleValue getStrengthEffectModifierValue() {
        return strengthEffectModifierValue;
    }

    public static ForgeConfigSpec.BooleanValue getStrengthEffectChange() {
        return strengthEffectChange;
    }
}
