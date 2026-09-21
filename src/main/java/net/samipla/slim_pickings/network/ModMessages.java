package net.samipla.slim_pickings.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.samipla.slim_pickings.SlimPickings;
import net.samipla.slim_pickings.server.ItemInteractionUtils;

@EventBusSubscriber(modid = SlimPickings.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModMessages {
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1.0");

        registrar.playToServer(
            KeySyncPayload.TYPE, 
            KeySyncPayload.STREAM_CODEC, 
            (payload, context) -> context.enqueueWork(() -> { 
                if (context.player() instanceof ServerPlayer player) {
                    SlimPickings.setPlayerKey(player.getUUID(), payload.isHeld()); 
                }
            })
        );

        registrar.playToServer(
            ItemInteractPayload.TYPE,
            ItemInteractPayload.STREAM_CODEC,
            (payload, context) -> context.enqueueWork(() -> {
                if (context.player() instanceof ServerPlayer player) {
                    Entity entity = player.level().getEntity(payload.entityId());
                    if (entity instanceof ItemEntity itemEntity) {
                        ItemInteractionUtils.handleInteraction(itemEntity, player, InteractionHand.MAIN_HAND, 0);
                    }
                }
            })
        );
    }
}