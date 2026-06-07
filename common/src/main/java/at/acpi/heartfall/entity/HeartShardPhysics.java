package at.acpi.heartfall.entity;

import net.minecraft.world.phys.Vec3;

public final class HeartShardPhysics {
    public static final double GRAVITY = 0.075;
    public static final double DAMPING = 0.9;
    public static final double ATTRACTION_BLEND = 0.25;
    public static final double MOTION_DAMP = 0.75;
    public static final double ATTRACTION_SPEED = 0.1;
    public static final double ATTRACTION_RANGE = 5.0;

    private HeartShardPhysics() {
    }

    public static Vec3 applyGravityAndBounce(Vec3 motion, double currentY, double spawnY) {
        motion = motion.subtract(0, GRAVITY, 0);
        if (currentY <= spawnY)
            motion = new Vec3(motion.x, -motion.y * 0.6, motion.z);
        return motion;
    }

    public static Vec3 attractTowards(Vec3 motion, Vec3 entityPos, Vec3 playerPos) {
        Vec3 toPlayer = playerPos.subtract(entityPos);
        double distance = toPlayer.length();
        double speed = 1.0 + (ATTRACTION_RANGE - distance) / ATTRACTION_RANGE * 3.0;
        Vec3 attraction = toPlayer.normalize().scale(ATTRACTION_SPEED * speed);
        return motion.scale(MOTION_DAMP).add(attraction.scale(ATTRACTION_BLEND));
    }
}