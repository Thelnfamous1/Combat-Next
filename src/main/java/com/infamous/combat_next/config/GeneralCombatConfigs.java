package com.infamous.combat_next.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class GeneralCombatConfigs {
    private static ForgeConfigSpec.IntValue drinkUseDurationTicks;
    private static ForgeConfigSpec.DoubleValue hitboxMinSizeForHitscan;
    private static ForgeConfigSpec.BooleanValue attacksInterruptConsumption;
    private static ForgeConfigSpec.BooleanValue impalingChange;
    private static ForgeConfigSpec.IntValue iFramesLeftBeforeDamageable;
    private static ForgeConfigSpec.BooleanValue playersAlwaysHurt;

    static void createServerConfigs(ForgeConfigSpec.Builder builder) {
        CNConfig.createConfigCategory(builder, " This category holds configs that affect general combat.", "General Combat Config Options", b -> {
            attacksInterruptConsumption = b
                    .comment("""
                            Toggles melee and ranged attacks from players or mobs interrupting eating and drinking.
                            For vanilla, this value is false.
                            """)
                    .define("attacks_interrupt_consumption", true);
            drinkUseDurationTicks = b
                    .comment("""
                            Adjusts the time in ticks (1/20 seconds) it takes to consume a drink when used.
                            For vanilla, this value is 32 for stews, milk buckets and potions, and 40 for honey bottles.
                            """)
                    .defineInRange("drink_use_duration_ticks", 20, 0, 40);
            iFramesLeftBeforeDamageable = b
                    .comment("""
                            Adjust the number of i-frames (invulnerable frames) left before full damage can be taken.
                            Hitting a target when the number of i-frames is greater than this will only result in partial or zero damage.
                            For vanilla, this value is 10.
                            """)
                    .defineInRange("i_frames_left_before_damageable", 10, 0, 20);
            impalingChange = b
                    .comment("""
                            Toggles the change to the Impaling enchantment to allow it to grant bonus damage against a target in water or rain.
                            For vanilla, this value is false.
                            """)
                    .define("impaling_change", true);
            hitboxMinSizeForHitscan = b
                    .comment("""
                            Adjusts the minimum size an entity's hitbox can be, in blocks, for hitscan detection.
                            An entity's hitbox with a size smaller than this value will be inflated to it during the hitscan.
                            For vanilla, this value is 0.0.
                            """)
                    .defineInRange("hitbox_min_size_for_hitscan", 0.9D, 0.0D, 2.0D);
            playersAlwaysHurt = b
                    .comment("""
                            Toggles Players triggering hurt logic even when received damage is zero.
                            For vanilla, this value is false.
                            """)
                    .define("players_always_hurt", true);
        });
    }

    public static ForgeConfigSpec.IntValue getDrinkUseDurationTicks() {
        return drinkUseDurationTicks;
    }

    public static ForgeConfigSpec.DoubleValue getHitboxMinSizeForHitscan() {
        return hitboxMinSizeForHitscan;
    }

    public static ForgeConfigSpec.BooleanValue getAttacksInterruptConsumption() {
        return attacksInterruptConsumption;
    }

    public static ForgeConfigSpec.BooleanValue getImpalingChange() {
        return impalingChange;
    }

    public static ForgeConfigSpec.IntValue getIFramesLeftBeforeDamageable() {
        return iFramesLeftBeforeDamageable;
    }

    public static ForgeConfigSpec.BooleanValue getPlayersAlwaysHurt() {
        return playersAlwaysHurt;
    }
}
