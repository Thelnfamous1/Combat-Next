package com.infamous.combat_next.data;

import com.infamous.combat_next.CombatNext;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class CNBlockTagsProvider extends BlockTagsProvider {
    public CNBlockTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, CombatNext.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {

    }
}
