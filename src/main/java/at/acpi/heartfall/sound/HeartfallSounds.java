package at.acpi.heartfall.sound;

import at.acpi.heartfall.registry.HeartfallSoundRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class HeartfallSounds {
    private static final float SPAWN_VOLUME = 1.0f;
    private static final float SPAWN_PITCH_BASE = 1.3f;
    private static final float SPAWN_PITCH_SPREAD = 0.2f;

    private static final float PICKUP_VOLUME = 0.8f;
    private static final float PICKUP_PITCH_BASE = 0.9f;
    private static final float PICKUP_PITCH_RANGE = 0.5f;

    private HeartfallSounds() {
    }

    public static void playSpawnSound(Level level, Vec3 pos, RandomSource random) {
        level.playSound(
                null,
                pos.x, pos.y, pos.z,
                SoundEvents.ITEM_PICKUP,
                SoundSource.NEUTRAL,
                SPAWN_VOLUME,
                SPAWN_PITCH_BASE + random.nextFloat() * SPAWN_PITCH_SPREAD
        );
    }

    @SuppressWarnings("resource")
    public static void playPickupSound(Player player, float healAmount, float maxHeal) {
        player.level().playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                HeartfallSoundRegistry.pickup(),
                SoundSource.PLAYERS,
                PICKUP_VOLUME,
                PICKUP_PITCH_BASE + (healAmount / maxHeal) * PICKUP_PITCH_RANGE
        );
    }
}
