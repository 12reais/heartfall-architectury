package at.acpi.heartfall.forge;

import at.acpi.heartfall.Heartfall;
import at.acpi.heartfall.client.HeartfallClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(Heartfall.MOD_ID)
public final class HeartfallForge {
    public HeartfallForge() {
        Heartfall.init();
        if (FMLEnvironment.getDist() == Dist.CLIENT)
            HeartfallClient.init();
    }
}
