package net.samipla.slim_pickings;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.samipla.slim_pickings.network.ModMessages;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod(SlimPickings.MODID)
public class SlimPickings {
    public static final String MODID = "slim_pickings";

    private static final Map<UUID, Boolean> PLAYER_KEY_STATES = new ConcurrentHashMap<>();

    public SlimPickings() {
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC);

        ModMessages.register();
    }

    public static void setPlayerKey(UUID uuid, boolean held) {
        PLAYER_KEY_STATES.put(uuid, held);
    }

    public static boolean isKeyHeld(UUID uuid) {
        return PLAYER_KEY_STATES.getOrDefault(uuid, false);
    }

    @SubscribeEvent
    public void onEntityPickup(EntityItemPickupEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (Config.BIG_PICKINGS.get()) {
                return;
            }
            if (!isKeyHeld(player.getUUID())) {
                event.setCanceled(true);
            }
        }
    }
}