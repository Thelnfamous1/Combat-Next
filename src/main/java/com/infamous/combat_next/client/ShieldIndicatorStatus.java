package com.infamous.combat_next.client;

import java.util.Arrays;
import java.util.Comparator;
import net.minecraft.util.Mth;
import net.minecraft.util.OptionEnum;

public enum ShieldIndicatorStatus implements OptionEnum {
   OFF(0, "options.off"),
   CROSSHAIR(1, "options.attack.crosshair"),
   HOTBAR(2, "options.attack.hotbar");

   private static final ShieldIndicatorStatus[] BY_ID = Arrays.stream(values())
           .sorted(Comparator.comparingInt(ShieldIndicatorStatus::getId))
           .toArray(ShieldIndicatorStatus[]::new);
   private final int id;
   private final String key;

   ShieldIndicatorStatus(int id, String key) {
      this.id = id;
      this.key = key;
   }

   @Override
   public int getId() {
      return this.id;
   }

   @Override
   public String getKey() {
      return this.key;
   }

   public static ShieldIndicatorStatus byId(int id) {
      return BY_ID[Mth.positiveModulo(id, BY_ID.length)];
   }
}