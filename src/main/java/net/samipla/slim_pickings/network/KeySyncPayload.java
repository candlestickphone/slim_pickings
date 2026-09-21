package net.samipla.slim_pickings.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record KeySyncPayload(boolean isHeld) implements CustomPacketPayload {
    public static final Type<KeySyncPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("slim_pickings", "key_sync"));
    public static final StreamCodec<ByteBuf, KeySyncPayload> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, KeySyncPayload::isHeld, KeySyncPayload::new);

    @Override 
    public Type<? extends CustomPacketPayload> type() { 
        return TYPE; 
    }
}