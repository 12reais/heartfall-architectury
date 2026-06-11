package at.acpi.heartfall.fabric;

//? fabric {

import at.acpi.heartfall.config.HeartfallConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class HeartfallModMenuIntegration implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return HeartfallConfig::createScreen;
	}
}//?}

