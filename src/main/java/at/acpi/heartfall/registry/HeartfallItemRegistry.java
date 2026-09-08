package at.acpi.heartfall.registry;

import at.acpi.heartfall.Heartfall;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

//? neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

final class NeoForgeItemRegistry {
    private NeoForgeItemRegistry() {
    }

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Heartfall.MOD_ID);

    public static final DeferredHolder<Item, Item> HEART_SHARD_ICON =
            ITEMS.registerItem("heart_shard_icon", Item::new);

    public static void registerItems(IEventBus bus) {
        ITEMS.register(bus);
    }
}

*///?} else fabric {
final class FabricItemRegistry {
    private FabricItemRegistry() {
    }

    public static Item HEART_SHARD_ICON;

    public static void registerItems() {
        HEART_SHARD_ICON = Registry.register(
                BuiltInRegistries.ITEM,
                Heartfall.of("heart_shard_icon"),
                new Item(new Item.Properties())
        );
    }
}
//? }

public final class HeartfallItemRegistry {
    private HeartfallItemRegistry() {
    }

    public static Item heartShardIcon() {
        //? fabric {
        return FabricItemRegistry.HEART_SHARD_ICON;
        //? } else neoforge {
        /*return NeoForgeItemRegistry.HEART_SHARD_ICON.get();
         *///?}
    }

    //? if neoforge {
    /*public static void registerItems(IEventBus bus) {
        NeoForgeItemRegistry.registerItems(bus);
    }

    *///? } else if fabric {
    public static void registerItems() {
        FabricItemRegistry.registerItems();
    }
    //? }
}
