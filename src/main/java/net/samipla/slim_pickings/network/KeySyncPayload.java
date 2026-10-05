package net.samipla.slim_pickings.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.samipla.slim_pickings.SlimPickings;

import java.util.function.Supplier;

public class KeySyncPayload {
    private final boolean isHeld;

    public KeySyncPayload(boolean isHeld) {
        this.isHeld = isHeld;
    }

    // Decoder constructor reading from FriendlyByteBuf
    public KeySyncPayload(FriendlyByteBuf buf) {
        this.isHeld = buf.readBoolean();
    }

    // Encoder writing to FriendlyByteBuf
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(isHeld);
    }

    // Packet execution handler logic on the server side
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                SlimPickings.setPlayerKey(player.getUUID(), isHeld);
            }
        });
        context.setPacketHandled(true);
        return true;
    }

    public boolean isHeld() {
        return isHeld;
    }
}