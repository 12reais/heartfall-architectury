package at.acpi.heartfall;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class AbsorbableHealthEntity extends Entity {
    private static final EntityDataAccessor<Float> DATA_HEAL =
            SynchedEntityData.defineId(AbsorbableHealthEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> DATA_DEATH_TICKS =
            SynchedEntityData.defineId(AbsorbableHealthEntity.class, EntityDataSerializers.INT);

    private static final float MIN_HEAL = 1.5f,
            MAX_HEAL = 7.5f;
    private static final double PICKUP_RANGE = 1.4,
            ATTRACTION_RANGE = 5,
            ATTRACTION_SPEED = 0.1;
    private static final double GRAVITY = 0.075,
            DAMPING = 0.9,
            ATTRACTION_BLEND = 0.25,
            MOTION_DAMP = 0.75;
    private static final int DEATH_ANIMATION_TICKS = 10,
            LIFESPAN_TICKS = 16 * 20;

    private double spawnY;

    public AbsorbableHealthEntity(EntityType<? extends AbsorbableHealthEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_HEAL, MIN_HEAL + random.nextFloat() * (MAX_HEAL - MIN_HEAL));
        builder.define(DATA_DEATH_TICKS, 0);

        setYRot(this.random.nextFloat() * 360f);
        this.invulnerableTime = 0;

        double vx = (this.random.nextDouble() - 0.5) * 0.2;
        double vy = 0.175 + this.random.nextDouble() * 0.25;
        double vz = (this.random.nextDouble() - 0.5) * 0.2;

        this.setDeltaMovement(vx, vy, vz);
        this.spawnY = this.getY();
    }

    public float getHeal() {
        return this.getEntityData().get(DATA_HEAL);
    }

    public void setHeal(float life) {
        this.getEntityData().set(DATA_HEAL, life);
    }

    public int getDeathTicks() {
        return this.getEntityData().get(DATA_DEATH_TICKS);
    }

    public void setDeathTicks(int ticks) {
        this.getEntityData().set(DATA_DEATH_TICKS, ticks);
    }

    public boolean isDying() {
        return getDeathTicks() > 0;
    }

    public float getDeathProgress(float partialTicks) {
        if (!isDying()) return 0f;
        return 1.0f - ((getDeathTicks() + partialTicks) / (float) DEATH_ANIMATION_TICKS);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float f) {
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
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
    public void tick() {
        super.tick();

        if (handleDeathAnimation()) return;
        if (handleLifespan()) return;

        var player = this.level().getNearestPlayer(this, ATTRACTION_RANGE);
        Vec3 motion = this.getDeltaMovement();

        if (player == null || cannotAttractTo(player)) {
            motion = inhibitGravityIfAirborne(motion);
        } else {
            if (attemptPickup(player)) return;
            motion = attractTowards(motion, player);
        }

        this.setDeltaMovement(motion);
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().multiply(DAMPING, 1, DAMPING));
    }

    private boolean handleDeathAnimation() {
        if (!isDying()) return false;
        int ticks = getDeathTicks() - 1;
        setDeathTicks(ticks);
        if (ticks <= 0) discard();
        return true;
    }

    private boolean handleLifespan() {
        if (tickCount < LIFESPAN_TICKS) return false;
        setDeathTicks(DEATH_ANIMATION_TICKS);
        return true;
    }

    private boolean cannotAttractTo(Player player) {
        return player.isSpectator() || (!player.isCreative() && !(player.getHealth() < player.getMaxHealth()));
    }

    private Vec3 inhibitGravityIfAirborne(Vec3 motion) {
        this.setNoGravity(false);
        motion = motion.subtract(0, GRAVITY, 0);
        if (getY() <= spawnY) {
            motion = new Vec3(motion.x, -motion.y * 0.6, motion.z);
            setPos(getX(), spawnY, getZ());
        }
        return motion;
    }

    private Vec3 attractTowards(Vec3 motion, Player player) {
        Vec3 target = player.position().add(0, player.getBbHeight() / 2.75, 0);
        Vec3 toPlayer = target.subtract(this.position());

        double distance = toPlayer.length();
        double speed = 1.0 + (ATTRACTION_RANGE - distance) / ATTRACTION_RANGE * 3.0;
        Vec3 attraction = toPlayer.normalize().scale(ATTRACTION_SPEED * speed);

        this.setNoGravity(true);
        return motion.scale(MOTION_DAMP).add(attraction.scale(ATTRACTION_BLEND));
    }

    private boolean attemptPickup(Player player) {
        if (!this.level().isClientSide() && this.distanceTo(player) < PICKUP_RANGE) {
            tryPickup(player);
            return true;
        }
        return false;
    }

    private void tryPickup(Player player) {
        if (cannotAttractTo(player)) return;

        float hp = Math.min(player.getHealth() + getHeal(), player.getMaxHealth());
        player.setHealth(hp);

        float pitch = 1.25f + this.random.nextFloat() * 0.5f;
        player.level().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ITEM_PICKUP,
                SoundSource.PLAYERS,
                1f,
                pitch
        );
        setDeathTicks(DEATH_ANIMATION_TICKS);
    }
}
