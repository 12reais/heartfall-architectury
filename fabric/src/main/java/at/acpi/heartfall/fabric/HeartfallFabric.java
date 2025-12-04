package at.acpi.heartfall.fabric;

import at.acpi.heartfall.Heartfall;
import net.fabricmc.api.ModInitializer;

public final class HeartfallFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Heartfall.init();
    }
}
