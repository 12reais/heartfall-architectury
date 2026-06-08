package at.acpi.heartfall.entity;

import at.acpi.heartfall.sound.HeartfallSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

@SuppressWarnings("resource")
public class HeartShardEntity extends Entity {
    public static final float MIN_HEAL = 1.5f;
    public static final float MAX_HEAL = 5f;
    public static final double PICKUP_RANGE = 1.25;

    private static final int DEATH_ANIMATION_TICKS = 10;
    private static final int LIFESPAN_TICKS = 32 * 20;

    private static final double SPAWN_HORIZONTAL_SPREAD = 0.3;
    private static final double SPAWN_VERTICAL_BASE = 0.2;
    private static final double SPAWN_VERTICAL_SPREAD = 0.2;

    private static final EntityDataAccessor<Float> DATA_HEAL =
            SynchedEntityData.defineId(HeartShardEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_DEATH_TICKS =
            SynchedEntityData.defineId(HeartShardEntity.class, EntityDataSerializers.INT);

    private double spawnY;

    public HeartShardEntity(EntityType<? extends HeartShardEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_HEAL, MIN_HEAL + random.nextFloat() * (MAX_HEAL - MIN_HEAL));
        builder.define(DATA_DEATH_TICKS, 0);

        setYRot(random.nextFloat() * 360f);
        invulnerableTime = 0;

        setDeltaMovement(
                (random.nextDouble() - 0.5) * SPAWN_HORIZONTAL_SPREAD,
                SPAWN_VERTICAL_BASE + random.nextDouble() * SPAWN_VERTICAL_SPREAD,
                (random.nextDouble() - 0.5) * SPAWN_HORIZONTAL_SPREAD
        );
        spawnY = getY();
    }

    public float getHeal() {
        return getEntityData().get(DATA_HEAL);
    }

    public void setHeal(float v) {
        getEntityData().set(DATA_HEAL, v);
    }

    public int getDeathTicks() {
        return getEntityData().get(DATA_DEATH_TICKS);
    }

    public void setDeathTicks(int v) {
        getEntityData().set(DATA_DEATH_TICKS, v);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isDying() {
        return getDeathTicks() > 0;
    }

    public float getDeathProgress(float partialTicks) {
        if (!isDying()) return 0f;
        return 1f - (getDeathTicks() + partialTicks) / (float) DEATH_ANIMATION_TICKS;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput tag) {
        setHeal(tag.getFloatOr("heal", 3f));
        setDeathTicks(tag.getIntOr("deathTicks", 0));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput tag) {
        tag.putFloat("heal", getHeal());
        tag.putInt("deathTicks", getDeathTicks());
    }

    @Override
    public boolean hurtServer(@NonNull ServerLevel level, @NonNull DamageSource src, float amount) {
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (handleDeathAnimation()) return;
        if (handleLifespan()) return;
        if (isInsideBlock()) {
            setDeathTicks(DEATH_ANIMATION_TICKS);
            return;
        }

        Player player = level().getNearestPlayer(this, HeartShardPhysics.ATTRACTION_RANGE);
        Vec3 motion = getDeltaMovement();

        if (player == null || cannotAttractTo(player)) {
            setNoGravity(false);
            motion = HeartShardPhysics.applyGravityAndBounce(motion, getY(), spawnY);
            if (getY() <= spawnY) setPos(getX(), spawnY, getZ());
        } else {
            if (attemptPickup(player)) return;
            setNoGravity(true);
            Vec3 target = player.position().add(0, player.getBbHeight() * 0.25, 0);
            motion = HeartShardPhysics.attractTowards(motion, position(), target);
        }

        setDeltaMovement(motion);
        move(MoverType.SELF, getDeltaMovement());
        setDeltaMovement(getDeltaMovement().scale(HeartShardPhysics.DAMPING));
    }

    private boolean handleDeathAnimation() {
        if (!isDying()) return false;
        int ticks = getDeathTicks() - 1;
        setDeathTicks(ticks);

        Player nearest = level().getNearestPlayer(this, HeartShardPhysics.ATTRACTION_RANGE);
        if (nearest != null) {
            double progress = 1.0 - ticks / (double) DEATH_ANIMATION_TICKS;
            Vec3 current = position();
            Vec3 target = nearest.position().add(0, nearest.getBbHeight() * 0.25, 0);
            setPos(
                    current.x * (1 - progress) + target.x * progress,
                    current.y * (1 - progress) + target.y * progress,
                    current.z * (1 - progress) + target.z * progress
            );
        }

        if (ticks <= 0) discard();
        return true;
    }

    private boolean handleLifespan() {
        if (tickCount < LIFESPAN_TICKS) return false;
        setDeathTicks(DEATH_ANIMATION_TICKS);
        return true;
    }

    private boolean isInsideBlock() {
        return !level().noCollision(this, getBoundingBox().deflate(0.01));
    }

    private boolean cannotAttractTo(Player player) {
        return player.isSpectator() || (!player.isCreative() && player.getHealth() >= player.getMaxHealth());
    }

    private boolean attemptPickup(Player player) {
        if (level().isClientSide() || distanceTo(player) >= PICKUP_RANGE) return false;
        if (cannotAttractTo(player)) return false;
        player.setHealth(Math.min(player.getHealth() + getHeal(), player.getMaxHealth()));
        HeartfallSounds.playPickup(player, getHeal(), MAX_HEAL);
        setDeathTicks(DEATH_ANIMATION_TICKS);
        return true;
    }
}