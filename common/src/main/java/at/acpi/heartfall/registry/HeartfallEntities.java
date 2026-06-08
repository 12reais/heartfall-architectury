package at.acpi.heartfall.registry;

import at.acpi.heartfall.Heartfall;
import at.acpi.heartfall.entity.HeartShardEntity;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class HeartfallEntities {
    private HeartfallEntities() {}

    private static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Heartfall.MOD_ID, Registries.ENTITY_TYPE);

    private static final ResourceKey<EntityType<?>> HEART_SHARD_KEY =
            ResourceKey.create(Registries.ENTITY_TYPE, Heartfall.of("heart_shard"));

    public static final RegistrySupplier<EntityType<HeartShardEntity>> HEARD_SHARD =
            ENTITIES.register("heart_shard", () ->
                    EntityType.Builder.of(HeartShardEntity::new, MobCategory.MISC)
                            .sized(.75f, .75f)
                            .fireImmune()
                            .updateInterval(1)
                            .build(HEART_SHARD_KEY)
            );

    public static void registerEntities() {
        ENTITIES.register();
    }
}