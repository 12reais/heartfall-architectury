package at.acpi.heartfall.registry;

import at.acpi.heartfall.Heartfall;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;

//? neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

final class NeoForgeSoundRegistry {
    private NeoForgeSoundRegistry() {
    }

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, Heartfall.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> PICKUP =
            SOUNDS.register("pickup", () -> SoundEvent.createVariableRangeEvent(Heartfall.of("pickup")));

    public static void registerSounds(IEventBus bus) {
        SOUNDS.register(bus);
    }
}

*///?} else fabric {
final class FabricSoundRegistry {
    private FabricSoundRegistry() {
    }

    public static SoundEvent PICKUP;

    public static void registerSounds() {
        PICKUP = Registry.register(
                BuiltInRegistries.SOUND_EVENT,
                Heartfall.of("pickup"),
                SoundEvent.createVariableRangeEvent(Heartfall.of("pickup"))
        );
    }
}
//? }

public final class HeartfallSoundRegistry {
    private HeartfallSoundRegistry() {
    }

    public static SoundEvent pickup() {
        //? fabric {
        return FabricSoundRegistry.PICKUP;
        //? } else neoforge {
        /*return NeoForgeSoundRegistry.PICKUP.get();
         *///?}
    }

    //? if neoforge {
    /*public static void registerSounds(IEventBus bus) {
        NeoForgeSoundRegistry.registerSounds(bus);
    }

    *///? } else if fabric {
    public static void registerSounds() {
        FabricSoundRegistry.registerSounds();
    }
    //? }
}
