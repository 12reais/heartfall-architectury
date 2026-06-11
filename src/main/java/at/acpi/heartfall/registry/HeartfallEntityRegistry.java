package at.acpi.heartfall.registry;

import at.acpi.heartfall.Heartfall;
import at.acpi.heartfall.entity.HeartShardEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

//? neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

final class NeoForgeEntityRegistry {
    private NeoForgeEntityRegistry() {
    }

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, Heartfall.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<HeartShardEntity>> HEART_SHARD =
            ENTITIES.register("heart_shard", HeartfallEntityRegistry::buildHeartShardEntityType);

    public static void registerEntities(IEventBus bus) {
        ENTITIES.register(bus);
    }
}

*///?} else fabric {
final class FabricEntityRegistry {
    private FabricEntityRegistry() {
    }

    public static EntityType<HeartShardEntity> HEART_SHARD;

    public static void registerEntities() {
        HEART_SHARD = Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                Heartfall.of("heart_shard"),
                HeartfallEntityRegistry.buildHeartShardEntityType()
        );
    }
}
//? }

public final class HeartfallEntityRegistry {
    private HeartfallEntityRegistry() {
    }

    public static final ResourceKey<EntityType<?>> HEART_SHARD_KEY =
            ResourceKey.create(Registries.ENTITY_TYPE, Heartfall.of("heart_shard"));

    public static EntityType<HeartShardEntity> buildHeartShardEntityType() {
        return EntityType.Builder.of(HeartShardEntity::new, MobCategory.MISC)
                .sized(.75f, .75f)
                .fireImmune()
                .updateInterval(1)
                .build(HeartfallEntityRegistry.HEART_SHARD_KEY);
    }

    public static EntityType<HeartShardEntity> heartShard() {
        //? fabric {
        return FabricEntityRegistry.HEART_SHARD;
        //? } else neoforge {
        /*return NeoForgeEntityRegistry.HEART_SHARD.get();
         *///?}
    }

    //? if neoforge {
    /*public static void registerEntities(IEventBus bus) {
        NeoForgeEntityRegistry.registerEntities(bus);
    }

    *///? } else if fabric {
    public static void registerEntities() {
        FabricEntityRegistry.registerEntities();
    }
    //? }

}