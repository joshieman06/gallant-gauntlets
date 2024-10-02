package joshie.gauntlets.mixin.client;


import joshie.gauntlets.Gauntlets;
import joshie.gauntlets.item.GauntletItem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.item.*;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;

import net.minecraft.util.ActionResult;
import net.minecraft.util.UseAction;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import joshie.gauntlets.access.PlayerAccess;
import joshie.gauntlets.network.PlayerAttackPacket;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;


import javax.swing.text.html.parser.Entity;
import java.lang.reflect.Method;
import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    @Nullable
    public ClientPlayerEntity player;
    @Shadow
    @Nullable
    public ClientPlayerInteractionManager interactionManager;
    @Shadow
    @Nullable
    public HitResult crosshairTarget;
    @Shadow
    private int itemUseCooldown;
    @Shadow @Final private static Logger LOGGER;
    @Unique
    private int secondAttackCooldown;

    @Inject(method = "Lnet/minecraft/client/MinecraftClient;tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;handleInputEvents()V"))
    public void tickMixin(CallbackInfo info) {
        if (this.secondAttackCooldown > 0) {
            --this.secondAttackCooldown;
        }
    }

    @Inject(method = "doItemUse", at = @At(value = "HEAD"), cancellable = true)
    private void doItemUseMixin(CallbackInfo info) throws NoSuchMethodException {
        if (!FabricLoader.getInstance().isModLoaded("bettercombat")) {
            assert player != null;

            Item offHandItem = player.getOffHandStack().getItem();
            Item mainHandItem = player.getMainHandStack().getItem();
            if (player != null && !player.isSpectator() && (offHandItem instanceof GauntletItem) && mainHandItem.getComponents().get(DataComponentTypes.FOOD) == null && (mainHandItem.getUseAction(player.getMainHandStack()) == UseAction.NONE)) {
                if (this.secondAttackCooldown <= 0) {
                    if (this.crosshairTarget != null && !this.player.isRiding()) {

                        switch (this.crosshairTarget.getType()) {
                            case ENTITY:
                                assert this.interactionManager != null;
                                ActionResult result = this.interactionManager.interactEntity(player, ((EntityHitResult) this.crosshairTarget).getEntity(), Hand.MAIN_HAND);
                                if (result == ActionResult.SUCCESS) {
                                    this.player.swingHand(Hand.MAIN_HAND);
                                    this.itemUseCooldown = 4;

                                } else {
                                    Gauntlets.LOGGER.info(String.valueOf(((EntityHitResult) this.crosshairTarget).getEntity()));
                                    ((PlayerAccess) this.player).attackOffhand(((EntityHitResult) this.crosshairTarget).getEntity(), "none");
                                    if (((EntityHitResult) this.crosshairTarget).getEntity() instanceof EnderDragonPart) {
                                        ClientPlayNetworking.send(new PlayerAttackPacket(this.player.getUuid(), ((EnderDragonPart)((EntityHitResult) this.crosshairTarget).getEntity()).owner.getUuid(), ((EnderDragonPart)((EntityHitResult) this.crosshairTarget).getEntity()).name));
                                    } else {
                                        ClientPlayNetworking.send(new PlayerAttackPacket(this.player.getUuid(), ((EntityHitResult) this.crosshairTarget).getEntity().getUuid(), "none"));
                                    }
                                    this.itemUseCooldown = 99999999;
                                    this.player.swingHand(Hand.OFF_HAND);
                                }
                                break;
                            case BLOCK:
                                BlockHitResult blockHitResult = (BlockHitResult) this.crosshairTarget;
                                BlockPos blockPos = blockHitResult.getBlockPos();
                                if (!player.getWorld().getBlockState(blockPos).isAir()) {
                                    assert this.interactionManager != null;
                                    ActionResult result2 = this.interactionManager.interactBlock(player, Hand.MAIN_HAND, blockHitResult);
                                    if (result2.shouldSwingHand()) {
                                        this.itemUseCooldown = 4;
                                        this.player.swingHand(Hand.MAIN_HAND);
                                    }
                                    break;
                                }
                            case MISS:
                                assert this.interactionManager != null;
                                if (this.interactionManager.hasLimitedAttackSpeed()) {
                                    this.secondAttackCooldown = 10;
                                }
                                ((PlayerAccess) player).resetLastDualOffhandAttackTicks();
                                this.itemUseCooldown = 999999999;
                                this.player.swingHand(Hand.OFF_HAND);
                        }

                        info.cancel();
                    }
                } else {
                    info.cancel();
                }
            }
        }
    }
}