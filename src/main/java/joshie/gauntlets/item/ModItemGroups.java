package joshie.gauntlets.item;

import joshie.gauntlets.Gauntlets;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup GAUNTLETS_GROUP = Registry.register(Registries.ITEM_GROUP, Identifier.of(Gauntlets.MOD_ID, "gauntlets"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.gauntlets"))
                    .icon(() ->  new ItemStack(ModItems.IRON_GAUNTLET)).entries(((displayContext, entries) ->  {
                        entries.add(ModItems.IRON_GAUNTLET);
                        entries.add(ModItems.GOLD_GAUNTLET);
                        entries.add(ModItems.DIAMOND_GAUNTLET);
                        entries.add(ModItems.NETHERITE_GAUNTLET);
                    })).build());
    public static void registerItemGroups() {
        Gauntlets.LOGGER.info("[Gauntlets] REGISTERING ITEM GROUPS");
    }
}
