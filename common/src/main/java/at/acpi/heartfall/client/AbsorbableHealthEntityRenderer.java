package at.acpi.heartfall.client;

import at.acpi.heartfall.AbsorbableHealthEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;


public class AbsorbableHealthEntityRenderer extends EntityRenderer<AbsorbableHealthEntity, AbsorbableHealthEntityRenderer.AbsorbableHealthEntityRenderState> {
    private static final Identifier
            CONTAINER = Identifier.withDefaultNamespace("textures/gui/sprites/hud/heart/container.png"),
            HEART_FULL = Identifier.withDefaultNamespace("textures/gui/sprites/hud/heart/full.png");

    private static final RenderType
            CONTAINER_LAYER = RenderTypes.entityTranslucentCullItemTarget(CONTAINER),
            HEART_LAYER = RenderTypes.entityTranslucentCullItemTarget(HEART_FULL);

    public AbsorbableHealthEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    private static void produceVertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, int alpha, float u, float v) {
        consumer.addVertex(pose, x, y, z).setColor(255, 255, 255, alpha).setUv(u, v).setOverlay(0).setLight(LightCoordsUtil.FULL_BRIGHT).setNormal(pose, 0f, 1f, 0f);
    }

    @Override
    public @NotNull AbsorbableHealthEntityRenderState createRenderState() {
        return new AbsorbableHealthEntityRenderState();
    }

    @Override
    public void extractRenderState(@NonNull AbsorbableHealthEntity entity, @NonNull AbsorbableHealthEntityRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        float delta = entity.tickCount + partialTicks;

        float bob = (float) Math.sin(delta / 10.0 + 0.5) * 0.1f;
        float deathProgress = entity.getDeathProgress(partialTicks);

        state.rotation = delta / 20.0f;
        state.scale = 0.5f + (float) Math.sin(delta * 0.25f) * 0.05f;
        state.bob = .5f + bob;

        float pulse = 0.75f + (float) Math.sin(delta * 0.25f) * 0.1f;
        float ageFade = 1.0f - Math.max(0, entity.tickCount - 220) / 100.0f;

        state.alpha = (int) (pulse * ageFade * (1f - deathProgress) * 225f);
    }

    @Override
    public void submit(AbsorbableHealthEntityRenderState state, PoseStack stack, @NonNull SubmitNodeCollector collector, @NonNull CameraRenderState camera) {
        stack.pushPose();

        stack.translate(0, state.bob, 0);
        stack.scale(state.scale, state.scale, state.scale);
        stack.mulPose(Axis.YP.rotation(state.rotation));

        drawQuad(stack, collector, HEART_LAYER, state.alpha, 0f);
        drawQuad(stack, collector, CONTAINER_LAYER, state.alpha, -0.001f);
        drawQuad(stack, collector, HEART_LAYER, state.alpha, -0.002f);

        stack.popPose();
        super.submit(state, stack, collector, camera);
    }

    private void drawQuad(PoseStack matrices, SubmitNodeCollector collector, RenderType layer, int alpha, float zOffset) {
        collector.submitCustomGeometry(matrices, layer, (pose, consumer) -> {
            produceVertex(consumer, pose, -0.5f, -0.5f, zOffset, alpha, 0f, 1f);
            produceVertex(consumer, pose, 0.5f, -0.5f, zOffset, alpha, 1f, 1f);
            produceVertex(consumer, pose, 0.5f, 0.5f, zOffset, alpha, 1f, 0f);
            produceVertex(consumer, pose, -0.5f, 0.5f, zOffset, alpha, 0f, 0f);

            produceVertex(consumer, pose, -0.5f, 0.5f, zOffset, alpha, 0f, 0f);
            produceVertex(consumer, pose, 0.5f, 0.5f, zOffset, alpha, 1f, 0f);
            produceVertex(consumer, pose, 0.5f, -0.5f, zOffset, alpha, 1f, 1f);
            produceVertex(consumer, pose, -0.5f, -0.5f, zOffset, alpha, 0f, 1f);
        });
    }


    public static class AbsorbableHealthEntityRenderState extends EntityRenderState {
        public float scale, rotation, bob;
        public int alpha;
    }
}
