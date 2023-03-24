package com.infamous.combat_next.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ShieldCombatConfigs {
    private static ForgeConfigSpec.BooleanValue shieldReduceKnockback;
    private static ForgeConfigSpec.BooleanValue shieldDisableChange;
    private static ForgeConfigSpec.IntValue shieldDisableTimeCleaving;
    private static ForgeConfigSpec.BooleanValue shieldWarmUpDelayChange;
    private static ForgeConfigSpec.BooleanValue shieldReduceDamageBlocked;
    private static ForgeConfigSpec.BooleanValue shieldGoatRamFullKnockback;
    private static ForgeConfigSpec.BooleanValue shieldCrouch;
    private static ForgeConfigSpec.IntValue shieldIndicatorStatus;
    private static ForgeConfigSpec.IntValue shieldIndicatorCrosshairOffsetX;
    private static ForgeConfigSpec.IntValue shieldIndicatorCrosshairOffsetY;
    private static ForgeConfigSpec.IntValue shieldIndicatorHotbarRightOffsetX;
    private static ForgeConfigSpec.IntValue shieldIndicatorHotbarLeftOffsetX;
    private static ForgeConfigSpec.IntValue shieldIndicatorHotbarOffsetY;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> shieldShieldStrengthEntries;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> shieldKnockbackResistanceEntries;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> shieldProtectionArcEntries;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> shieldWarmUpDelayEntries;
    private static ForgeConfigSpec.ConfigValue<List<? extends String>> shieldDisableTimeBaseEntries;
    private static ForgeConfigSpec.BooleanValue shieldProtectionArcChange;
    private static ForgeConfigSpec.BooleanValue shieldAttackWhileCrouchShielding;

    static void createServerConfigs(ForgeConfigSpec.Builder builder) {
        CNConfig.createConfigCategory(builder, " This category holds configs that affect shield combat.", "Shield Combat Config Options", b -> {
            shieldAttackWhileCrouchShielding = b
                    .comment("""
                            Toggles being able to attack while crouch-shielding.
                            For vanilla, this value is false.
                            """)
                    .define("shield_attack_while_crouch_shielding", true);
            shieldDisableChange = b
                    .comment("""
                            Toggles the vanilla shield disable logic being changed to utilize the Cleaving enchantment.
                            If true, the base number of ticks (1/20 seconds) a shield will be disabled for will be equal to their disable time base value obtained from the "shield_disable_time_base_entries" config value.
                            An additional number of ticks (1/20 seconds) will be added by multiplying the "shield_disable_time_cleaving" config value by the attacking weapon's Cleaving level.
                            For vanilla, this value is false.
                            """)
                    .define("shield_disable_change", true);
            shieldDisableTimeBaseEntries = b
                    .comment("""
                            A list of shield item ids mapped to their corresponding disable time base value, in ticks (1/20 seconds).
                            Format each entry as a namespaced id (ex. for the Shield, "minecraft:shield"), follow by a "#", follow by a non-negative integer (ex. 32).
                            Note: Make sure you are surrounding each entry with quotation (") marks, and separate each entry by a comma (,).
                            """)
                    .defineList("shield_disable_time_base_entries", List.of("minecraft:shield#32"), entry -> entry instanceof String);
            shieldDisableTimeCleaving = b
                    .comment("""
                            Adjusts the additional amount of ticks (1/20 seconds) a shield is disabled for when hit by an axe, per level of Cleaving.
                            Note: The "shield_disable_change" config value must be set to true.
                            """)
                    .defineInRange("shield_disable_time_cleaving", 10, 0, 20);
            shieldGoatRamFullKnockback = b
                    .comment("""
                            Toggles ramming Goats applying full knockback to a target, regardless of whether or not they are shielded.
                            Note: If false, Goat ram knockback is reduced by 50% if shielded, and stacks with the "shield_knockback_scale" config value.
                            For vanilla, this value is false.
                            """)
                    .define("shield_goat_ram_full_knockback", true);
            shieldKnockbackResistanceEntries = b
                    .comment("""
                            A list of shield item ids mapped to their corresponding Knockback Resistance value.
                            Format each entry as a namespaced id (ex. for the Shield, "minecraft:shield"), follow by a "#", follow by a decimal number between 0.0 and 1.0 (ex. 0.5).
                            Note: Make sure you are surrounding each entry with quotation (") marks, and separate each entry by a comma (,).
                            """)
                    .defineList("shield_knockback_resistance_entries", List.of("minecraft:shield#0.5"), entry -> entry instanceof String);
            shieldProtectionArcChange = b
                    .comment("""
                            Toggles shields having a custom arc of protection.
                            If true, the arc of protection they provide will be their Protection Arc value obtained from the "shield_protection_arc_entries" config value.
                            If false, all shields will have an arc of protection of 180 degrees.
                            For vanilla, this value is false.
                            """)
                    .define("shield_protection_arc_change", true);
            shieldProtectionArcEntries = b
                    .comment("""
                            A list of shield item ids mapped to their corresponding arc of protection value, in degrees.
                            Format each entry as a namespaced id (ex. for the Shield, "minecraft:shield"), follow by a "#", follow by a decimal number between 0.0 and 180.0 (ex. 100.0).
                            Note: Make sure you are surrounding each entry with quotation (") marks, and separate each entry by a comma (,).
                            In vanilla, the Shield's arc of protection is 180 degrees.
                            """)
                    .defineList("shield_protection_arc_entries", List.of("minecraft:shield#100.0"), entry -> entry instanceof String);
            shieldReduceDamageBlocked = b
                    .comment("""
                            Toggles shields no longer blocking all non-projectile and non-explosive damage.
                            Instead, the max damage they can block will be their Shield Strength value obtained from the "shield_shield_strength_entries" config value.
                            For vanilla, this value is false.
                            """)
                    .define("shield_reduce_damage_blocked", true);
            shieldReduceKnockback = b
                    .comment("""
                            Toggles shields reducing received knockback.
                            If true, they will reduce received knockback by their Knockback Resistance value obtained from the "shield_knockback_resistance_entries" config value.
                            For vanilla, this value is false.
                            """)
                    .define("shield_reduce_knockback", true);
            shieldShieldStrengthEntries = b
                    .comment("""
                            A list of shield item ids mapped to their corresponding Shield Strength value, in half-hearts.
                            Format each entry as a namespaced id (ex. for the Shield, "minecraft:shield"), follow by a "#", follow by a non-negative integer (ex. 5).
                            Note: Make sure you are surrounding each entry with quotation (") marks, and separate each entry by a comma (,).
                            """)
                    .defineList("shield_shield_strength_entries", List.of("minecraft:shield#5"), entry -> entry instanceof String);
            shieldWarmUpDelayChange = b
                    .comment("""
                            Toggles shields having a custom warm up delay, in ticks (1/20 seconds).
                            if true, the time it takes for a shield to fully activate will be their warm up delay value obtained from the "shield_warm_up_delay_entries" config value.
                            For vanilla, this value is false.
                            """)
                    .define("shield_warmup_delay_change", true);
            shieldWarmUpDelayEntries = b
                    .comment("""
                            A list of shield item ids mapped to their corresponding warm up delay value, in ticks (1/20 seconds) .
                            Format each entry as a namespaced id (ex. for the Shield, "minecraft:shield"), follow by a "#", follow by a non-negative integer (ex. 0).
                            Note: Make sure you are surrounding each entry with quotation (") marks, and separate each entry by a comma (,).
                            """)
                    .defineList("shield_warm_up_delay_entries", List.of("minecraft:shield#0"), entry -> entry instanceof String);
        });
    }

    static void createClientConfigs(ForgeConfigSpec.Builder builder) {
        CNConfig.createConfigCategory(builder, " This category holds configs that affect shield combat.", "Shield Combat Config Options", b -> {
            shieldCrouch = b
                    .comment("""
                            Toggles crouching causing activation of any shield present in the offhand.
                            Note: Must be on solid ground in order to do so.
                            For vanilla, this value is false.
                            """)
                    .define("shield_crouch", true);
            shieldIndicatorStatus = b
                    .comment("""
                            Adjusts the status of the Shield Indicator.
                            A value of 0 means the Shield Indicator will be turned off.
                            A value of 1 means the Shield Indicator will render with the Crosshair.
                            A value of 2 means the Shield Indicator will render with the Hotbar.
                            For vanilla, this value is 0.
                            """)
                    .defineInRange("shield_indicator_status", 1, 0, 2);
            shieldIndicatorCrosshairOffsetX = b
                    .comment("""
                            Adjusts the vertical offset of the Crosshair Shield Indicator, in pixels.
                            This will be subtracted from 1/2 of the screen's width.
                            Higher values will move the Crosshair Shield Indicator left on the screen.
                            For vanilla, this value is 8 (1/2 the Crosshair Shield Indicator's texture width of 16).
                            """)
                    .defineInRange("shield_indicator_crosshair_offset_x", 8, -8000, 8000);
            shieldIndicatorCrosshairOffsetY = b
                    .comment("""
                            Adjusts the vertical offset of the Crosshair Shield Indicator, in pixels.
                            This will be added to 1/2 of the screen's height.
                            Higher values will move the Crosshair Shield Indicator down on the screen.
                            For vanilla, this value is 9 (The Crosshair Shield Indicator's texture height of 16 - 1/2 the Crosshair's texture height of 15).
                            """)
                    .defineInRange("shield_indicator_crosshair_offset_y", 9, -8000, 8000);
            shieldIndicatorHotbarRightOffsetX = b
                    .comment("""
                            Adjusts the horizontal offset of the Hotbar Shield Indicator when the Main Hand is Right, in pixels.
                            This will be added to 1/2 of the screen's width.
                            Higher values will move the Hotbar Shield Indicator right on the screen.
                            For vanilla, this value is 115 (1/2 the Hotbar's texture width of 192 + 1/2 the Hotbar Attack Indicator's texture width of 18 + 6).
                            """)
                    .defineInRange("shield_indicator_hotbar_right_offset_x", 115, -8000, 8000);
            shieldIndicatorHotbarLeftOffsetX = b
                    .comment("""
                            Adjusts the horizontal offset of the Hotbar Shield Indicator when the Main Hand is Left, in pixels.
                            This will be subtracted from 1/2 of the screen's width.
                            Higher values will move the Hotbar Shield Indicator left on the screen.
                            For vanilla, this value is 131 (1/2 the Hotbar's texture width of 192 + 1/2 the Hotbar Attack Indicator's texture width of 18 + 22).
                            """)
                    .defineInRange("shield_indicator_hotbar_left_offset_x", 131, -8000, 8000);
            shieldIndicatorHotbarOffsetY = b
                    .comment("""
                            Adjusts the vertical offset of the Hotbar Shield Indicator, in pixels.
                            This will be subtracted from the screen's height.
                            Higher values will move the Hotbar Shield Indicator up on the screen.
                            For vanilla, this value is 20 (The Hotbar Shield Indicator's texture width of 18 + 2).
                            """)
                    .defineInRange("shield_indicator_hotbar_offset_y", 20, 20, 8000);
        });
    }

    public static ForgeConfigSpec.BooleanValue getShieldGoatRamFullKnockback() {
        return shieldGoatRamFullKnockback;
    }

    public static ForgeConfigSpec.IntValue getShieldDisableTimeCleaving() {
        return shieldDisableTimeCleaving;
    }

    public static ForgeConfigSpec.BooleanValue getShieldWarmUpDelayChange() {
        return shieldWarmUpDelayChange;
    }

    public static ForgeConfigSpec.BooleanValue getShieldReduceDamageBlocked() {
        return shieldReduceDamageBlocked;
    }

    public static ForgeConfigSpec.BooleanValue getShieldDisableChange() {
        return shieldDisableChange;
    }

    public static ForgeConfigSpec.BooleanValue getShieldCrouch() {
        return shieldCrouch;
    }

    public static ForgeConfigSpec.IntValue getShieldIndicatorStatus() {
        return shieldIndicatorStatus;
    }

    public static ForgeConfigSpec.IntValue getShieldIndicatorCrosshairOffsetX() {
        return shieldIndicatorCrosshairOffsetX;
    }

    public static ForgeConfigSpec.IntValue getShieldIndicatorCrosshairOffsetY() {
        return shieldIndicatorCrosshairOffsetY;
    }

    public static ForgeConfigSpec.IntValue getShieldIndicatorHotbarRightOffsetX() {
        return shieldIndicatorHotbarRightOffsetX;
    }

    public static ForgeConfigSpec.IntValue getShieldIndicatorHotbarLeftOffsetX() {
        return shieldIndicatorHotbarLeftOffsetX;
    }

    public static ForgeConfigSpec.IntValue getShieldIndicatorHotbarOffsetY() {
        return shieldIndicatorHotbarOffsetY;
    }

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> getShieldShieldStrengthEntries() {
        return shieldShieldStrengthEntries;
    }

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> getShieldKnockbackResistanceEntries() {
        return shieldKnockbackResistanceEntries;
    }

    public static ForgeConfigSpec.BooleanValue getShieldReduceKnockback() {
        return shieldReduceKnockback;
    }

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> getShieldProtectionArcEntries() {
        return shieldProtectionArcEntries;
    }

    public static ForgeConfigSpec.BooleanValue getShieldProtectionArcChange() {
        return shieldProtectionArcChange;
    }

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> getShieldDisableTimeBaseEntries() {
        return shieldDisableTimeBaseEntries;
    }

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> getShieldWarmUpDelayEntries() {
        return shieldWarmUpDelayEntries;
    }

    public static ForgeConfigSpec.BooleanValue getShieldAttackWhileCrouchShielding() {
        return shieldAttackWhileCrouchShielding;
    }
}
