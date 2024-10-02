//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package joshie.gauntlets.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import joshie.gauntlets.registry.tags.BlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;


import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.item.SwordItem;


public class GauntletItem extends ToolItem {




    private static final Map<ToolMaterials, RegistryEntry<ArmorMaterial>> toolToArmorMaterialMap = new HashMap<>();
    static {
        toolToArmorMaterialMap.put(ToolMaterials.IRON, ArmorMaterials.IRON);
        toolToArmorMaterialMap.put(ToolMaterials.DIAMOND, ArmorMaterials.DIAMOND);
        toolToArmorMaterialMap.put(ToolMaterials.GOLD, ArmorMaterials.GOLD);
        toolToArmorMaterialMap.put(ToolMaterials.NETHERITE, ArmorMaterials.NETHERITE);
    }



    public GauntletItem(ToolMaterial toolMaterial, float attackSpeed, Item.Settings settings) {
        super(toolMaterial, settings);
    }



    public static AttributeModifiersComponent createAttributeModifiers(ToolMaterial material, int baseAttackDamage, float attackSpeed) {
        ArmorMaterial armorMaterial = toolToArmorMaterialMap.get(material).value();
        if (material == ToolMaterials.GOLD) {
            baseAttackDamage = (int)material.getAttackDamage()+ 2;
        } else {
            baseAttackDamage = (int)material.getAttackDamage();
        }
        AttributeModifiersComponent.Builder builder = AttributeModifiersComponent.builder();
        AttributeModifierSlot attributeModifierSlot = AttributeModifierSlot.MAINHAND;
        builder.add(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(Identifier.ofVanilla("armor.mainhand"), armorMaterial.getProtection(ArmorItem.Type.BOOTS), Operation.ADD_VALUE), attributeModifierSlot);
        builder.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, baseAttackDamage, Operation.ADD_VALUE), attributeModifierSlot);
        builder.add(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, Operation.ADD_VALUE), attributeModifierSlot);

        attributeModifierSlot = AttributeModifierSlot.OFFHAND;
        builder.add(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(Identifier.ofVanilla("armor.offhand"), armorMaterial.getProtection(ArmorItem.Type.BOOTS), Operation.ADD_VALUE), attributeModifierSlot);
        return builder.build();
    }

    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return !miner.isCreative();
    }

    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        if (state.isOf(Blocks.COBWEB)) {
            return 15.0F;
        } else {
            return state.isIn(BlockTags.GAUNTLET_EFFICIENT) ? 1.5F : 1.0F;
        }
    }

    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
        return true;
    }

    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (state.getHardness(world, pos) != 0.0F) {
            stack.damage(2, miner, EquipmentSlot.MAINHAND);
        }

        return true;
    }

    public boolean isSuitableFor(BlockState state) {
        return state.isIn(BlockTags.GAUNTLET_EFFICIENT);
    }



}
