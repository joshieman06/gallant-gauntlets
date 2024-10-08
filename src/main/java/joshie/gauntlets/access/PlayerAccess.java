package joshie.gauntlets.access;

import net.minecraft.entity.Entity;

public interface PlayerAccess {

    void resetLastDualOffhandAttackTicks();

    float getAttackCooldownProgressDualOffhand(float baseTime);

    void attackOffhand(Entity target, String part);

}