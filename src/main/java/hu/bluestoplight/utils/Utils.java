package hu.bluestoplight.utils;

import hu.bluestoplight.ui.screens.SednaMainMenuScreen;
import hu.bluestoplight.ui.screens.clickgui.ClickGUI;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;

import static hu.bluestoplight.SednaClient.mc;

public class Utils {

    public static boolean canUpdate() {
        return mc != null && mc.world != null && mc.player != null;
    }

    public static boolean canOpenGui() {
        if (canUpdate()) return mc.currentScreen == null;

        return mc.currentScreen instanceof SednaMainMenuScreen || mc.currentScreen instanceof MultiplayerScreen || mc.currentScreen instanceof SelectWorldScreen;
    }

    public static boolean canCloseGui() {
        return mc.currentScreen instanceof ClickGUI;
    }

    public static boolean isToggleable() {
        if (mc.player == null || mc.world == null) {
            return false;
        } else {
            return true;
        }
    }

}
