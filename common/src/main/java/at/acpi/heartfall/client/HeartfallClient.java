package at.acpi.heartfall.client;

import at.acpi.heartfall.client.registry.HeartfallEntityRenderers;

public class HeartfallClient {
    public static void init() {
        HeartfallEntityRenderers.registerEntityRenderers();
    }
}
