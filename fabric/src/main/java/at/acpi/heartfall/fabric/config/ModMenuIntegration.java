package at.acpi.heartfall.fabric.config;

import at.acpi.heartfall.config.HeartfallConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return HeartfallConfig::createScreen;
    }
}
