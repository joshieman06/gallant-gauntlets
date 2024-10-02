package joshie.gauntlets.mixin.client;

import joshie.gauntlets.Gauntlets;
import joshie.gauntlets.item.ModItems;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useGoldGauntletModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (stack.isOf(ModItems.GOLD_GAUNTLET) && ((renderMode != ModelTransformationMode.GUI) && (renderMode != ModelTransformationMode.FIXED) && (renderMode != ModelTransformationMode.GROUND))) {
            return ((ItemRendererAccessor) this).mccourse$getModels().getModelManager().getModel(new ModelIdentifier(Identifier.of(Gauntlets.MOD_ID, "gold_gauntlet_3d"), "inventory"));
        }
        return value;
    }
    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useIronGauntletModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (stack.isOf(ModItems.IRON_GAUNTLET) && ((renderMode != ModelTransformationMode.GUI) && (renderMode != ModelTransformationMode.FIXED) && (renderMode != ModelTransformationMode.GROUND))) {
            return ((ItemRendererAccessor) this).mccourse$getModels().getModelManager().getModel(new ModelIdentifier(Identifier.of(Gauntlets.MOD_ID, "iron_gauntlet_3d"), "inventory"));
        }
        return value;
    }
    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useDiamondGauntletModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (stack.isOf(ModItems.DIAMOND_GAUNTLET) && ((renderMode != ModelTransformationMode.GUI) && (renderMode != ModelTransformationMode.FIXED) && (renderMode != ModelTransformationMode.GROUND))) {
            return ((ItemRendererAccessor) this).mccourse$getModels().getModelManager().getModel(new ModelIdentifier(Identifier.of(Gauntlets.MOD_ID, "diamond_gauntlet_3d"), "inventory"));
        }
        return value;
    }
    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useNetheriteGauntletModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (stack.isOf(ModItems.NETHERITE_GAUNTLET) && ((renderMode != ModelTransformationMode.GUI) && (renderMode != ModelTransformationMode.FIXED) && (renderMode != ModelTransformationMode.GROUND))) {
            return ((ItemRendererAccessor) this).mccourse$getModels().getModelManager().getModel(new ModelIdentifier(Identifier.of(Gauntlets.MOD_ID, "netherite_gauntlet_3d"), "inventory"));
        }
        return value;
    }

}