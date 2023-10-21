package com.infamous.combat_next.util;

import net.minecraft.world.entity.player.Player;

public interface PlayerCombat {
    static PlayerCombat cast(Player player) {
        return (PlayerCombat) player;
    }
    int getMissedAttackRecovery();
    void setMissedAttackRecovery(int ticks);
}
