package at.acpi.heartfall;

import at.acpi.heartfall.config.HeartfallConfig;
import at.acpi.heartfall.entity.HeartShardEntity;
import at.acpi.heartfall.entity.HeartShardSpawnUtils;
import at.acpi.heartfall.registry.HeartfallEntities;
import at.acpi.heartfall.sound.HeartfallSounds;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.phys.Vec3;

public final class Heartfall {
    public static final String MOD_ID = "heartfall";

    public static void init() {
        HeartfallConfig.load();
        HeartfallEntities.registerEntities();
        EntityEvent.LIVING_DEATH.register(Heartfall::onLivingDeath);
    }

    @SuppressWarnings("resource")
    private static EventResult onLivingDeath(LivingEntity entity, DamageSource src) {
        if (entity.level().isClientSide()) return EventResult.pass();
        if (!(entity instanceof Mob) || entity instanceof Animal) return EventResult.pass();
        if (!HeartShardSpawnUtils.shouldDrop(src, entity.getRandom())) return EventResult.pass();

        ServerLevel level = (ServerLevel) entity.level();
        var heart = new HeartShardEntity(HeartfallEntities.HEARD_SHARD.get(), level);

        Vec3 safePos = HeartShardSpawnUtils.findSafePos(heart, entity.position(), level);
        if (safePos == null) return EventResult.pass();

        heart.setPos(safePos);
        level.addFreshEntity(heart);
        HeartfallSounds.playSpawn(level, safePos, entity.getRandom());

        return EventResult.pass();
    }

    public static Identifier of(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}