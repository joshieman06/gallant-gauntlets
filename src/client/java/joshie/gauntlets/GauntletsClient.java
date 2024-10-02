package joshie.gauntlets;

import joshie.gauntlets.enchantment.effect.entity.CrushEnchantmentEntityEffect;
import joshie.gauntlets.enchantment.effect.entity.UppercutEnchantmentEntityEffect;
import joshie.gauntlets.network.PlayerAttackPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static joshie.gauntlets.Gauntlets.MOD_ID;

public class GauntletsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(MOD_ID, "crushing_effect"), CrushEnchantmentEntityEffect.CODEC);
		Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(MOD_ID, "uppercut_effect"), UppercutEnchantmentEntityEffect.CODEC);
	}
}