package net.samipla.slim_pickings.server;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;

public class ItemInteractionUtils {
    public static InteractionResult handleInteraction(ItemEntity itemEntity, Player player, InteractionHand hand, int pickupDelay) {
        if (itemEntity.level().isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (itemEntity.isRemoved() || pickupDelay > 0) {
            return InteractionResult.PASS;
        }

        double reach = player.getAttributeValue(ForgeMod.ENTITY_REACH.get()) + 1.5D;
        if (player.distanceToSqr(itemEntity) > reach * reach) {
            return InteractionResult.PASS;
        }

        ItemStack itemStack = itemEntity.getItem();
        ItemStack copy = itemStack.copy();

        if (player.getInventory().add(itemStack)) {
            int pickedUpCount = copy.getCount() - itemStack.getCount();

            player.take(itemEntity, pickedUpCount);

            if (itemStack.isEmpty()) {
                itemEntity.discard();
                itemStack.setCount(pickedUpCount);
            }

            player.awardStat(Stats.ITEM_PICKED_UP.get(copy.getItem()), pickedUpCount);
            player.onItemPickup(itemEntity);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}