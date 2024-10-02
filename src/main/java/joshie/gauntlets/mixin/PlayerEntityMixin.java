package joshie.gauntlets.mixin;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import joshie.gauntlets.access.PlayerAccess;
import joshie.gauntlets.util.GauntletOffhandAttack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

@Mixin(value = PlayerEntity.class, priority = 1500)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerAccess {

    @Unique
    private int lastAttackedOffhandTicks;

    public PlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(EntityType.PLAYER, world);
    }

    @Inject(method = "Lnet/minecraft/entity/player/PlayerEntity;tick()V", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerEntity;lastAttackedTicks:I", ordinal = 0))
    private void tickMixin(CallbackInfo info) {
        lastAttackedOffhandTicks++;
    }

    public float getOffhandAttackCooldownProgressPerTick() {
        return GauntletOffhandAttack.getOffhandAttackCooldownProgressPerTick((PlayerEntity) (Object) this);
    }

    @Override
    public void attackOffhand(Entity target, String part) {
        GauntletOffhandAttack.offhandAttack((PlayerEntity) (Object) this, target, part);
    }

    @Override
    public void resetLastDualOffhandAttackTicks() {
        this.lastAttackedOffhandTicks = 0;
    }

    @Override
    public float getAttackCooldownProgressDualOffhand(float baseTime) {
        return MathHelper.clamp(((float) this.lastAttackedOffhandTicks + baseTime) / this.getOffhandAttackCooldownProgressPerTick(), 0.0F, 1.0F);
    }


}