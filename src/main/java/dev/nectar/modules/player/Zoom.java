package dev.nectar.modules.player;

import dev.nectar.core.input.KeyAction;
import dev.nectar.core.input.Keybind;
import dev.nectar.events.core.input.KeyEvent;
import dev.nectar.events.world.TickEvent;
import dev.nectar.modules.Module;
import dev.nectar.modules.setting.Setting;
import dev.nectar.modules.setting.settings.DoubleSetting;
import dev.nectar.modules.setting.settings.KeybindSetting;
import dev.nectar.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import org.lwjgl.glfw.GLFW;

public class Zoom extends Module {

    private final Setting<Double> zoomLevel = new DoubleSetting.Builder()
            .name("Modifier").description("")
            .min(0.1d).max(1d)
            .defaultValue(0.8d)
            .build();

    private final Setting<Double> speed = new DoubleSetting.Builder()
            .name("Speed").description("")
            .min(0.1d).max(1d)
            .defaultValue(0.9d)
            .build();

    private final Setting<Keybind> zoomKey = new KeybindSetting.Builder()
            .name("Zoom Key").description("")
            .defaultValue(Keybind.fromKey(GLFW.GLFW_KEY_C))
            .action(this::handleKey)
            .build();

    private boolean zooming = false;
    private boolean canZoom = false;
    private float progress = 0.0f;

    public Zoom() {
        super("Zoom", "Does the Samsung thing.", Category.PLAYER);

        addSetting(zoomLevel);
        addSetting(speed);
        addSetting(zoomKey);
    }

    @EventHandler
    public void onTick(TickEvent.Pre event) {
        float target = zooming ? 1.0f : 0.0f;
        progress += (target - progress) * speed.get().floatValue();

        if (Math.abs(progress - target) < 0.001f) {
            progress = target;
        }
    }

    public float getZoomModifier() {
        return 1.0f - (progress * zoomLevel.get().floatValue());
    }

    private void handleKey() {
        canZoom = true;
    }

    @EventHandler
    public void onKey(KeyEvent event) {
        if (!Utils.isToggleable()) return;
        if (event.action == KeyAction.Repeat) return;

        if (!canZoom) return;

        zooming = event.action != KeyAction.Release;
        if (!zooming) canZoom = false;
    }
}