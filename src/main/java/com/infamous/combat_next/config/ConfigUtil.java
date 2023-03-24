package com.infamous.combat_next.config;

import com.google.common.collect.ImmutableMap;
import com.infamous.combat_next.CombatNext;
import com.infamous.combat_next.util.TieredWeaponType;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ConfigUtil {
    static final ResourceLocation TRIDENT_LOCATION = new ResourceLocation("trident");
    static final double TRIDENT_ATTACK_DAMAGE = 7.0D;
    static final double TRIDENT_ATTACK_SPEED = 2.0D;
    static final double TRIDENT_ATTACK_RANGE = 3.5D;

    public static final Map<ResourceLocation, Double> DEFAULT_ATTACK_DAMAGE_VALUES = Util.make(new HashMap<>(), (map) -> {
        Arrays.stream(TieredWeaponType.values())
                .forEach(weaponType -> insertTieredWeaponEntries(map, weaponType, TieredWeaponType::getAttackDamage));
        map.put(TRIDENT_LOCATION, TRIDENT_ATTACK_DAMAGE);
    });

    public static final Map<ResourceLocation, Double> DEFAULT_ATTACK_SPEED_VALUES = Util.make(new HashMap<>(), (map) -> {
        Arrays.stream(TieredWeaponType.values())
                .forEach(weaponType -> insertTieredWeaponEntries(map, weaponType, TieredWeaponType::getAttackSpeed));
        map.put(TRIDENT_LOCATION, TRIDENT_ATTACK_SPEED);
    });
    public static final Map<ResourceLocation, Double> DEFAULT_ATTACK_RANGE_VALUES = Util.make(new HashMap<>(), (map) -> {
        Arrays.stream(TieredWeaponType.values())
                .forEach(weaponType -> insertTieredWeaponEntries(map, weaponType, TieredWeaponType::getAttackRange));
        map.put(TRIDENT_LOCATION, TRIDENT_ATTACK_RANGE);
    });

    static <K, V> List<String> getMapAsStringList(Map<K, V> map){
        return map.entrySet().stream().map(e -> e.getKey().toString() + "#" + e.getValue().toString()).collect(Collectors.toList());
    }

    private static void insertTieredWeaponEntries(Map<ResourceLocation, Double> map, TieredWeaponType weaponType, TieredWeaponType.ValueGetter valueGetter){
        map.put(new ResourceLocation(String.format("%s_%s", "wooden", weaponType.getName())), valueGetter.apply(weaponType, Tiers.WOOD));
        map.put(new ResourceLocation(String.format("%s_%s", "stone", weaponType.getName())), valueGetter.apply(weaponType, Tiers.STONE));
        map.put(new ResourceLocation(String.format("%s_%s", "iron", weaponType.getName())), valueGetter.apply(weaponType, Tiers.IRON));
        map.put(new ResourceLocation(String.format("%s_%s", "diamond", weaponType.getName())), valueGetter.apply(weaponType, Tiers.DIAMOND));
        map.put(new ResourceLocation(String.format("%s_%s", "golden", weaponType.getName())), valueGetter.apply(weaponType, Tiers.GOLD));
        map.put(new ResourceLocation(String.format("%s_%s", "netherite", weaponType.getName())), valueGetter.apply(weaponType, Tiers.NETHERITE));
    }

    static <V> Map<Item, V> createMapFromConfigList(List<? extends String> entryStrings, String cacheName, Function<String, V> valueParser, Predicate<V> valueValidator, String invalidValueMsg) {
        Map<Item, V> tempMap = new LinkedHashMap<>(entryStrings.size());
        for(String entryString : entryStrings){
            String[] entry = entryString.split("#");
            if(entry.length != 2){
                CombatNext.LOGGER.error("Invalid formatting for {} entry {}, must be a namespaced-id and a value separated by #!", cacheName, entryString);
                continue;
            }
            ResourceLocation itemId = ResourceLocation.tryParse(entry[0]);
            if(itemId == null){
                CombatNext.LOGGER.error("Invalid formatting for {} entry {}, key {} must be a namespaced-id!", cacheName, entryString, entry[0]);
                continue;
            }
            Item item;
            if(ForgeRegistries.ITEMS.containsKey(itemId)){
                item = ForgeRegistries.ITEMS.getValue(itemId);
            } else{
                CombatNext.LOGGER.error("Invalid formatting for {} entry {}, key {} must be a registered Item!", cacheName, entryString, itemId);
                continue;
            }
            V value;
            try{
                value = valueParser.apply(entry[1]);
            } catch (NumberFormatException e){
                CombatNext.LOGGER.error("Invalid formatting for {} entry {}, invalid value {}!", cacheName, entryString, entry[1]);
                continue;
            }
            if(!valueValidator.test(value)){
                CombatNext.LOGGER.error("Invalid formatting for {} entry {}, value {} {}!", cacheName, entryString, entry[1], invalidValueMsg);
                continue;
            }
            V prevValue = tempMap.put(item, value);
            CombatNext.LOGGER.info("Entered key {} into {} cache with value {}!", itemId, cacheName, value);
            if(prevValue != null){
                CombatNext.LOGGER.info("    Previous value of {} mapped to {} key {} was replaced with {}", prevValue, cacheName, itemId, value);
            }
        }
        CombatNext.LOGGER.info("Created {} cache with {} entries!", cacheName, tempMap.size());
        return ImmutableMap.copyOf(tempMap);
    }
}
