package at.acpi.heartfall.client;

import at.acpi.heartfall.registry.HeartfallEntities;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;

public class HeartfallClient {
    public static void init() {
        HeartShardEntityRenderer.register();
        EntityRendererRegistry.register(HeartfallEntities.HEARD_SHARD, HeartShardEntityRenderer::new);
    }
}
