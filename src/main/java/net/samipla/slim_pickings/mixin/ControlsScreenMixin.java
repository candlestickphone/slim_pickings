package net.samipla.slim_pickings.mixin;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.network.chat.Component;
import net.samipla.slim_pickings.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ControlsScreen.class)
public class ControlsScreenMixin {

    @Inject(method = "options", at = @At("RETURN"), cancellable = true)
    private static void slim_pickings$addCustomOptions(Options options, CallbackInfoReturnable<OptionInstance<?>[]> cir) {
        OptionInstance<?>[] vArr = cir.getReturnValue();

        boolean disabled = Config.BIG_PICKINGS.get();

        OptionInstance<Boolean> pickupModeOption = new OptionInstance<>(
                "slim_pickings.gui.controls_screen.pickup",
                disabled 
                    ? OptionInstance.cachedConstantTooltip(Component.translatable("slim_pickings.gui.controls_screen.pickup.tooltip.big_pickings")) 
                    : OptionInstance.noTooltip(),
                (component, value) -> disabled 
                    ? Component.literal("On") 
                    : Component.literal(value ? "Toggle" : "Hold"),
                OptionInstance.BOOLEAN_VALUES,
                Config.KEYBIND_MODE.get(),
                newValue -> {
                    if (!disabled) {
                        Config.KEYBIND_MODE.set(newValue);
                        Config.KEYBIND_MODE.save();
                    }
                }
        );

        OptionInstance<?>[] newArr = new OptionInstance<?>[vArr.length + 1];
        int insert = 2;

        System.arraycopy(vArr, 0, newArr, 0, insert);
        newArr[insert] = pickupModeOption;
        System.arraycopy(vArr, insert, newArr, insert + 1, vArr.length - insert);

        cir.setReturnValue(newArr);
    }
}