package net.samipla.slim_pickings.mixin;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.MouseSettingsScreen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.samipla.slim_pickings.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@OnlyIn(Dist.CLIENT)
@Mixin(ControlsScreen.class)
public abstract class ControlsScreenMixin extends net.minecraft.client.gui.screens.OptionsSubScreen {

    public ControlsScreenMixin(net.minecraft.client.gui.screens.Screen screen, net.minecraft.client.Options options) {
        super(screen, options, Component.translatable("controls.title"));
    }
    
    @Overwrite
    protected void init() {
        super.init();
        int i = this.width / 2 - 155;
        int j = i + 160;
        int k = this.height / 6;
        
        this.addRenderableWidget(
            Button.builder(Component.translatable("options.mouse_settings"), button -> this.minecraft.setScreen(new MouseSettingsScreen(this, this.options)))
               .bounds(i, k, 150, 20)
               .build()
        );
        this.addRenderableWidget(
            Button.builder(Component.translatable("controls.keybinds"), button -> this.minecraft.setScreen(new KeyBindsScreen(this, this.options)))
               .bounds(j, k, 150, 20)
               .build()
        );
        k += 24;
        this.addRenderableWidget(this.options.toggleCrouch().createButton(this.options, i, k, 150));
        this.addRenderableWidget(this.options.toggleSprint().createButton(this.options, j, k, 150));
        k += 24;
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

        this.addRenderableWidget(pickupModeOption.createButton(this.options, i, k, 150));
        this.addRenderableWidget(this.options.autoJump().createButton(this.options, j, k, 150));
        k += 24;
        this.addRenderableWidget(this.options.operatorItemsTab().createButton(this.options, i, k, 150));
        k += 24;
        this.addRenderableWidget(
            Button.builder(CommonComponents.GUI_DONE, button -> this.minecraft.setScreen(this.lastScreen))
               .bounds(this.width / 2 - 100, k, 200, 20)
               .build()
        );
    }
}