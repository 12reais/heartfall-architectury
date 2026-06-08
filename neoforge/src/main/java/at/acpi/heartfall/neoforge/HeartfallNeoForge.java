package at.acpi.heartfall.neoforge;

import at.acpi.heartfall.Heartfall;
import at.acpi.heartfall.client.HeartfallClient;
import at.acpi.heartfall.config.HeartfallConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(Heartfall.MOD_ID)
public final class HeartfallNeoForge {
    public HeartfallNeoForge() {
        Heartfall.init();
        if (FMLEnvironment.getDist() != Dist.CLIENT)
            return;

        HeartfallClient.init();
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class,
                () -> (_, parent) -> HeartfallConfig.createScreen(parent));
    }
}
