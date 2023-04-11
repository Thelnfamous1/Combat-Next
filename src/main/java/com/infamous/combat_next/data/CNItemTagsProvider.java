package com.infamous.combat_next.data;

import com.infamous.combat_next.CombatNext;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CNItemTagsProvider extends ItemTagsProvider {
    public CNItemTagsProvider(DataGenerator generator, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), lookupProvider, blockTagsProvider.contentsGetter(), CombatNext.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(CNTags.FAST_DRINKS)
                .add(Items.BEETROOT_SOUP)
                .add(Items.MUSHROOM_STEW)
                .add(Items.RABBIT_STEW)
                .add(Items.SUSPICIOUS_STEW)
                .add(Items.POTION)
                .add(Items.MILK_BUCKET)
                .add(Items.HONEY_BOTTLE);
        this.tag(CNTags.SLOW_THROWABLES)
                .add(Items.EGG)
                .add(Items.SNOWBALL);
        this.tag(CNTags.SHIELDS)
                .add(Items.SHIELD);
    }
}
