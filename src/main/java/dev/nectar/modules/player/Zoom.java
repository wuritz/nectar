package dev.nectar.modules.player;

import dev.nectar.core.input.KeyAction;
import dev.nectar.core.input.Keybind;
import dev.nectar.events.core.KeyEvent;
import dev.nectar.events.world.TickEvent;
import dev.nectar.modules.Module;
import dev.nectar.modules.util.settings.KeybindSetting;
import dev.nectar.modules.util.settings.NumberSetting;
import dev.nectar.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import org.lwjgl.glfw.GLFW;

public class Zoom extends Module {
    private static final NumberSetting zoomLevel = new NumberSetting("Modifier", 0.01, 1.0, 0.8, 0.01);
    private static final NumberSetting speed = new NumberSetting("Speed", 0.1, 1.0, 0.9, 0.1);
    private static final KeybindSetting zoomKey = new KeybindSetting("Zoom Key", Keybind.fromKey(GLFW.GLFW_KEY_C));

    private static boolean zooming = false;
    private static float progress = 0.0f;

    public Zoom() {
        super("Zoom", "Does the Samsung thing.", Category.PLAYER);
        addSetting(zoomLevel);
        addSetting(speed);
        addSetting(zoomKey);
    }

    @EventHandler
    public void onTick(TickEvent.Pre event) {
        float target = zooming ? 1.0f : 0.0f;
        progress += (target - progress) * speed.getFloatValue();

        if (Math.abs(progress - target) < 0.001f) {
            progress = target;
        }
    }

    public static float getZoomModifier() {
        return 1.0f - (progress * zoomLevel.getFloatValue());
    }

    public static float getZoomLevel() {
        return zoomLevel.getFloatValue();
    }

    public static boolean isZooming() {
        return zooming;
    }

    public static void setZooming(boolean zooming) {
        Zoom.zooming = zooming;
    }

    @EventHandler
    public void onKey(KeyEvent event) {
        if (!Utils.isToggleable()) return;
        if (event.action == KeyAction.Repeat) return;

        if (event.input.key() != zoomKey.getKeybind().getValue()) return;

        if (event.action == KeyAction.Release) setZooming(false);
        else setZooming(true);
    }
}