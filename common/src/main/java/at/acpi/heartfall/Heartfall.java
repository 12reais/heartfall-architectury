package at.acpi.heartfall;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Animal;

public final class Heartfall {
    public static final String MOD_ID = "heartfall";

    private static final float DROP_CHANCE = 0.325f;

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

    private static boolean shouldDrop(LivingEntity entity) {
        return entity.getRandom().nextFloat() < DROP_CHANCE;
    }

    private static EventResult spawn(LivingEntity entity, DamageSource src) {
        if (entity.level().isClientSide()) return EventResult.pass();

        if (!(entity instanceof Mob) || entity instanceof Animal)
            return EventResult.pass();

        if (!shouldDrop(entity))
            return EventResult.pass();

        var health = new AbsorbableHealthEntity(ABSORBABLE_HEALTH.get(), entity.level());
        health.setPos(entity.position());
        entity.level().addFreshEntity(health);

        entity.level().playSound(
                null,
                entity.getX(), entity.getY(), entity.getZ(),
                SoundEvents.ITEM_PICKUP,
                SoundSource.NEUTRAL,
                0.4f,
                0.8f
        );

        return EventResult.pass();
    }

    public static ResourceLocation of(String location) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, location);
    }
}
