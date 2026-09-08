package at.acpi.heartfall.renderer;

//? if <1.21.2 {
/*public class HeartShardEntityRenderState {
}
*///?} else {
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class HeartShardEntityRenderState extends EntityRenderState {
	public float scale, rotation, bob;
	public final ItemStackRenderState item = new ItemStackRenderState();
}
//?}
