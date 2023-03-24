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
public class MeleeCombatValues {

    private static Map<Item, Double> ATTACK_DAMAGE_VALUES = ImmutableMap.of();
    private static Map<Item, Double> ATTACK_SPEED_VALUES = ImmutableMap.of();
    private static Map<Item, Double> ATTACK_RANGE_VALUES = ImmutableMap.of();

    public static Optional<Double> getAttackDamage(Item item){
        return Optional.ofNullable(ATTACK_DAMAGE_VALUES.get(item));
    }

    public static Optional<Double> getAttackSpeed(Item item){
        return Optional.ofNullable(ATTACK_SPEED_VALUES.get(item));
    }

    public static Optional<Double> getAttackRange(Item item){
        return Optional.ofNullable(ATTACK_RANGE_VALUES.get(item));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading configEvent) {
        if(configEvent.getConfig().getSpec() == CNConfig.SERVER_SPEC && CNConfig.SERVER_SPEC.isLoaded()){
            CombatNext.LOGGER.info("Creating " + MeleeCombatValues.class.getSimpleName() + " caches!");
            buildCaches();
            CombatNext.LOGGER.info("Finished making " + MeleeCombatValues.class.getSimpleName() + " caches!");
            WeaponRebalancing.clearWeaponRebalancingCache();
        }
    }

    private static void buildCaches() {
        List<? extends String> attackDamages = MeleeCombatConfigs.getWeaponAttackDamageEntries().get();
        ATTACK_DAMAGE_VALUES = ConfigUtil.createMapFromConfigList(attackDamages, "Attack Damage", Double::parseDouble,
                MeleeCombatValues::isValidAttackDamage, "must be greater than or equal to 0.0"
        );
        List<? extends String> attackSpeeds = MeleeCombatConfigs.getWeaponAttackSpeedEntries().get();
        ATTACK_SPEED_VALUES = ConfigUtil.createMapFromConfigList(attackSpeeds, "Attack Speed", Double::parseDouble,
                MeleeCombatValues::isValidAttackSpeed, "must be greater than or equal to 0.0"
        );
        List<? extends String> attackRanges = MeleeCombatConfigs.getWeaponAttackRangeEntries().get();
        ATTACK_RANGE_VALUES = ConfigUtil.createMapFromConfigList(attackRanges, "Attack Range", Double::parseDouble,
                MeleeCombatValues::isValidAttackRange, "must be greater than or equal to 0.0"
        );
    }

    private static boolean isValidAttackDamage(Double attackDamage) {
        return attackDamage >= 0.0D;
    }

    private static boolean isValidAttackSpeed(Double attackSpeed) {
        return attackSpeed >= 0.0D;
    }

    private static boolean isValidAttackRange(Double attackRange) {
        return attackRange >= 0.0D;
    }

    @SubscribeEvent
    static void onFileChange(final ModConfigEvent.Reloading configEvent) {
        if(configEvent.getConfig().getSpec() == CNConfig.SERVER_SPEC && CNConfig.SERVER_SPEC.isLoaded()){
            CombatNext.LOGGER.info("Refreshing " + MeleeCombatValues.class.getSimpleName() + " caches!");
            buildCaches();
            CombatNext.LOGGER.info("Finished making " + MeleeCombatValues.class.getSimpleName() + " caches!");
            WeaponRebalancing.clearWeaponRebalancingCache();
        }
    }
}
