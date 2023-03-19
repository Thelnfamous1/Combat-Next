package com.infamous.combat_next.data;

import com.infamous.combat_next.CombatNext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CNTags {

    public static final TagKey<Item> FAST_DRINKS = ItemTags.create(new ResourceLocation(CombatNext.MODID, "fast_drinks"));

    public static final TagKey<Item> SLOW_THROWABLES = ItemTags.create(new ResourceLocation(CombatNext.MODID, "slow_throwables"));
}
