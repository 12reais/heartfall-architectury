package at.acpi.heartfall.client.registry;

import at.acpi.heartfall.client.renderer.HeartShardEntityRenderer;
import at.acpi.heartfall.registry.HeartfallEntities;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;

public final class HeartfallEntityRenderers {
    private HeartfallEntityRenderers() {}

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(HeartfallEntities.HEARD_SHARD, HeartShardEntityRenderer::new);
    }
}
