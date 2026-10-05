package net.samipla.slim_pickings.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import java.util.Optional;

public class PickupTargetUtils {
    public static ItemEntity getTargetedItemEntity(Minecraft mc) {
        if (mc.player == null || mc.level == null) return null;

        double reach = mc.player.getAttributeValue(ForgeMod.ENTITY_REACH.get()) + 1.5D;
        Vec3 start = mc.player.getEyePosition(1.0F);
        Vec3 viewVector = mc.player.getViewVector(1.0F);
        Vec3 end = start.add(viewVector.scale(reach));

        AABB searchBox = mc.player.getBoundingBox().expandTowards(viewVector.scale(reach)).inflate(1.0D);

        ItemEntity closestExactEntity = null;
        double closestExactDistSqr = Double.MAX_VALUE;

        ItemEntity closestInflatedEntity = null;
        double closestInflatedDistSqr = Double.MAX_VALUE;

        for (Entity entity : mc.level.getEntities(mc.player, searchBox, e -> e instanceof ItemEntity)) {
            ItemEntity item = (ItemEntity) entity;

            Optional<Vec3> exactClip = item.getBoundingBox().clip(start, end);
            if (exactClip.isPresent()) {
                double distSqr = start.distanceToSqr(exactClip.get());
                if (distSqr <= reach * reach && distSqr < closestExactDistSqr) {
                    closestExactDistSqr = distSqr;
                    closestExactEntity = item;
                }
            }

            AABB inflatedBox = item.getBoundingBox().inflate(0.3D);
            Optional<Vec3> inflatedClip = inflatedBox.clip(start, end);
            if (inflatedClip.isPresent()) {
                double distSqr = start.distanceToSqr(inflatedClip.get());
                if (distSqr <= reach * reach && distSqr < closestInflatedDistSqr) {
                    closestInflatedDistSqr = distSqr;
                    closestInflatedEntity = item;
                }
            }
        }

        return closestExactEntity != null ? closestExactEntity : closestInflatedEntity;
    }
}