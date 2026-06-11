package at.acpi.heartfall.neoforge;

//? if neoforge {

/*import at.acpi.heartfall.Heartfall;
import at.acpi.heartfall.config.HeartfallConfig;
import at.acpi.heartfall.registry.HeartfallEntityRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(Heartfall.MOD_ID)
public class HeartfallNeoForgeEntrypoint {
	public HeartfallNeoForgeEntrypoint(IEventBus bus) {
		Heartfall.init();
		HeartfallEntityRegistry.registerEntities(bus);

		if (FMLEnvironment.getDist() != Dist.CLIENT)
			return;

		ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class,
				() -> (_container, parent) -> HeartfallConfig.createScreen(parent));
	}
}
*///?}
