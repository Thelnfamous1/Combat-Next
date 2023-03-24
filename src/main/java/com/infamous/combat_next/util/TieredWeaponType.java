package com.infamous.combat_next.util;

import net.minecraft.world.item.Tiers;

import java.util.Arrays;

public enum TieredWeaponType {
    AXE("axe", new double[]{5, 5, 6, 7, 5, 8}, makeFilled(2), makeFilled(2.5D)),
    HOE("hoe", new double[]{2, 2, 3, 4, 2, 5}, new double[]{2, 2.5D, 3, 3.5D, 3.5D, 3.5D}, makeFilled(3.5D)),
    PICKAXE("pickaxe", new double[]{3, 3, 4, 5, 3, 6}, makeFilled(2), makeFilled(2.5D)),
    SHOVEL("shovel", new double[]{2, 2, 3, 4, 2, 5}, makeFilled(2.5D), makeFilled(2.5D)),
    SWORD("sword", new double[]{4, 4, 5, 6, 4, 7}, makeFilled(3), makeFilled(3));

    private final String name;
    private final double[] attackDamage;
    private final double[] attackSpeed;
    private final double[] attackRange;

    TieredWeaponType(String name, double[] attackDamage, double[] attackSpeed, double[] attackRange) {
        this.name = name;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.attackRange = attackRange;
    }

    private static double[] makeFilled(double defaultValue){
        double[] array = new double[6];
        Arrays.fill(array, defaultValue);
        return array;
    }

    public String getName() {
        return name;
    }

    public double getAttackDamage(Tiers vanillaTier) {
        return attackDamage[vanillaTier.ordinal()];
    }

    public double getAttackSpeed(Tiers vanillaTier) {
        return attackSpeed[vanillaTier.ordinal()];
    }

    public double getAttackRange(Tiers vanillaTier) {
        return attackRange[vanillaTier.ordinal()];
    }

    @FunctionalInterface
    public interface ValueGetter{

        double apply(TieredWeaponType weaponType, Tiers vanillaTier);
    }
}
