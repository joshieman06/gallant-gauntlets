package joshie.gauntlets.item;

import joshie.gauntlets.Gauntlets;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.fabricmc.fabric.api.item.v1.FabricItem.Settings;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item IRON_GAUNTLET = registerItem("iron_gauntlet",
            new GauntletItem(ToolMaterials.IRON, -2F, new Item.Settings().maxDamage(780).attributeModifiers(GauntletItem.createAttributeModifiers(ToolMaterials.IRON, 0, -2.0F))));
    public static final Item GOLD_GAUNTLET = registerItem("gold_gauntlet",
            new GauntletItem(ToolMaterials.GOLD, -1.6F, new Item.Settings().maxDamage(360).attributeModifiers(GauntletItem.createAttributeModifiers(ToolMaterials.GOLD, 0, -1.6F))));
    public static final Item DIAMOND_GAUNTLET = registerItem("diamond_gauntlet",
            new GauntletItem(ToolMaterials.DIAMOND, -1.7F, new Item.Settings().maxDamage(920).attributeModifiers(GauntletItem.createAttributeModifiers(ToolMaterials.DIAMOND, 0, -1.7F))));
    public static final Item NETHERITE_GAUNTLET = registerItem("netherite_gauntlet",
            new GauntletItem(ToolMaterials.NETHERITE, -1.6F, new Item.Settings().maxDamage(1140).attributeModifiers(GauntletItem.createAttributeModifiers(ToolMaterials.NETHERITE, 0, -1.6F))));
    public static final Item TEST_ITEM = registerItem("test_item", new AxeItem(ToolMaterials.DIAMOND, new Item.Settings().maxDamage(100)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Gauntlets.MOD_ID, name), item);
    }
    public static void registerModItems() {
        Gauntlets.LOGGER.info("[Gauntlets] REGISTERING ITEMS");

    }
}

