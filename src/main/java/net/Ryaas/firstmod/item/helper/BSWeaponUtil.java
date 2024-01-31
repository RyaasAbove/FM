package net.Ryaas.firstmod.item.helper;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;


public interface BSWeaponUtil {




    default void MoreDMGhits(Player holder, Level level, LivingEntity target, float damage) {


            target.hurt(level.damageSources().magic(), damage);

        }

    public static void decrementHitCount(LivingEntity entity, String key) {
        CompoundTag entityData = entity.getPersistentData();
        int hitCount = entityData.getInt(key);
        entityData.putInt(key, Math.max(0, hitCount - 1));
        entity.addAdditionalSaveData(entityData);
    }


}





