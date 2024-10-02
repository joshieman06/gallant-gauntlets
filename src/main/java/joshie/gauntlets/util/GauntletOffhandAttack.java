package joshie.gauntlets.util;

import java.util.List;

import joshie.gauntlets.access.PlayerAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;

import net.minecraft.entity.Entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GauntletOffhandAttack {

    public static float getOffhandAttackCooldownProgressPerTick(PlayerEntity playerEntity) {
        EntityAttributeInstance newInstance = new EntityAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED, spd -> playerEntity.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED));

        var ref = new Object() {
            float attackSpeed;
        };
        if (playerEntity.getMainHandStack().getComponents().get(DataComponentTypes.ATTRIBUTE_MODIFIERS) != null) {
            if (!playerEntity.getMainHandStack().getComponents().get(DataComponentTypes.ATTRIBUTE_MODIFIERS).modifiers().isEmpty()) {
                playerEntity.getMainHandStack().getComponents().get(DataComponentTypes.ATTRIBUTE_MODIFIERS).modifiers().forEach((attribute) -> {
                    if (attribute.modifier().id() == Identifier.ofVanilla("base_attack_speed")) {
                        ref.attackSpeed = (float) attribute.modifier().value();
                    }
                });
            }
        }

        return (float) (1.0 / ((newInstance.getValue() - 2) + ref.attackSpeed) * 20.0);
    }


    public static void offhandAttack(PlayerEntity playerEntity, Entity target, String part) {


        if (!target.isAttackable()) {
            return;
        }
        if (target.handleAttack(playerEntity)) {
            return;
        }

        target.timeUntilRegen = 0;
        assert playerEntity.getMainHandStack().getComponents() != null;
        var ref = new Object() {
            float f, g = (float) (playerEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
        };
        if (playerEntity.getMainHandStack().getComponents().get(DataComponentTypes.ATTRIBUTE_MODIFIERS) != null) {
            playerEntity.getMainHandStack().getComponents().get(DataComponentTypes.ATTRIBUTE_MODIFIERS).modifiers().forEach((attribute) -> {
                if (attribute.modifier().id().toString().equals("minecraft:base_attack_damage")) {
                    ref.f = (float) (playerEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) - attribute.modifier().value());
                }
            });
        }
        playerEntity.getOffHandStack().getComponents().get(DataComponentTypes.ATTRIBUTE_MODIFIERS).modifiers().forEach((attribute) -> {

            if (attribute.modifier().id().toString().equals("minecraft:base_attack_damage")) {
                ref.g = (float) (attribute.modifier().value());
            }
        });
        DamageSource damageSource = playerEntity.getDamageSources().playerAttack(playerEntity);
        float f = ref.f;

        float g = ref.g;
        float h = ((PlayerAccess) playerEntity).getAttackCooldownProgressDualOffhand(0.5f);
        g *= h;
        ((PlayerAccess) playerEntity).resetLastDualOffhandAttackTicks();
        if ((f *= 0.2f + h * h * 0.8f) > 0.0f || g > 0.0f) {
            ItemStack itemStack = playerEntity.getStackInHand(Hand.OFF_HAND);
            boolean bl = h > 0.9f;
            boolean bl2 = false;


        int i = 0;
        float g2 = (float)playerEntity.getAttributeValue(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);
        World var5 = playerEntity.getWorld();
        if (var5 instanceof ServerWorld serverWorld) {
            i =  (int)EnchantmentHelper.modifyKnockback(serverWorld, playerEntity.getOffHandStack(), target, damageSource, g2) ;
        } else {
            i = (int)f;
        }
            i += (bl2 ? 1.0F : 0.0F);
            if (playerEntity.isSprinting() && bl) {
                playerEntity.getWorld().playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, playerEntity.getSoundCategory(),
                        1.0f, 1.0f);
                ++i;
                bl2 = true;
            }
            boolean bl3 = bl && playerEntity.fallDistance > 0.0f && !playerEntity.isOnGround() && !playerEntity.isClimbing() && !playerEntity.isTouchingWater()
                    && !playerEntity.hasStatusEffect(StatusEffects.BLINDNESS) && !playerEntity.hasVehicle() && target instanceof LivingEntity;
            bl3 = bl3 && !playerEntity.isSprinting();
            f += g;
            if (bl3) {
                f *= 1.5f;
            }

            boolean bl42 = false;
            double d = playerEntity.horizontalSpeed - playerEntity.prevHorizontalSpeed;
            if (bl && !bl3 && !bl2 && playerEntity.isOnGround() && d < (double) playerEntity.getMovementSpeed() && itemStack.getItem() instanceof SwordItem) {
                bl42 = true;
            }
            float j = 0.0f;
            boolean bl5 = false;
            int k = 0;

            if (target instanceof LivingEntity) {
                j = ((LivingEntity) target).getHealth();
                if (k > 0 && !target.isOnFire()) {
                    bl5 = true;
                    target.setOnFireFor(1);
                }
            }
            Vec3d vec3d = target.getVelocity();

            boolean bl6 = target.damage(target.getDamageSources().playerAttack(playerEntity), f);
            if (bl6) {
                if (i > 0) {
                    if (target instanceof LivingEntity) {
                        ((LivingEntity) target).takeKnockback((float) i * 0.5f, MathHelper.sin(playerEntity.getYaw() * ((float) Math.PI / 180)),
                                -MathHelper.cos(playerEntity.getYaw() * ((float) Math.PI / 180)));
                    } else {
                        target.addVelocity(-MathHelper.sin(playerEntity.getYaw() * ((float) Math.PI / 180)) * (float) i * 0.5f, 0.1,
                                MathHelper.cos(playerEntity.getYaw() * ((float) Math.PI / 180)) * (float) i * 0.5f);
                    }
                    playerEntity.setVelocity(playerEntity.getVelocity().multiply(0.6, 1.0, 0.6));
                    playerEntity.setSprinting(false);
                }
                if (bl42) {
                    float l = 1.0f + 0 * f;
                    List<LivingEntity> list = playerEntity.getWorld().getNonSpectatingEntities(LivingEntity.class, target.getBoundingBox().expand(1.0, 0.25, 1.0));
                    for (LivingEntity livingEntity : list) {
                        if (livingEntity == playerEntity || livingEntity == target || playerEntity.isTeammate(livingEntity)
                                || livingEntity instanceof ArmorStandEntity && ((ArmorStandEntity) livingEntity).isMarker() || !(playerEntity.squaredDistanceTo(livingEntity) < 9.0))
                            continue;
                        livingEntity.takeKnockback(0.4f, MathHelper.sin(playerEntity.getYaw() * ((float) Math.PI / 180)), -MathHelper.cos(playerEntity.getYaw() * ((float) Math.PI / 180)));
                        livingEntity.damage(livingEntity.getDamageSources().playerAttack((PlayerEntity) (Object) playerEntity), l);
                    }
                    playerEntity.getWorld().playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, playerEntity.getSoundCategory(),
                            1.0f, 1.0f);

                    double posOne = -MathHelper.sin(playerEntity.getYaw() * ((float) Math.PI / 180));
                    double posTwo = MathHelper.cos(playerEntity.getYaw() * ((float) Math.PI / 180));
                }
                if (target instanceof ServerPlayerEntity && target.velocityModified) {
                    ((ServerPlayerEntity) target).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(target));
                    target.velocityModified = false;
                    target.setVelocity(vec3d);
                }
                if (bl3) {
                    playerEntity.getWorld().playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, playerEntity.getSoundCategory(),
                            1.0f, 1.0f);
                    ((PlayerEntity) (Object) playerEntity).addCritParticles(target);
                }
                if (!bl3 && !bl42) {
                    if (bl) {
                        playerEntity.getWorld().playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG,
                                playerEntity.getSoundCategory(), 1.0f, 1.0f);
                    } else {
                        playerEntity.getWorld().playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, playerEntity.getSoundCategory(),
                                1.0f, 1.0f);
                    }
                }
                if (!playerEntity.getOffHandStack().getComponents().get(DataComponentTypes.ENCHANTMENTS).isEmpty()) {
                    ((PlayerEntity) (Object) playerEntity).addEnchantedHitParticles(target);
                }
                playerEntity.onAttacking(target);
                if (target instanceof LivingEntity && !(target instanceof EnderDragonEntity)) {
                    EnchantmentHelper.onTargetDamaged(playerEntity.getServer().getWorld(playerEntity.getWorld().getRegistryKey()), (LivingEntity) target, target.getDamageSources().playerAttack(playerEntity), playerEntity.getOffHandStack());
                }

                ItemStack itemStack2 = playerEntity.getOffHandStack();
                Entity entity = target;
                if (target instanceof EnderDragonPart) {
                    entity = ((EnderDragonPart) target).owner;
                }
                if (!playerEntity.getWorld().isClient() && !itemStack2.isEmpty() && entity instanceof LivingEntity) {
                    itemStack2.postHit((LivingEntity) entity, (PlayerEntity) (Object) playerEntity);
                    if (itemStack2.isEmpty()) {
                        playerEntity.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
                    }
                }
                if (target instanceof LivingEntity) {
                    float m = j - ((LivingEntity) target).getHealth();
                    ((PlayerEntity) (Object) playerEntity).increaseStat(Stats.DAMAGE_DEALT, Math.round(m * 10.0f));
                    if (k > 0) {
                        target.setOnFireFor(k * 4);
                    }
                    if (playerEntity.getWorld() instanceof ServerWorld && m > 2.0f) {
                        int n = (int) ((double) m * 0.5);
                        ((ServerWorld) playerEntity.getWorld()).spawnParticles(ParticleTypes.DAMAGE_INDICATOR, target.getX(), target.getBodyY(0.5), target.getZ(), n, 0.1, 0.0, 0.1, 0.2);
                    }
                    target.timeUntilRegen = 0;
                }
                ((PlayerEntity) (Object) playerEntity).addExhaustion(0.1f);
            } else {
                playerEntity.getWorld().playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, playerEntity.getSoundCategory(),
                        1.0f, 1.0f);
                if (bl5) {
                    target.extinguish();
                }
            }

        }
    }

}