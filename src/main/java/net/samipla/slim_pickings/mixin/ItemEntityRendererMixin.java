package net.samipla.slim_pickings.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.samipla.slim_pickings.client.ClientModEvents;
import net.samipla.slim_pickings.client.utils.ItemEntityRendererUtils;
import net.samipla.slim_pickings.client.utils.PickupTargetUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(ItemEntityRenderer.class)
public class ItemEntityRendererMixin {

    @Inject(
        method = "render(Lnet/minecraft/world/entity/item/ItemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", 
        at = @At("HEAD")
    )
    private void slim_pickings$onRenderHead(ItemEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        float spin = ((float) entity.getAge() + partialTicks) / 20.0F + entity.bobOffs;

        Minecraft mc = Minecraft.getInstance();
        boolean targeted = PickupTargetUtils.getTargetedItemEntity(mc) == entity;
        boolean isTargetedOrPicking = ClientModEvents.isPicking() || targeted;

        float targetRot = spin;
        Player player = mc.player;
        if (isTargetedOrPicking && player != null) {
            double x = player.getX() - entity.getX();
            double z = player.getZ() - entity.getZ();
            targetRot = (float) -Mth.atan2(z, x) + (float) (Math.PI / 2);
        }

        float finalRot = ItemEntityRendererUtils.getTargetRotation(entity, targetRot, isTargetedOrPicking, partialTicks);

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotation(finalRot - spin));
    }

    @Inject(
        method = "render(Lnet/minecraft/world/entity/item/ItemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", 
        at = @At("RETURN")
    )
    private void slim_pickings$onRenderReturn(ItemEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        poseStack.popPose();
    }
}