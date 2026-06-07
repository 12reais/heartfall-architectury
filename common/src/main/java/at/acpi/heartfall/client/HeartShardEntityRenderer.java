package at.acpi.heartfall.client;

import at.acpi.heartfall.entity.HeartShardEntity;
import at.acpi.heartfall.registry.HeartfallEntities;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
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


public class HeartShardEntityRenderer extends EntityRenderer<HeartShardEntity, HeartShardEntityRenderer.AbsorbableHealthEntityRenderState> {
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
            ROTATION_PERIOD = 25.0f,
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
            ALPHA_MAX = 230f;

    public HeartShardEntityRenderer(EntityRendererProvider.Context ctx) {
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
    public void extractRenderState(@NonNull HeartShardEntity entity, @NonNull AbsorbableHealthEntityRenderState state, float partialTicks) {
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

        state.alpha = (int) (pulse * ageFade * deathFade * ALPHA_MAX);
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

    public static void register() {
        EntityRendererRegistry.register(HeartfallEntities.HEARD_SHARD, HeartShardEntityRenderer::new);
    }
}
