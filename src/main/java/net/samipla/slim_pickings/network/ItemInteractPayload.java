package net.samipla.slim_pickings.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkEvent;
import net.samipla.slim_pickings.server.ItemInteractionUtils;

import java.util.function.Supplier;

public class ItemInteractPayload {
    private final int entityId;

    public ItemInteractPayload(int entityId) {
        this.entityId = entityId;
    }

    public ItemInteractPayload(FriendlyByteBuf buf) {
        this.entityId = buf.readVarInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(entityId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            Entity entity = player.level().getEntity(entityId);
            if (entity instanceof ItemEntity itemEntity) {
                double reach = player.getAttributeValue(ForgeMod.ENTITY_REACH.get()) + 1.5D;
                if (player.distanceToSqr(itemEntity) <= reach * reach) {
                    ItemInteractionUtils.handleInteraction(itemEntity, player, InteractionHand.MAIN_HAND, 0);
                }
            }
        });
        context.setPacketHandled(true);
        return true;
    }

    public int entityId() {
        return entityId;
    }
}