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
	private static final float DEFAULT_MIN_HEAL = 1.5f;
	private static final float DEFAULT_MAX_HEAL = 5.0f;
	private static final double DEFAULT_PICKUP_RANGE = 1.5;
	private static final int DEFAULT_DEATH_ANIMATION_TICKS = 10;
	private static final int DEFAULT_LIFESPAN_TICKS = 6000;
	private static final float DEFAULT_BASE_DROP_CHANCE = .125f;
	private static final float DEFAULT_MAX_DROP_CHANCE = .5f;

	public static final ConfigClassHandler<HeartfallConfig> HANDLER = ConfigClassHandler.createBuilder(HeartfallConfig.class)
			.id(Heartfall.of("config"))
			.serializer(config -> GsonConfigSerializerBuilder.create(config)
					.setPath(HeartfallPlatform.PLATFORM.getConfigFolder().resolve("heartfall.json"))
					.build())
			.build();

	@SerialEntry
	public float minHeal = DEFAULT_MIN_HEAL;

	@SerialEntry
	public float maxHeal = DEFAULT_MAX_HEAL;

	@SerialEntry
	public double pickupRange = DEFAULT_PICKUP_RANGE;

	@SerialEntry
	public int deathAnimationTicks = DEFAULT_DEATH_ANIMATION_TICKS;

	@SerialEntry
	public int lifespanTicks = DEFAULT_LIFESPAN_TICKS;

	@SerialEntry
	public float baseDropChance = DEFAULT_BASE_DROP_CHANCE;

	@SerialEntry
	public float maxDropChance = DEFAULT_MAX_DROP_CHANCE;

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
								.binding(DEFAULT_MIN_HEAL, () -> get().minHeal, val -> get().minHeal = val)
								.controller(opt -> FloatSliderControllerBuilder.create(opt).range(0.0f, 20.0f).step(0.5f))
								.build())
						.option(Option.<Float>createBuilder()
								.name(Component.translatable("text.heartfall.config.option.maxHeal"))
								.description(OptionDescription.of(Component.translatable("text.heartfall.config.option.maxHeal.desc")))
								.binding(DEFAULT_MAX_HEAL, () -> get().maxHeal, val -> get().maxHeal = val)
								.controller(opt -> FloatSliderControllerBuilder.create(opt).range(0.0f, 40.0f).step(0.5f))
								.build())
						.option(Option.<Double>createBuilder()
								.name(Component.translatable("text.heartfall.config.option.pickupRange"))
								.description(OptionDescription.of(Component.translatable("text.heartfall.config.option.pickupRange.desc")))
								.binding(DEFAULT_PICKUP_RANGE, () -> get().pickupRange, val -> get().pickupRange = val)
								.controller(opt -> DoubleSliderControllerBuilder.create(opt).range(0.5, 5.0).step(0.25))
								.build())
						.option(Option.<Integer>createBuilder()
								.name(Component.translatable("text.heartfall.config.option.deathAnimationTicks"))
								.description(OptionDescription.of(Component.translatable("text.heartfall.config.option.deathAnimationTicks.desc")))
								.binding(DEFAULT_DEATH_ANIMATION_TICKS, () -> get().deathAnimationTicks, val -> get().deathAnimationTicks = val)
								.controller(opt -> IntegerSliderControllerBuilder.create(opt).range(0, 100).step(1))
								.build())
						.option(Option.<Integer>createBuilder()
								.name(Component.translatable("text.heartfall.config.option.lifespanTicks"))
								.description(OptionDescription.of(Component.translatable("text.heartfall.config.option.lifespanTicks.desc")))
								.binding(DEFAULT_LIFESPAN_TICKS, () -> get().lifespanTicks, val -> get().lifespanTicks = val)
								.controller(opt -> IntegerSliderControllerBuilder.create(opt).range(20, 12000).step(20))
								.build())
						.option(Option.<Float>createBuilder()
								.name(Component.translatable("text.heartfall.config.option.baseDropChance"))
								.description(OptionDescription.of(Component.translatable("text.heartfall.config.option.baseDropChance.desc")))
								.binding(DEFAULT_BASE_DROP_CHANCE, () -> get().baseDropChance, val -> get().baseDropChance = val)
								.controller(opt -> FloatSliderControllerBuilder.create(opt).range(0.0f, 1.0f).step(0.025f))
								.build())
						.option(Option.<Float>createBuilder()
								.name(Component.translatable("text.heartfall.config.option.maxDropChance"))
								.description(OptionDescription.of(Component.translatable("text.heartfall.config.option.maxDropChance.desc")))
								.binding(DEFAULT_MAX_DROP_CHANCE, () -> get().maxDropChance, val -> get().maxDropChance = val)
								.controller(opt -> FloatSliderControllerBuilder.create(opt).range(0.0f, 1.0f).step(0.025f))
								.build())
						.build())
				.save(HANDLER::save)
				.build()
				.generateScreen(parent);
	}
}
