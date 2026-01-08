package dev.nectar.core.input;

import dev.nectar.Nectar;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class CoreKeybinds {

    private static final KeyBinding.Category CATEGORY = KeyBinding.Category.create(Nectar.identifier("sedna"));

    public static KeyBinding OPEN_GUI = new KeyBinding("Open GUI", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_CONTROL, CATEGORY);

    private CoreKeybinds() {

    }

    public static KeyBinding[] apply(KeyBinding[] binds) {
        // Add key binding
        KeyBinding[] newBinds = new KeyBinding[binds.length + 1];

        System.arraycopy(binds, 0, newBinds, 0, binds.length);
        newBinds[binds.length] = OPEN_GUI;

        return newBinds;
    }

}
