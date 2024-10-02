package joshie.gauntlets.mixin.client;

import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.At;

import net.fabricmc.api.Environment;
import joshie.gauntlets.access.PlayerAccess;
import net.fabricmc.api.EnvType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
    @Shadow
    private float equipProgressOffHand;
    @Shadow
    private float equipProgressMainHand;
    @Shadow
    private ItemStack mainHand;
    @Shadow
    private float prevEquipProgressMainHand;
    @Shadow
    private float prevEquipProgressOffHand;

    @Shadow
    @Final
    @Mutable
    private MinecraftClient client;

    @Shadow
    private ItemStack offHand;

    private float equipOffhand;
    private boolean isOffhandAttack;

    public HeldItemRendererMixin(MinecraftClient client) {
        this.client = client;
    }

    @Inject(method = "updateHeldItems", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F", ordinal = 3, shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void updateHeldItemsMixin(CallbackInfo info, ClientPlayerEntity clientPlayerEntity, ItemStack itemStack, ItemStack itemStack2) {
        float o = ((PlayerAccess) clientPlayerEntity).getAttackCooldownProgressDualOffhand(1.0F);
        if (o < 0.1F)
            this.isOffhandAttack = true;
        // this.equipOffhand = 1.0F;
        if (this.isOffhandAttack) {
            if (this.equipProgressMainHand >= 1.0F) {
                this.isOffhandAttack = false;
            }
            this.equipOffhand += MathHelper.clamp((this.offHand == itemStack2 ? o * o * o : 0.0F) - this.equipOffhand, -0.4F, 0.4F);
            if (!clientPlayerEntity.handSwinging) {
                this.equipProgressOffHand = this.equipOffhand;
            }
        }
    }

    @Inject(method = "updateHeldItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getAttackCooldownProgress(F)F"))
    public void updateHeldMainhandMixin(CallbackInfo info) {
        float o = ((PlayerAccess) client.player).getAttackCooldownProgressDualOffhand(1.0F);
        if (o < 0.9F && o > 0.15F) {
            this.offHand = new ItemStack(Items.AIR);
        }
    }
}