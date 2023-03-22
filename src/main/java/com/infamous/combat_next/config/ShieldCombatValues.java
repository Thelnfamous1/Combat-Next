package com.infamous.combat_next.config;

import com.infamous.combat_next.CombatNext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = CombatNext.MODID)
public class ShieldCombatValues {
    private static final Map<Item, Integer> SHIELD_STRENGTH_VALUES = new HashMap<>();
    private static final Map<Item, Float> KNOCKBACK_RESISTANCE_VALUES = new HashMap<>();
    private static final Map<Item, Float> PROTECTION_ARC_VALUES = new HashMap<>();

    public static Optional<Integer> getShieldStrength(ItemStack stack){
        return Optional.ofNullable(SHIELD_STRENGTH_VALUES.get(stack.getItem()));
    }

    public static Optional<Float> getKnockbackResistance(ItemStack stack){
        return Optional.ofNullable(KNOCKBACK_RESISTANCE_VALUES.get(stack.getItem()));
    }

    public static Optional<Float> getProtectionArc(ItemStack stack){
        return Optional.ofNullable(PROTECTION_ARC_VALUES.get(stack.getItem()));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading configEvent) {
        if(configEvent.getConfig().getSpec() == CNConfig.SERVER_SPEC && CNConfig.SERVER_SPEC.isLoaded()){
            CombatNext.LOGGER.info("Creating ShieldCombatValues caches!");
            buildCaches();
        }
    }

    private static void buildCaches() {
        List<? extends String> shieldStrengthEntries = ShieldCombatConfigs.getShieldShieldStrengthEntries().get();
        processEntries(shieldStrengthEntries, SHIELD_STRENGTH_VALUES, "Shield Strength", Integer::parseInt,
                ShieldCombatValues::isValidShieldStrength, "must be greater than 0"
        );
        List<? extends String> shieldKnockbackResistanceEntries = ShieldCombatConfigs.getShieldKnockbackResistanceEntries().get();
        processEntries(shieldKnockbackResistanceEntries, KNOCKBACK_RESISTANCE_VALUES, "Knockback Resistance", Float::parseFloat,
                ShieldCombatValues::isValidKnockbackResistance, "must be between 0.0 and 1.0"
        );
        List<? extends String> shieldProtectionArcEntries = ShieldCombatConfigs.getShieldProtectionArcEntries().get();
        processEntries(shieldProtectionArcEntries, PROTECTION_ARC_VALUES, "Protection Arc", Float::parseFloat,
                ShieldCombatValues::isValidShieldArc, "must be between 0.0 and 180.0"
        );
    }

    private static boolean isValidShieldStrength(Integer shieldStrength) {
        return shieldStrength > 0;
    }

    private static boolean isValidKnockbackResistance(Float knockbackResistance) {
        return knockbackResistance >= 0.0F && knockbackResistance <= 1.0F;
    }

    private static boolean isValidShieldArc(Float protectionArc) {
        return protectionArc >= 0.0F && protectionArc <= 180.0F;
    }

    private static <V extends Number> void processEntries(List<? extends String> entryStrings, Map<Item, V> cache, String cacheName, Function<String, V> valueParser, Predicate<V> valueValidator, String invalidValueMsg) {
        for(String entryString : entryStrings){
            String[] entry = entryString.split("#");
            if(entry.length != 2){
                CombatNext.LOGGER.error("Invalid formatting for {} entry {}, must be a namespaced-id and a value separated by #!", cacheName, entryString);
                continue;
            }
            ResourceLocation shieldId = ResourceLocation.tryParse(entry[0]);
            if(shieldId == null){
                CombatNext.LOGGER.error("Invalid formatting for {} entry {}, key {} must be a namespaced-id!", cacheName, entryString, entry[0]);
                continue;
            }
            Item item;
            if(ForgeRegistries.ITEMS.containsKey(shieldId)){
                item = ForgeRegistries.ITEMS.getValue(shieldId);
            } else{
                CombatNext.LOGGER.error("Invalid formatting for {} entry {}, key {} must be a registered Item!", cacheName, entryString, entry[0]);
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
            }
            cache.put(item, value);
            CombatNext.LOGGER.info("Entered {} into {} cache with value {}!", shieldId, cacheName, value);
        }
        CombatNext.LOGGER.info("Created Shield {} cache!", cacheName);
    }

    @SubscribeEvent
    static void onFileChange(final ModConfigEvent.Reloading configEvent) {
        if(configEvent.getConfig().getSpec() == CNConfig.SERVER_SPEC && CNConfig.SERVER_SPEC.isLoaded()){
            CombatNext.LOGGER.info("Refreshing ShieldCombatValues caches!");
            buildCaches();
        }
    }
}
