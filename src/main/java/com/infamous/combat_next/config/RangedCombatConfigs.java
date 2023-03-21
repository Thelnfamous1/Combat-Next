package com.infamous.combat_next.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class RangedCombatConfigs {
    private static ForgeConfigSpec.DoubleValue bowOverdrawnArrowInaccuracy;
    private static ForgeConfigSpec.IntValue bowTicksBeforeOverdrawn;
    private static ForgeConfigSpec.IntValue snowballMaxStackSize;
    private static ForgeConfigSpec.BooleanValue projectileNoIFrames;
    private static ForgeConfigSpec.BooleanValue bowOverdrawing;
    private static ForgeConfigSpec.BooleanValue tridentLoyaltyReturnFromVoid;
    private static ForgeConfigSpec.BooleanValue tridentShootFromDispenser;
    private static ForgeConfigSpec.IntValue throwableItemCooldown;

    static void createServerConfigs(ForgeConfigSpec.Builder builder) {
        CNConfig.createConfigCategory(builder, " This category holds configs that affect ranged combat.", "Ranged Combat Config Options", b -> {
            bowOverdrawing = b
                    .comment("""
                            Toggles bows being capable of becoming "overdrawn", preventing crits and causing inaccuracy.
                            For vanilla, this value is false.
                            """)
                    .define("bow_overdrawing", true);
            bowOverdrawnArrowInaccuracy = b
                    .comment("""
                            Adjusts the inaccuracy factor for an arrow shot by an overdrawn bow.
                            Note: The "bow_overdrawing" config value must be set to true.
                            Note: In vanilla, an inaccuracy factor of 1.0 is applied to all arrows shot from bows.
                            """)
                    .defineInRange("bow_overdrawn_arrow_inaccuracy", 0.25F, 0.0F, 100.0F);
            bowTicksBeforeOverdrawn = b
                    .comment("""
                            Adjusts the amount of ticks (1/20 seconds) a bow can be charged before it is "overdrawn", preventing crits and causing inaccuracy.
                            Note: The "bow_overdrawing" config value must be set to true.
                            """)
                    .defineInRange("bow_ticks_before_overdrawn", 60, 0, 200);
            projectileNoIFrames = b
                    .comment("""
                            Toggles projectiles no longer giving the target i-frames (invulnerable frames).
                            For vanilla, this value is false.
                            """)
                    .define("projectile_no_i_frames", true);
            throwableItemCooldown = b
                    .comment("""
                            Adjusts the amount of ticks (1/20 seconds) a throwable item (eggs, snowballs, etc) is put on cooldown for after being thrown.
                            For vanilla, this value is 0 (no cooldown).
                            """)
                    .defineInRange("throwable_item_cooldown", 4, 0, 20);
            tridentLoyaltyReturnFromVoid = b
                    .comment("""
                            Toggles tridents enchanted with Loyalty being able to return from the void.
                            For vanilla, this value is false.
                            """)
                    .define("trident_loyalty_return_from_void", true);
            tridentShootFromDispenser = b
                    .comment("""
                            Toggles vanilla tridents being able to be shot out of Dispensers.
                            For vanilla, this value is false.
                            """)
                    .define("trident_shoot_from_dispenser", true);
            snowballMaxStackSize = b
                    .comment("""
                            Adjusts the max number of snowballs that can be held in a single stack.
                            For vanilla, this value is 64.
                            """)
                    .defineInRange("snowball_max_stack_size", 64, 1, 64);
        });
    }

    public static ForgeConfigSpec.DoubleValue getBowOverdrawnArrowInaccuracy() {
        return bowOverdrawnArrowInaccuracy;
    }

    public static ForgeConfigSpec.IntValue getBowTicksBeforeOverdrawn() {
        return bowTicksBeforeOverdrawn;
    }

    public static ForgeConfigSpec.IntValue getSnowballMaxStackSize() {
        return snowballMaxStackSize;
    }

    public static ForgeConfigSpec.BooleanValue getProjectileNoIFrames() {
        return projectileNoIFrames;
    }

    public static ForgeConfigSpec.BooleanValue getBowOverdrawing() {
        return bowOverdrawing;
    }

    public static ForgeConfigSpec.BooleanValue getTridentLoyaltyReturnFromVoid() {
        return tridentLoyaltyReturnFromVoid;
    }

    public static ForgeConfigSpec.BooleanValue getTridentShootFromDispenser() {
        return tridentShootFromDispenser;
    }

    public static ForgeConfigSpec.IntValue getThrowableItemCooldown() {
        return throwableItemCooldown;
    }
}
