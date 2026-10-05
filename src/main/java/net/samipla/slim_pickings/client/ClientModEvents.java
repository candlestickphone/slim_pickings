package net.samipla.slim_pickings.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.samipla.slim_pickings.Config;
import net.samipla.slim_pickings.SlimPickings;
import net.samipla.slim_pickings.client.utils.PickupTargetUtils;
import net.samipla.slim_pickings.network.ItemInteractPayload;
import net.samipla.slim_pickings.network.KeySyncPayload;
import net.samipla.slim_pickings.network.ModMessages;

import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = SlimPickings.MODID, value = Dist.CLIENT)
public class ClientModEvents {

    public static final KeyMapping PICKUP_KEY = new KeyMapping(
            "slim_pickings.key.pickup", 
            InputConstants.Type.KEYSYM, 
            GLFW.GLFW_KEY_LEFT_SHIFT, 
            KeyMapping.CATEGORY_GAMEPLAY
    );

    private static boolean lastPressed;
    private static boolean toggled;
    private static boolean picking;

    @Mod.EventBusSubscriber(modid = SlimPickings.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBus {
        @SubscribeEvent
        public static void registerBindings(RegisterKeyMappingsEvent event) {
            event.register(PICKUP_KEY);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        boolean state = false;
        if (!Config.BIG_PICKINGS.get()) {
            boolean pressed = PICKUP_KEY.isDown();
            state = !Config.KEYBIND_MODE.get() ? pressed : (pressed && !lastPressed ? (toggled = !toggled) : toggled);
            lastPressed = pressed;
        } else {
            lastPressed = PICKUP_KEY.isDown();
            toggled = false;
        }

        if (state != picking) {
            picking = state;
            ModMessages.sendToServer(new KeySyncPayload(picking));
        }
    }

    @SubscribeEvent
    public static void onInteractionKeyTriggered(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (event.isAttack() && mc.hitResult instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof ItemEntity) {
            event.setCanceled(true);
            mc.player.swing(mc.player.getUsedItemHand());
            return;
        }

        if (event.isUseItem()) {
            ItemEntity targetItem = PickupTargetUtils.getTargetedItemEntity(mc);
            if (targetItem != null) {
                event.setCanceled(true);
                ModMessages.sendToServer(new ItemInteractPayload(targetItem.getId()));
                mc.player.swing(mc.player.getUsedItemHand());
            }
        }
    }

    public static boolean isPicking() {
        return Config.BIG_PICKINGS.get() || picking;
    }
}