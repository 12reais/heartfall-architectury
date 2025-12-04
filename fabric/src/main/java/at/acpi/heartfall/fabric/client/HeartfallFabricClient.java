package at.acpi.heartfall.fabric.client;

import at.acpi.heartfall.client.HeartfallClient;
import net.fabricmc.api.ClientModInitializer;

public final class HeartfallFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HeartfallClient.init();
    }
}
