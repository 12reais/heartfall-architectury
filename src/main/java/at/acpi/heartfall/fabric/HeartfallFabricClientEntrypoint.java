package at.acpi.heartfall.fabric;

//? fabric {

import at.acpi.heartfall.registry.HeartfallEntityRendererRegistry;
import net.fabricmc.api.ClientModInitializer;

public class HeartfallFabricClientEntrypoint implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		HeartfallEntityRendererRegistry.registerEntityRenderers();
	}
}
//?}
