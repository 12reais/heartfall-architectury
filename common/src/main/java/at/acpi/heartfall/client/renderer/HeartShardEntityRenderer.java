package at.acpi.heartfall.client.renderer;

import at.acpi.heartfall.entity.HeartShardEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;


public class HeartShardEntityRenderer extends EntityRenderer<HeartShardEntity, HeartShardEntityRenderState> {
    private static final Identifier
            CONTAINER = Identifier.withDefaultNamespace("textures/gui/sprites/hud/heart/container.png"),
            HEART_FULL = Identifier.withDefaultNamespace("textures/gui/sprites/hud/heart/full.png");

    private static final RenderType
            CONTAINER_LAYER = RenderTypes.itemTranslucent(CONTAINER),
            HEART_LAYER = RenderTypes.itemTranslucent(HEART_FULL);

    private static final float
            BOB_PRIMARY_PERIOD = 8.0f,
            BOB_PRIMARY_AMP = 0.08f,
            BOB_SECONDARY_PERIOD = 5.3f,
            BOB_SECONDARY_AMP = 0.03f,
            BOB_BASE = 0.5f,
            SCALE_BASE = 0.5f,
            SCALE_PRIMARY_FREQ = 0.18f,
            SCALE_PRIMARY_AMP = 0.03f,
            SCALE_SECONDARY_FREQ = 0.31f,
            SCALE_SECONDARY_AMP = 0.015f,
            PULSE_BASE = 0.85f,
            PULSE_FREQ = 0.22f,
            PULSE_AMP = 0.08f,
            AGE_FADE_START_TICKS = 200f,
            AGE_FADE_DURATION = 120f,
            ROTATION_PERIOD = 32f;
    public HeartShardEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    private static void produceVertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, int alpha, float u, float v, float nz) {
        consumer.addVertex(pose, x, y, z).setColor(255, 255, 255, alpha)
                .setUv(u, v).setOverlay(0).setLight(LightCoordsUtil.FULL_BRIGHT)
                .setNormal(pose, (float) 0.0, (float) 0.0, nz);
    }

    @Override
    public @NotNull HeartShardEntityRenderState createRenderState() {
        return new HeartShardEntityRenderState();
    }

    @Override
    public void extractRenderState(
            @NonNull HeartShardEntity entity, @NonNull HeartShardEntityRenderState state, float partialTicks
    ) {
        super.extractRenderState(entity, state, partialTicks);
        float delta = entity.tickCount + partialTicks;

        float bob = (float) (Math.sin(delta / BOB_PRIMARY_PERIOD) * BOB_PRIMARY_AMP
                + Math.sin(delta / BOB_SECONDARY_PERIOD) * BOB_SECONDARY_AMP);
        float deathProgress = entity.getDeathProgress(partialTicks);

        state.rotation = delta / ROTATION_PERIOD;
        state.scale = SCALE_BASE
                + (float) Math.sin(delta * SCALE_PRIMARY_FREQ) * SCALE_PRIMARY_AMP
                + (float) Math.sin(delta * SCALE_SECONDARY_FREQ) * SCALE_SECONDARY_AMP;
        state.bob = BOB_BASE + bob;

        float pulse = PULSE_BASE + (float) Math.sin(delta * PULSE_FREQ) * PULSE_AMP;
        float ageFade = 1.0f - Math.max(0f, (entity.tickCount - AGE_FADE_START_TICKS) / AGE_FADE_DURATION);
        float deathFade = deathProgress > 0f ? (float) Math.pow(1f - deathProgress, 2f) : 1f;

        state.alpha = (int) (pulse * ageFade * deathFade * 255f);
    }


    @Override
    public void submit(
            HeartShardEntityRenderState state, @NonNull PoseStack stack, @NonNull SubmitNodeCollector collector,
            @NonNull CameraRenderState camera
    ) {
        if (state.alpha <= 0) return;

        stack.pushPose();
        stack.translate(0, state.bob, 0);
        stack.mulPose(camera.viewRotationMatrix);
        stack.mulPose(Axis.YP.rotation(state.rotation));
        stack.scale(state.scale, state.scale, state.scale);

        collector.submitCustomGeometry(stack, HEART_LAYER, (pose, consumer) -> {
            drawPlane(consumer, pose, 0, state.alpha);
            drawPlane(consumer, pose, -0.002f, state.alpha);
        });
        collector.submitCustomGeometry(stack, CONTAINER_LAYER, (pose, consumer) -> {
            drawPlane(consumer, pose, -0.001f, state.alpha);
        });

        stack.popPose();
        super.submit(state, stack, collector, camera);
    }

    private void drawPlane(VertexConsumer consumer, PoseStack.Pose pose, float z, int alpha) {
        produceVertex(consumer, pose, (float) -0.5, (float) -0.5, z, alpha, 0f, 1f, -1f);
        produceVertex(consumer, pose, (float) 0.5, (float) -0.5, z, alpha, 1f, 1f, -1f);
        produceVertex(consumer, pose, (float) 0.5, (float) 0.5, z, alpha, 1f, 0f, -1f);
        produceVertex(consumer, pose, (float) -0.5, (float) 0.5, z, alpha, 0f, 0f, -1f);

        produceVertex(consumer, pose, (float) -0.5, (float) 0.5, z, alpha, 0f, 0f, 1f);
        produceVertex(consumer, pose, (float) 0.5, (float) 0.5, z, alpha, 1f, 0f, 1f);
        produceVertex(consumer, pose, (float) 0.5, (float) -0.5, z, alpha, 1f, 1f, 1f);
        produceVertex(consumer, pose, (float) -0.5, (float) -0.5, z, alpha, 0f, 1f, 1f);
    }
}
