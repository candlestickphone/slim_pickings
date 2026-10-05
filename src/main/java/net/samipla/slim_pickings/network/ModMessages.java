package net.samipla.slim_pickings.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.samipla.slim_pickings.SlimPickings;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int nextId() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(SlimPickings.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(KeySyncPayload.class, nextId(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(KeySyncPayload::toBytes)
                .decoder(KeySyncPayload::new)
                .consumerNetworkThread(KeySyncPayload::handle)
                .add();

        net.messageBuilder(ItemInteractPayload.class, nextId(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(ItemInteractPayload::toBytes)
                .decoder(ItemInteractPayload::new)
                .consumerNetworkThread(ItemInteractPayload::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
}