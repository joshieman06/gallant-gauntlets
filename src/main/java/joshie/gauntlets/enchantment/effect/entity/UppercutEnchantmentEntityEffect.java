package joshie.gauntlets.enchantment.effect.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record UppercutEnchantmentEntityEffect(EnchantmentLevelBasedValue amount)implements EnchantmentEntityEffect
    {

        public static final MapCodec<joshie.gauntlets.enchantment.effect.entity.UppercutEnchantmentEntityEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        EnchantmentLevelBasedValue.CODEC.fieldOf("amount").forGetter(joshie.gauntlets.enchantment.effect.entity.UppercutEnchantmentEntityEffect::amount)
                ).apply(instance, joshie.gauntlets.enchantment.effect.entity.UppercutEnchantmentEntityEffect::new)
        );

        @Override
        public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
            (user).addVelocity(0, 0.2 * amount.getValue(level), 0);
        }

        @Override
        public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
            return CODEC;
        }
    }


