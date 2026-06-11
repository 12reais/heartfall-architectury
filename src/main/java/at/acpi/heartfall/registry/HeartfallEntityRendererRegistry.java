package at.acpi.heartfall.registry;

import at.acpi.heartfall.renderer.HeartShardEntityRenderer;

//? fabric {
import net.minecraft.client.renderer.entity.EntityRenderers;
//? }

//? neoforge {
/*import at.acpi.heartfall.Heartfall;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Heartfall.MOD_ID, value = Dist.CLIENT)
*///?}
public final class HeartfallEntityRendererRegistry {
	private HeartfallEntityRendererRegistry() {
	}

	//? fabric {
	public static void registerEntityRenderers() {
		EntityRenderers.register(HeartfallEntityRegistry.heartShard(), HeartShardEntityRenderer::new);
    }//?} else neoforge {
	/*@SubscribeEvent
	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(HeartfallEntityRegistry.heartShard(), HeartShardEntityRenderer::new);
	}
	*///?}
}
