package at.acpi.heartfall;

import at.acpi.heartfall.config.HeartfallConfig;
import at.acpi.heartfall.entity.HeartShardEntity;
import at.acpi.heartfall.entity.HeartShardSpawnUtils;
import at.acpi.heartfall.registry.HeartfallEntityRegistry;
import at.acpi.heartfall.sound.HeartfallSounds;
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
    }

	@SuppressWarnings("resource")
	public static void process(LivingEntity entity, DamageSource src) {
		if (entity.level().isClientSide()) return;
		if (!(entity instanceof Mob) || entity instanceof Animal) return;
		if (!HeartShardSpawnUtils.shouldDrop(src, entity.getRandom())) return;

		ServerLevel level = (ServerLevel) entity.level();
		var heart = new HeartShardEntity(HeartfallEntityRegistry.heartShard(), level);

		Vec3 safePos = HeartShardSpawnUtils.findSafePos(heart, entity.position(), level);
		if (safePos == null) return;

		heart.setPos(safePos);
		level.addFreshEntity(heart);
		HeartfallSounds.playSpawn(level, safePos, entity.getRandom());
	}

    public static Identifier of(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
