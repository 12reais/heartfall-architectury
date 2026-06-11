package at.acpi.heartfall;

//? fabric {
import at.acpi.heartfall.fabric.HeartfallFabricPlatform;

 
//?} else neoforge {

/*import at.acpi.heartfall.neoforge.HeartfallNeoForgePlatform;
*///? }

import java.nio.file.Path;

public interface HeartfallPlatform {

	//? fabric {
	HeartfallPlatform PLATFORM = new HeartfallFabricPlatform();
	 //? } else neoforge {
	/*HeartfallPlatform PLATFORM = new HeartfallNeoForgePlatform();
	*///? }

	Path getConfigFolder();

	String loader();
}
