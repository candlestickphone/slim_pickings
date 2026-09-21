package net.samipla.slim_pickings;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod(SlimPickings.MODID)
public class SlimPickings {
    public static final String MODID = "slim_pickings";

    private static final Map<UUID, Boolean> PLAYER_KEY_STATES = new ConcurrentHashMap<>();

    public SlimPickings(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }
    }

    public static void setPlayerKey(UUID uuid, boolean held) {
        PLAYER_KEY_STATES.put(uuid, held);
    }

    public static boolean isKeyHeld(UUID uuid) {
        return PLAYER_KEY_STATES.getOrDefault(uuid, false);
    }

    @SubscribeEvent
    public void onEntityPickupPre(ItemEntityPickupEvent.Pre event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            if (Config.BIG_PICKINGS.get()) {
                return;
            }
            if (!isKeyHeld(player.getUUID())) {
                event.setCanPickup(TriState.FALSE);
            }
        }
    }
}