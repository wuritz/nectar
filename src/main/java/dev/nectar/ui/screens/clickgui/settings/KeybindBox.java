package dev.nectar.ui.screens.clickgui.settings;

import dev.nectar.Nectar;
import dev.nectar.core.input.KeyAction;
import dev.nectar.events.core.KeyEvent;
import dev.nectar.modules.util.settings.KeybindSetting;
import dev.nectar.modules.util.settings.Setting;
import dev.nectar.ui.UIUtils;
import dev.nectar.ui.screens.clickgui.ModButton;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

public class KeybindBox extends Component {

    private final KeybindSetting keybindSetting;
    private final int margin = (parent.parent.height / 2) - (mc.textRenderer.fontHeight / 2);

    private String currentKeybind;
    private boolean listening;
    private final String label;

    public KeybindBox(Setting<?> setting, ModButton parent, int offset) {
        super(setting, parent, offset);

        this.keybindSetting = (KeybindSetting) setting;
        this.currentKeybind = keybindSetting.getKeybind().getKey();
        this.label = keybindSetting.getName() + ":";
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float deltaTicks) {
        super.render(drawContext, mouseX, mouseY, deltaTicks);

        drawContext.fill(parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y + parent.offset + offset + parent.parent.height, UIUtils.BACKGROUND_BASE.darker().getRGB());

        drawContext.drawTextWithShadow(mc.textRenderer, label, parent.parent.x + margin, parent.parent.y + parent.offset + offset + margin, UIUtils.LIGHT.getRGB());

        if (listening) {
            drawContext.drawTextWithShadow(mc.textRenderer, "...", parent.parent.x + margin + mc.textRenderer.getWidth(label + " "), parent.parent.y + parent.offset + offset + margin, UIUtils.LIGHT.getRGB());
        } else {
            drawContext.drawTextWithShadow(mc.textRenderer, currentKeybind, parent.parent.x + margin + mc.textRenderer.getWidth(label + " "), parent.parent.y + parent.offset + offset + margin, UIUtils.LIGHT.getRGB());
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        if (isHovered(mouseX, mouseY) && parent.extended && (button == 0)) {
            listening = true;
            Nectar.EVENT_BUS.subscribe(this);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onKeyBinding(KeyEvent event) {
        if (!listening) return;

        if (event.action == KeyAction.Press && event.key() == GLFW.GLFW_KEY_ESCAPE) {
            event.cancel();
            Nectar.EVENT_BUS.unsubscribe(this);
            listening = false;
        } else if (event.action == KeyAction.Release && setKeybind(true, event.key(), event.modifiers())) {
            event.cancel();
            Nectar.EVENT_BUS.unsubscribe(this);
        }
    }

    public boolean setKeybind(boolean isKey, int value, int modifiers) {
        if (listening) {
            keybindSetting.getKeybind().set(isKey, value, modifiers);
            if (keybindSetting.getName().equalsIgnoreCase("Keybind")) parent.mod.keybind.set(isKey, value, modifiers); // Bad way to do this

            currentKeybind = keybindSetting.getKeybind().getKey();
            listening = false;
            return true;
        }

        return false;
    }

    @Override
    public boolean isHovered(double mouseX, double mouseY) {
        return super.isHovered(mouseX, mouseY);
    }
}
