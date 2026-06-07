package at.acpi.heartfall;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

public final class Heartfall {
    public static final String MOD_ID = "heartfall";

    private static final float BASE_DROP_CHANCE = .175f;
    private static final float MAX_DROP_CHANCE = .49f;

    private static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(MOD_ID, Registries.ENTITY_TYPE);

    private static final ResourceKey<EntityType<?>> key =
            ResourceKey.create(Registries.ENTITY_TYPE, of("absorbable_health"));

    public static final RegistrySupplier<EntityType<AbsorbableHealthEntity>> ABSORBABLE_HEALTH =
            ENTITIES.register("absorbable_health", () ->
                    EntityType.Builder.of(AbsorbableHealthEntity::new, MobCategory.MISC)
                            .sized(.75f, .75f)
                            .fireImmune()
                            .updateInterval(1)
                            .build(key)
            );

    public static void init() {
        ENTITIES.register();
        EntityEvent.LIVING_DEATH.register(Heartfall::spawn);
    }

    private static boolean shouldDrop(LivingEntity entity, DamageSource src) {
        float chance = BASE_DROP_CHANCE;
        if (src.getEntity() instanceof Player player && !player.isCreative()) {
            float healthFraction = player.getHealth() / player.getMaxHealth();
            chance = BASE_DROP_CHANCE + (MAX_DROP_CHANCE - BASE_DROP_CHANCE) * (1f - healthFraction);
        }
        return entity.getRandom().nextFloat() < chance;
    }

    @SuppressWarnings("resource")
    private static EventResult spawn(LivingEntity entity, DamageSource src) {
        if (entity.level().isClientSide()) return EventResult.pass();

        if (!(entity instanceof Mob) || entity instanceof Animal)
            return EventResult.pass();

        if (!shouldDrop(entity, src))
            return EventResult.pass();

        var health = new AbsorbableHealthEntity(ABSORBABLE_HEALTH.get(), entity.level());
        health.setPos(entity.position());
        entity.level().addFreshEntity(health);

        entity.level().playSound(
                null,
                entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.ITEM_PICKUP,
                SoundSource.NEUTRAL,
                1.25f,
                0.9f + entity.getRandom().nextFloat() * 0.2f
        );

        return EventResult.pass();
    }

    public static Identifier of(String location) {
        return Identifier.fromNamespaceAndPath(MOD_ID, location);
    }
}
