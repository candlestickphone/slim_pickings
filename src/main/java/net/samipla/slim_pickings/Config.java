package net.samipla.slim_pickings;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue KEYBIND_MODE = BUILDER
            .comment("If true, changes controls from hold to toggle.")
            .define("keybind_mode", false);

    public static final ModConfigSpec.BooleanValue BIG_PICKINGS = BUILDER
            .comment("If true, disables the keybind and always allows picking up items, similar to vanilla. Does not disable right-clicking on items.")
            .define("big_pickings", false);
            
    public static final ModConfigSpec.BooleanValue LOOKING_ITEMS = BUILDER
            .comment("If false, items will no longer face you when pressing the keybind or interacting. This may be useful for mod conficts as it disables all custom rendering.")
            .define("looking_items", true);
    
    public static final ModConfigSpec SPEC = BUILDER.build();
}