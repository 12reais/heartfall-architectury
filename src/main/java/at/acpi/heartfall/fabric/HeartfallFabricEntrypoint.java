package at.acpi.heartfall.fabric;

//? fabric {

import at.acpi.heartfall.Heartfall;
import at.acpi.heartfall.registry.HeartfallEntityRegistry;
import at.acpi.heartfall.registry.HeartfallItemRegistry;
import at.acpi.heartfall.registry.HeartfallSoundRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;

public class HeartfallFabricEntrypoint implements ModInitializer {
	@Override
	public void onInitialize() {
		Heartfall.init();
		HeartfallEntityRegistry.registerEntities();
		HeartfallItemRegistry.registerItems();
		HeartfallSoundRegistry.registerSounds();
		ServerLivingEntityEvents.AFTER_DEATH.register(Heartfall::process);
	}
}
//?}
