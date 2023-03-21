package com.infamous.combat_next.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BugFixConfigs {
    public static ForgeConfigSpec.BooleanValue fixShieldAttackerKnockback;
    public static ForgeConfigSpec.BooleanValue fixShieldUserKnockback;

    static void createServerConfigs(ForgeConfigSpec.Builder builder) {
        CNConfig.createConfigCategory(builder, " This category holds configs that affect bug fixes.", "Bug Fix Config Options", b -> {
            fixShieldAttackerKnockback = b
                    .comment("""
                            Fix MC-147694 (shield not knocking back attackers).
                            For vanilla, this value is false.
                            """)
                    .define("fix_shield_attacker_knockback", true);
            fixShieldUserKnockback = b
                    .comment("""
                            Fix MC-223238 and MC-248310 (shield users blocking all knockback).
                            For vanilla, this value is false.
                            """)
                    .define("fix_shield_user_knockback", true);

        });
    }
}
