package at.acpi.heartfall.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class HeartShardSpawnUtils {
    private static final float BASE_DROP_CHANCE = 0.175f;
    private static final float MAX_DROP_CHANCE = 0.49f;

    private static final Vec3[] OFFSETS = {
            new Vec3(0, 0, 0),
            new Vec3(0, 1, 0),
            new Vec3(1, 0, 0),
            new Vec3(-1, 0, 0),
            new Vec3(0, 0, 1),
            new Vec3(0, 0, -1),
            new Vec3(0, 2, 0),
    };

    private HeartShardSpawnUtils() {
    }

    public static boolean shouldDrop(DamageSource src, RandomSource random) {
        float chance = BASE_DROP_CHANCE;
        if (!src.isCreativePlayer() && src.getEntity() instanceof LivingEntity damager) {
            float healthFraction = damager.getHealth() / damager.getMaxHealth();
            chance = BASE_DROP_CHANCE + (MAX_DROP_CHANCE - BASE_DROP_CHANCE) * (1f - healthFraction);
        }
        return random.nextFloat() < chance;
    }

    public static Vec3 findSafePos(HeartShardEntity entity, Vec3 origin, ServerLevel level) {
        for (Vec3 offset : OFFSETS) {
            Vec3 candidate = origin.add(offset);
            AABB box = entity.getBoundingBox().move(candidate.subtract(entity.position()));
            if (level.noCollision(entity, box))
                return candidate;
        }
        return null;
    }
}
