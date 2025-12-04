package at.acpi.heartfall.client;

import at.acpi.heartfall.Heartfall;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;

public class HeartfallClient {
    public static void init() {
        EntityRendererRegistry.register(Heartfall.ABSORBABLE_HEALTH, AbsorbableHealthEntityRenderer::new);
    }
}
