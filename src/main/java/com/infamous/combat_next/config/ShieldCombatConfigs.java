package com.infamous.combat_next.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ShieldCombatConfigs {
    private static ForgeConfigSpec.DoubleValue shieldKnockbackScale;
    private static ForgeConfigSpec.DoubleValue shieldMaxBlockedDamage;
    private static ForgeConfigSpec.DoubleValue shieldProtectionArc;
    private static ForgeConfigSpec.BooleanValue shieldDisableChange;
    private static ForgeConfigSpec.IntValue shieldDisableTicksBase;
    private static ForgeConfigSpec.IntValue shieldDisableTicksCleaving;
    private static ForgeConfigSpec.IntValue shieldWarmUpDelay;
    private static ForgeConfigSpec.BooleanValue shieldReduceDamageBlocked;
    private static ForgeConfigSpec.BooleanValue shieldGoatRamFullKnockback;
    private static ForgeConfigSpec.BooleanValue shieldCrouch;
    private static ForgeConfigSpec.IntValue shieldIndicatorStatus;
    private static ForgeConfigSpec.IntValue shieldIndicatorCrosshairOffsetX;
    private static ForgeConfigSpec.IntValue shieldIndicatorCrosshairOffsetY;
    private static ForgeConfigSpec.IntValue shieldIndicatorHotbarRightOffsetX;
    private static ForgeConfigSpec.IntValue shieldIndicatorHotbarLeftOffsetX;
    private static ForgeConfigSpec.IntValue shieldIndicatorHotbarOffsetY;

    static void createServerConfigs(ForgeConfigSpec.Builder builder) {
        CNConfig.createConfigCategory(builder, " This category holds configs that affect shield combat.", "Shield Combat Config Options", b -> {
            shieldDisableChange = b
                    .comment("""
                            Toggles the vanilla shield disable logic being changed to utilize the Cleaving enchantment.
                            For vanilla, this value is false.
                            """)
                    .define("shield_disable_change", true);
            shieldDisableTicksBase = b
                    .comment("""
                            Adjusts the base amount of ticks (1/20 seconds) a shield is disabled for.
                            Note: The "shield_disable_change" config value must be set to true.
                            """)
                    .defineInRange("shield_disable_ticks_base", 32, 0, 600);
            shieldDisableTicksCleaving = b
                    .comment("""
                            Adjusts the additional amount of ticks (1/20 seconds) a shield is disabled for when hit by an axe, per level of Cleaving.
                            Note: The "shield_disable_change" config value must be set to true.
                            """)
                    .defineInRange("shield_disable_ticks_cleaving", 10, 0, 20);
            shieldGoatRamFullKnockback = b
                    .comment("""
                            Toggles ramming Goats applying full knockback to a target, regardless of whether or not they are shielded.
                            Note: If false, Goat ram knockback is reduced by 50% if shielded, and stacks with the "shield_knockback_scale" config value.
                            For vanilla, this value is false.
                            """)
                    .define("shield_goat_ram_full_knockback", true);
            shieldKnockbackScale = b
                    .comment("""
                            Adjusts how much received knockback is scaled by when blocking with the vanilla shield.
                            For vanilla,this value is 1.0.
                            """)
                    .defineInRange("shield_knockback_scale", 0.5F, 0.0F, 1.0F);
            shieldMaxBlockedDamage = b
                    .comment("""
                            Adjusts the maximum damage (in half-hearts) a vanilla shield will absorb when blocking.
                            Note: The "shield_reduce_damage_blocked" config value must be set to true.
                            """)
                    .defineInRange("shield_max_blocked_damage", 5.0F, 0.0F, 1024.0F);
            shieldProtectionArc = b
                    .comment("""
                            Adjusts the arc of protection, in degrees, given when blocking with the vanilla shield.
                            For vanilla, this value is 180.0.
                            """)
                    .defineInRange("shield_protection_arc", 100.0F, 0.0F, 180.0F);
            shieldReduceDamageBlocked = b
                    .comment("""
                            Toggles the vanilla shield no longer blocking all non-projectile and non-explosive damage.
                            Instead, it will block, at most, the "shield_max_blocked_damage" config value.
                            For vanilla, this value is false.
                            """)
                    .define("shield_reduce_damage_blocked", true);
            shieldWarmUpDelay = b
                    .comment("""
                            Adjusts the amount of ticks (1/20 seconds) the shield must be active before being able to block attacks.
                            For vanilla, this value is 5.
                            """)
                    .defineInRange("shield_warmup_delay", 0, 0, 200);
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

    public static ForgeConfigSpec.DoubleValue getShieldKnockbackScale() {
        return shieldKnockbackScale;
    }

    public static ForgeConfigSpec.DoubleValue getShieldMaxBlockedDamage() {
        return shieldMaxBlockedDamage;
    }

    public static ForgeConfigSpec.DoubleValue getShieldProtectionArc() {
        return shieldProtectionArc;
    }

    public static ForgeConfigSpec.IntValue getShieldDisableTicksCleaving() {
        return shieldDisableTicksCleaving;
    }

    public static ForgeConfigSpec.IntValue getShieldWarmUpDelay() {
        return shieldWarmUpDelay;
    }

    public static ForgeConfigSpec.BooleanValue getShieldReduceDamageBlocked() {
        return shieldReduceDamageBlocked;
    }

    public static ForgeConfigSpec.BooleanValue getShieldDisableChange() {
        return shieldDisableChange;
    }

    public static ForgeConfigSpec.IntValue getShieldDisableTicksBase() {
        return shieldDisableTicksBase;
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
}
