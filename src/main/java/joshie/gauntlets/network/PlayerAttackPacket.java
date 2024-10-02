package joshie.gauntlets.network;

import io.netty.buffer.Unpooled;
import joshie.gauntlets.Gauntlets;
import joshie.gauntlets.access.PlayerAccess;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import joshie.gauntlets.item.GauntletItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.Packet;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.util.hit.EntityHitResult;

import java.util.UUID;



public class PlayerAttackPacket implements CustomPayload {

    public static final CustomPayload.Id<PlayerAttackPacket> PACKET_ID = new CustomPayload.Id<>(Identifier.of("gauntlets", "offhand_attack_entity"));
    public static final PacketCodec<RegistryByteBuf, PlayerAttackPacket> PACKET_CODEC = PacketCodec.of(PlayerAttackPacket::encode, PlayerAttackPacket::new).cast();

    public PlayerAttackPacket(UUID firstUuid, UUID secondUuid, String part) {
        this.firstUuid = firstUuid;
        this.secondUuid = secondUuid;
        this.part = part;
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    private final UUID firstUuid;
    private final UUID secondUuid;
    private final String part;

    public UUID getFirstUuid() {
        return firstUuid;
    }

    public UUID getSecondUuid() {
        return secondUuid;
    }

    public String getPart() {
        return part;
    }

    public PlayerAttackPacket(RegistryByteBuf buf) {
        this.firstUuid = buf.readUuid();
        this.secondUuid = buf.readUuid();
        this.part = buf.readString();
    }

    public void encode(RegistryByteBuf buf) {
        buf.writeUuid(firstUuid);
        buf.writeUuid(secondUuid);
        buf.writeString(part);
    }

    public static void registerServerReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID, (payload, context) -> {
            context.player().getServer().execute(() -> {
                // Handle the packet data on the server side
                UUID firstUuid = payload.getFirstUuid();
                UUID secondUuid = payload.getSecondUuid();
                String part = payload.getPart();
                // Your handling logic here
                PlayerEntity attacker = (PlayerEntity) context.player().getServerWorld().getEntity(firstUuid);
                Entity victim = context.player().getServerWorld().getEntity(secondUuid);
                ((PlayerAccess) attacker).attackOffhand(victim, part);
            });
        });
    }


}
//public class PlayerAttackPacket {
//
//    public static final Identifier ATTACK_PACKET = new Identifier();
//
//
//    public static void init() {
//        ServerPlayNetworking.registerGlobalReceiver(ATTACK_PACKET, (server, player, handler, buffer, sender) -> {
//            int entityId = buffer.readVarInt();
//            server.execute(() -> {
//                player.updateLastActionTime();
//                if (player.getWorld().getEntityById(entityId) != null)
//                    ((PlayerAccess) player).attackOffhand(player.getWorld().getEntityById(entityId));
//            });
//
//        });
//
//    }
//
//}