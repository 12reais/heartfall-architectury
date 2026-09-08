package at.acpi.heartfall.entity;

import net.minecraft.world.phys.Vec3;

public final class HeartShardPhysics {
    public static final double GRAVITY = 0.075;
    public static final double DAMPING = 0.9;
    public static final double MOTION_DAMP = 0.8;

    public static final double ATTRACTION_BLEND = 0.16;
    public static final double ATTRACTION_SPEED = 0.07;
    public static final double ATTRACTION_CLOSING_BOOST = 1.5;
    public static final double ATTRACTION_RANGE = 3.0;

    private HeartShardPhysics() {
    }

    public static Vec3 applyGravity(Vec3 motion) {
        return motion.subtract(0, GRAVITY, 0);
    }

    public static Vec3 attractTowards(Vec3 motion, Vec3 entityPos, Vec3 playerPos) {
        Vec3 toPlayer = playerPos.subtract(entityPos);
        double distance = toPlayer.length();
        double speed = 1.0 + (ATTRACTION_RANGE - distance) / ATTRACTION_RANGE * ATTRACTION_CLOSING_BOOST;
        Vec3 attraction = toPlayer.normalize().scale(ATTRACTION_SPEED * speed);
        return motion.scale(MOTION_DAMP).add(attraction.scale(ATTRACTION_BLEND));
    }
}
