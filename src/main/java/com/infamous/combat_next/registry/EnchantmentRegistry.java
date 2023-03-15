package com.infamous.combat_next.registry;

import com.infamous.combat_next.CombatNext;
import com.infamous.combat_next.enchantment.CleavingEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentRegistry {

    public static DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, CombatNext.MODID);

    public static RegistryObject<Enchantment> CLEAVING = ENCHANTMENTS.register("cleaving", () ->
            new CleavingEnchantment(Enchantment.Rarity.COMMON, CleavingEnchantment.AXE, EquipmentSlot.MAINHAND));
}
