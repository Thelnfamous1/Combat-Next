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
}
