package at.acpi.heartfall.config;

import at.acpi.heartfall.Heartfall;
import at.acpi.heartfall.HeartfallPlatform;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class HeartfallConfig {
    public static final ConfigClassHandler<HeartfallConfig> HANDLER = ConfigClassHandler.createBuilder(HeartfallConfig.class)
            .id(Heartfall.of("config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(HeartfallPlatform.PLATFORM.getConfigFolder().resolve("heartfall.json"))
                    .build())
            .build();

    @SerialEntry(comment = "The minimum amount of health a single heart shard can restore.")
    public float minHeal = 1.5f;

    @SerialEntry(comment = "The maximum amount of health a single heart shard can restore.")
    public float maxHeal = 5.0f;

    @SerialEntry(comment = "The block radius around a player within which heart shards are pulled toward them.")
    public double pickupRange = 1.5;

    @SerialEntry(comment = "Duration of the scaling/fade animation out when a shard is consumed (in game ticks).")
    public int deathAnimationTicks = 10;

    @SerialEntry(comment = "Total time a heart shard remains on the ground before despawning (in game ticks).")
    public int lifespanTicks = 32 * 20;

    @SerialEntry(comment = "The default drop probability when a hostile living entity dies.")
    public float baseDropChance = .125f;

    @SerialEntry(comment = "The highest possible drop probability, scaled based on how low the player's current health status is.")
    public float maxDropChance = .5f;

    public static HeartfallConfig get() {
        return HANDLER.instance();
    }

    public static void load() {
        HANDLER.load();
    }

    public static Screen createScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("text.heartfall.config.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("text.heartfall.config.category.general"))
                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.heartfall.config.option.minHeal"))
                                .description(OptionDescription.of(Component.translatable("text.heartfall.config.option.minHeal.desc")))
                                .binding(1.5f, () -> get().minHeal, val -> get().minHeal = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt).range(0.0f, 20.0f).step(0.5f))
                                .build())
                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.heartfall.config.option.maxHeal"))
                                .description(OptionDescription.of(Component.translatable("text.heartfall.config.option.maxHeal.desc")))
                                .binding(5.0f, () -> get().maxHeal, val -> get().maxHeal = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt).range(0.0f, 40.0f).step(0.5f))
                                .build())
                        .option(Option.<Double>createBuilder()
                                .name(Component.translatable("text.heartfall.config.option.pickupRange"))
                                .description(OptionDescription.of(Component.translatable("text.heartfall.config.option.pickupRange.desc")))
                                .binding(1.25, () -> get().pickupRange, val -> get().pickupRange = val)
                                .controller(opt -> DoubleSliderControllerBuilder.create(opt).range(0.5, 5.0).step(0.25))
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("text.heartfall.config.option.deathAnimationTicks"))
                                .description(OptionDescription.of(Component.translatable("text.heartfall.config.option.deathAnimationTicks.desc")))
                                .binding(10, () -> get().deathAnimationTicks, val -> get().deathAnimationTicks = val)
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(0, 100).step(1))
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("text.heartfall.config.option.lifespanTicks"))
                                .description(OptionDescription.of(Component.translatable("text.heartfall.config.option.lifespanTicks.desc")))
                                .binding(32 * 20, () -> get().lifespanTicks, val -> get().lifespanTicks = val)
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(20, 12000).step(20))
                                .build())
                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.heartfall.config.option.baseDropChance"))
                                .description(OptionDescription.of(Component.translatable("text.heartfall.config.option.baseDropChance.desc")))
                                .binding(.125f, () -> get().baseDropChance, val -> get().baseDropChance = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt).range(0.0f, 1.0f).step(0.025f))
                                .build())
                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("text.heartfall.config.option.maxDropChance"))
                                .description(OptionDescription.of(Component.translatable("text.heartfall.config.option.maxDropChance.desc")))
                                .binding(.5f, () -> get().maxDropChance, val -> get().maxDropChance = val)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt).range(0.0f, 1.0f).step(0.025f))
                                .build())
                        .build())
                .save(HANDLER::save)
                .build()
                .generateScreen(parent);
    }
}
