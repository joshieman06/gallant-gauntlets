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
import net.minecraft.enchantment.effect.entity.PlaySoundEnchantmentEffect;

public record CrushEnchantmentEntityEffect(EnchantmentLevelBasedValue amount)implements EnchantmentEntityEffect
{

    public static final MapCodec<CrushEnchantmentEntityEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    EnchantmentLevelBasedValue.CODEC.fieldOf("amount").forGetter(CrushEnchantmentEntityEffect::amount)
            ).apply(instance, CrushEnchantmentEntityEffect::new)
    );

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d pos) {
        Iterable<ItemStack> armorItems = ((LivingEntity)user).getArmorItems();
        for (ItemStack armorItem : armorItems) {
            if (!armorItem.isEmpty()) {
                int damageAmount = (int) (amount.getValue(level)); // Customize as needed
                assert context.owner() != null;
                armorItem.damage(damageAmount, context.owner(), EquipmentSlot.MAINHAND);
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
