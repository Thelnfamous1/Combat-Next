package com.infamous.combat_next.config;

import com.google.common.collect.ImmutableMap;
import com.infamous.combat_next.CombatNext;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = CombatNext.MODID)
public class ShieldCombatValues {
    private static Map<Item, Integer> SHIELD_STRENGTH_VALUES = ImmutableMap.of();
    private static Map<Item, Float> KNOCKBACK_RESISTANCE_VALUES = ImmutableMap.of();
    private static Map<Item, Float> PROTECTION_ARC_VALUES = ImmutableMap.of();
    private static Map<Item, Integer> WARM_UP_DELAY_VALUES = ImmutableMap.of();
    private static Map<Item, Integer> DISABLE_TIME_BASE_VALUES = ImmutableMap.of();

    public static Optional<Integer> getShieldStrength(Item item){
        return Optional.ofNullable(SHIELD_STRENGTH_VALUES.get(item));
    }

    public static Optional<Float> getKnockbackResistance(Item item){
        return Optional.ofNullable(KNOCKBACK_RESISTANCE_VALUES.get(item));
    }

    public static Optional<Float> getProtectionArc(Item item){
        return Optional.ofNullable(PROTECTION_ARC_VALUES.get(item));
    }
    public static Optional<Integer> getWarmUpDelay(Item item){
        return Optional.ofNullable(WARM_UP_DELAY_VALUES.get(item));
    }
    public static Optional<Integer> getDisableTimeBase(Item item){
        return Optional.ofNullable(DISABLE_TIME_BASE_VALUES.get(item));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading configEvent) {
        if(configEvent.getConfig().getSpec() == CNConfig.SERVER_SPEC && CNConfig.SERVER_SPEC.isLoaded()){
            CombatNext.LOGGER.info("Creating " + ShieldCombatValues.class.getSimpleName() + " caches!");
            buildCaches();
            CombatNext.LOGGER.info("Finished making " + ShieldCombatValues.class.getSimpleName() + " caches!");
        }
    }

    private static void buildCaches() {
        List<? extends String> shieldStrengthEntries = ShieldCombatConfigs.getShieldShieldStrengthEntries().get();
        SHIELD_STRENGTH_VALUES = ConfigUtil.createMapFromConfigList(shieldStrengthEntries, "Shield Strength", Integer::parseInt,
                ShieldCombatValues::isValidShieldStrength, "must be greater than or equal to 0"
        );
        List<? extends String> shieldKnockbackResistanceEntries = ShieldCombatConfigs.getShieldKnockbackResistanceEntries().get();
        KNOCKBACK_RESISTANCE_VALUES = ConfigUtil.createMapFromConfigList(shieldKnockbackResistanceEntries, "Knockback Resistance", Float::parseFloat,
                ShieldCombatValues::isValidKnockbackResistance, "must be between 0.0 and 1.0"
        );
        List<? extends String> shieldProtectionArcEntries = ShieldCombatConfigs.getShieldProtectionArcEntries().get();
        PROTECTION_ARC_VALUES = ConfigUtil.createMapFromConfigList(shieldProtectionArcEntries, "Protection Arc", Float::parseFloat,
                ShieldCombatValues::isValidShieldArc, "must be between 0.0 and 180.0"
        );
        List<? extends String> shieldWarmUpDelayEntries = ShieldCombatConfigs.getShieldWarmUpDelayEntries().get();
        WARM_UP_DELAY_VALUES = ConfigUtil.createMapFromConfigList(shieldWarmUpDelayEntries, "Warm-Up Delay", Integer::parseInt,
                ShieldCombatValues::isValidWarmUpDelay, "must be greater than or equal to  0"
        );
        List<? extends String> shieldDisableTimeBaseEntries = ShieldCombatConfigs.getShieldDisableTimeBaseEntries().get();
        DISABLE_TIME_BASE_VALUES = ConfigUtil.createMapFromConfigList(shieldDisableTimeBaseEntries, "Disable Time Base", Integer::parseInt,
                ShieldCombatValues::isValidDisableBase, "must be greater than or equal to  0"
        );
    }

    private static boolean isValidShieldStrength(Integer shieldStrength) {
        return shieldStrength >= 0;
    }

    private static boolean isValidKnockbackResistance(Float knockbackResistance) {
        return knockbackResistance >= 0.0F && knockbackResistance <= 1.0F;
    }

    private static boolean isValidShieldArc(Float protectionArc) {
        return protectionArc >= 0.0F && protectionArc <= 180.0F;
    }

    private static boolean isValidWarmUpDelay(Integer warmUpDelay) {
        return warmUpDelay >= 0;
    }

    private static boolean isValidDisableBase(Integer disableTimeBase) {
        return disableTimeBase >= 0;
    }

    @SubscribeEvent
    static void onFileChange(final ModConfigEvent.Reloading configEvent) {
        if(configEvent.getConfig().getSpec() == CNConfig.SERVER_SPEC && CNConfig.SERVER_SPEC.isLoaded()){
            CombatNext.LOGGER.info("Refreshing " + ShieldCombatValues.class.getSimpleName() + " caches!");
            buildCaches();
            CombatNext.LOGGER.info("Finished making " + ShieldCombatValues.class.getSimpleName() + " caches!");
        }
    }
}
