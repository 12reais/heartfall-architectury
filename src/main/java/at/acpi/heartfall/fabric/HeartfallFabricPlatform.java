package at.acpi.heartfall.fabric;

//? fabric {
import at.acpi.heartfall.HeartfallPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class HeartfallFabricPlatform implements HeartfallPlatform {
	@Override
	public Path getConfigFolder() {
		return FabricLoader.getInstance().getConfigDir();
	}

    @Override
    public String loader() {
        return "fabric";
    }
}
//?}
