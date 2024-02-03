package net.Ryaas.firstmod.assisting;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public class KeyBinding {
    public static final KeyBinding INSTANCE = new KeyBinding();
    public static final String KEY_CATEGORY = "key.category.firstmod.randombs";
    public static final String KEY_USE_MARK = "key.firstmod.use_mark";
    public static final String KEY_PRIMARY = "key.firstmod.primary";
    public static final String KEY_SECONDARY = "key.firstmod.secondary";

public static final KeyMapping MARK_KEY = new KeyMapping(KEY_USE_MARK, KeyConflictContext.IN_GAME,
        InputConstants.getKey(InputConstants.KEY_R,-1), KEY_CATEGORY);
    public static final KeyMapping PRIMARY = new KeyMapping(KEY_PRIMARY, KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_F,-1), KEY_CATEGORY);

    public static final KeyMapping SECONDARY = new KeyMapping(KEY_SECONDARY, KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_C,-1), KEY_CATEGORY);
}


