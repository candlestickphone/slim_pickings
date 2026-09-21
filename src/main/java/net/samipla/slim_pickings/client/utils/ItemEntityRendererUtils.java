package net.samipla.slim_pickings.client.utils;

import net.minecraft.world.entity.item.ItemEntity;
import net.samipla.slim_pickings.Config;

import java.util.Map;
import java.util.WeakHashMap;

public class ItemEntityRendererUtils {
    public static class AnimState {
        public float currentRot;
        public float lastVanillaSpin;
        public boolean initialized = false;
    }

    private static final Map<ItemEntity, AnimState> STATES = new WeakHashMap<>();

    public static float getTargetRotation(ItemEntity entity, float targetRot, boolean isTargeted, float partialTicks) {
        float vanillaSpin = ((float) entity.getAge() + partialTicks) / 20.0F + entity.bobOffs;

        if (!Config.LOOKING_ITEMS.get()) {
            return vanillaSpin;
        }

        AnimState state = STATES.computeIfAbsent(entity, e -> {
            AnimState s = new AnimState();
            s.currentRot = vanillaSpin;
            s.lastVanillaSpin = vanillaSpin;
            s.initialized = true;
            return s;
        });

        if (!state.initialized) {
            state.currentRot = vanillaSpin;
            state.lastVanillaSpin = vanillaSpin;
            state.initialized = true;
        }

        float vanillaDelta = vanillaSpin - state.lastVanillaSpin;
        state.lastVanillaSpin = vanillaSpin;

        if (isTargeted) {
            float diff = targetRot - state.currentRot;
            while (diff >= (float) Math.PI) {
                diff -= (float) (Math.PI * 2);
            }
            while (diff < (float) -Math.PI) {
                diff += (float) (Math.PI * 2);
            }

            state.currentRot += diff * 0.15F;
        } else {
            state.currentRot += vanillaDelta;
        }

        return state.currentRot;
    }
}