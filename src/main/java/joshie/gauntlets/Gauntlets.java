package joshie.gauntlets;



import joshie.gauntlets.enchantment.effect.entity.CrushEnchantmentEntityEffect;
import joshie.gauntlets.enchantment.effect.entity.UppercutEnchantmentEntityEffect;
import joshie.gauntlets.item.ModItemGroups;
import joshie.gauntlets.item.ModItems;
//import joshie.gauntlets.network.PlayerAttackPacket;
import joshie.gauntlets.network.PlayerAttackPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import static joshie.gauntlets.item.ModItems.registerModItems;

public class Gauntlets implements ModInitializer {
    public static final String MOD_ID = "gauntlets";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
//    public static Enchantment FORTITUDE = new FortitudeEnchantment();


    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(PlayerAttackPacket.PACKET_ID, PlayerAttackPacket.PACKET_CODEC);
        PlayerAttackPacket.registerServerReceiver();
        ModItemGroups.registerItemGroups();
        registerModItems();
        Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(MOD_ID, "crushing_effect"), CrushEnchantmentEntityEffect.CODEC);
        Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(MOD_ID, "uppercut_effect"), UppercutEnchantmentEntityEffect.CODEC);
//        Registry.register(Registries.ENCHANTMENT, new Identifier("gauntlets", "fortitude"), FORTITUDE);
//        Registry.register(Registries.ENCHANTMENT, Identifier.of("gauntlets", "crushing"), CRUSHING);
    }
}
