package net.samipla.slim_pickings.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.samipla.slim_pickings.SlimPickings;

public record ItemInteractPayload(int entityId) implements CustomPacketPayload {
    public static final Type<ItemInteractPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(SlimPickings.MODID, "item_interact"));

    public static final StreamCodec<ByteBuf, ItemInteractPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ItemInteractPayload::entityId,
            ItemInteractPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}