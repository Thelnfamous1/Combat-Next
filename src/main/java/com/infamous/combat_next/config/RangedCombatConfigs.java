package com.infamous.combat_next.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class RangedCombatConfigs {
    private static ForgeConfigSpec.DoubleValue bowOverdrawnArrowInaccuracyDefault;
    private static ForgeConfigSpec.DoubleValue bowOverdrawnArrowInaccuracyMax;
    private static ForgeConfigSpec.IntValue bowTicksBeforeOverdrawn;
    private static ForgeConfigSpec.IntValue bowTicksUntilMaxOverdraw;
    private static ForgeConfigSpec.IntValue snowballMaxStackSize;
    private static ForgeConfigSpec.BooleanValue bowOverdrawing;
    private static ForgeConfigSpec.BooleanValue tridentLoyaltyReturnFromVoid;
    private static ForgeConfigSpec.BooleanValue tridentShootFromDispenser;
    private static ForgeConfigSpec.IntValue throwableItemCooldown;
    private static ForgeConfigSpec.IntValue projectileIFrames;

    static void createServerConfigs(ForgeConfigSpec.Builder builder) {
        CNConfig.createConfigCategory(builder, " This category holds configs that affect ranged combat.", "Ranged Combat Config Options", b -> {
            bowOverdrawing = b
                    .comment("""
                            Toggles bows being capable of becoming "overdrawn", preventing crits and causing inaccuracy.
                            For vanilla, this value is false.
                            """)
                    .define("bow_overdrawing", true);
            bowOverdrawnArrowInaccuracyDefault = b
                    .comment("""
                            Adjusts the minimum inaccuracy factor for an arrow shot by an overdrawn bow.
                            Note: The "bow_overdrawing" config value must be set to true.
                            Note: In vanilla, an inaccuracy factor of 1.0 is applied to all arrows shot from bows.
                            """)
                    .defineInRange("bow_overdrawn_arrow_inaccuracy_default", 0.25F, 0.0F, 1.0F);
            bowOverdrawnArrowInaccuracyMax = b
                    .comment("""
                            Adjusts the maximum inaccuracy multiplier for an arrow shot by an overdrawn bow.
                            Note: The "bow_overdrawing" config value must be set to true.
                            """)
                    .defineInRange("bow_overdrawn_arrow_inaccuracy_max", 10.5F, 0.0F, 100.0F);
            bowTicksUntilMaxOverdraw = b
                    .comment("""
                            Adjusts the amount of ticks (1/20 seconds) a bow can be charged before reaches maximum fatigue, preventing crits and providing the maximum inaccuracy.
                            Note: The "bow_overdrawing" config value must be set to true.
                            """)
                    .defineInRange("bow_ticks_before_overdrawn", 200, 0, 1000);
            bowTicksBeforeOverdrawn = b
                    .comment("""
                            Adjusts the amount of ticks (1/20 seconds) a bow can be charged before it is fatigue, preventing crits and causing inaccuracy.
                            Note: The "bow_overdrawing" config value must be set to true.
                            """)
                    .defineInRange("bow_ticks_before_overdrawn", 60, 0, 200);
            projectileIFrames = b
                    .comment("""
                            Toggles the number of i-frames (invulnerable frames) given to a target when being struck by a projectile.
                            Setting the value to 0 allows for things like Multishot arrows being able to damage the same target.
                            For vanilla, this value is 20.
                            """)
                    .defineInRange("projectile_i_frames", 0, 0, 20);
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

    public static ForgeConfigSpec.DoubleValue getBowOverdrawnArrowInaccuracyDefault() {
        return bowOverdrawnArrowInaccuracyDefault;
    }

    public static ForgeConfigSpec.DoubleValue getBowOverdrawnArrowInaccuracyMax() {
        return bowOverdrawnArrowInaccuracyMax;
    }

    public static ForgeConfigSpec.IntValue getBowTicksUntilMaxOverdraw() {
        return bowTicksUntilMaxOverdraw;
    }

    public static ForgeConfigSpec.IntValue getBowTicksBeforeOverdrawn() {
        return bowTicksBeforeOverdrawn;
    }

    public static ForgeConfigSpec.IntValue getSnowballMaxStackSize() {
        return snowballMaxStackSize;
    }

    public static ForgeConfigSpec.IntValue getProjectileIFrames() {
        return projectileIFrames;
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
