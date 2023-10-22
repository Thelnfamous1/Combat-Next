package com.infamous.combat_next.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Consumer;

public class CNConfig {
    //public static final ForgeConfigSpec COMMON_SPEC;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        //COMMON_SPEC = createConfig(CNConfig::setupCommonConfig);
        CLIENT_SPEC = createConfig(CNConfig::setupClientConfig);
        SERVER_SPEC = createConfig(CNConfig::setupServerConfig);
    }

    private static void setupClientConfig(ForgeConfigSpec.Builder builder) {
        ShieldCombatConfigs.createClientConfigs(builder);
        AnimCombatConfigs.createClientConfigs(builder);
    }

    private static void setupServerConfig(ForgeConfigSpec.Builder builder) {
        BugFixConfigs.createServerConfigs(builder);
        GeneralCombatConfigs.createServerConfigs(builder);
        HungerConfigs.createServerConfigs(builder);
        MagicCombatConfigs.createServerConfigs(builder);
        MeleeCombatConfigs.createServerConfigs(builder);
        RangedCombatConfigs.createServerConfigs(builder);
        ShieldCombatConfigs.createServerConfigs(builder);
    }

    private static ForgeConfigSpec createConfig(Consumer<ForgeConfigSpec.Builder> setup) {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setup.accept(configBuilder);
        return configBuilder.build();
    }
    static void createConfigCategory(ForgeConfigSpec.Builder builder, String comment, String path, Consumer<ForgeConfigSpec.Builder> definitions) {
        builder.comment(comment).push(path);
        definitions.accept(builder);
        builder.pop();
    }
}