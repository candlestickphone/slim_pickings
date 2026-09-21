package net.samipla.slim_pickings.mixin;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.samipla.slim_pickings.Config;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OptionInstance.class)
public class OptionInstanceMixin {

    @Shadow @Final Component caption;

    @Inject(method = "createButton", at = @At("RETURN"))
    private void slim_pickings$disableButtonIfNeeded(CallbackInfoReturnable<AbstractWidget> cir) {
        if (this.caption.getContents() instanceof TranslatableContents translatable 
                && translatable.getKey().equals("slim_pickings.gui.controls_screen.pickup") 
                && Config.BIG_PICKINGS.get()) {
            
            AbstractWidget widget = cir.getReturnValue();
            if (widget != null) {
                widget.active = false;
            }
        }
    }
}