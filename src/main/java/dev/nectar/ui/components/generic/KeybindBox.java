package dev.nectar.ui.components.generic;

import dev.nectar.Nectar;
import dev.nectar.core.input.KeyAction;
import dev.nectar.core.input.Keybind;
import dev.nectar.events.core.input.KeyEvent;
import dev.nectar.modules.setting.settings.KeybindSetting;
import dev.nectar.ui.components.Component;
import dev.nectar.ui.components.settings.KeybindComponent;
import dev.nectar.ui.components.settings.SettingComponent;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

import static dev.nectar.Nectar.mc;

public class KeybindBox extends Component {

    private final KeybindSetting keybindSetting;

    private int currentKey;
    private String currentKeyString;
    private boolean listening;

    public KeybindBox(int x, int y, int width, int height, KeybindComponent parentComponent) {
        super(x, y, width, height);

        this.keybindSetting = (KeybindSetting) parentComponent.setting;
        this.currentKey = keybindSetting.get().getValue();
        this.currentKeyString = keybindSetting.get().getKey();

        this.listening = false;
    }

    private void startListening() {
        listening = true;
        Nectar.EVENT_BUS.subscribe(this);
    }

    private void stopListening() {
        listening = false;
        Nectar.EVENT_BUS.unsubscribe(this);
    }

    private String getDrawString() {
        if (listening) return "...";
        if (currentKey == GLFW.GLFW_KEY_UNKNOWN) return "None";

        return currentKeyString;
    }

    @EventHandler
    public void onKey(KeyEvent event) {
        if (event.action != KeyAction.Press || !listening) return;
        if (event.input.key() == GLFW.GLFW_KEY_ESCAPE) {
            stopListening();
            return;
        }

        currentKey = event.input.key();
        Keybind newKeybind = Keybind.fromKey(currentKey);

        currentKeyString = newKeybind.getKey();
        keybindSetting.set(newKeybind);

        stopListening();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        // Background
        context.fill(x, y, x+width, y+height, new Color(136, 136, 136, 134).getRGB());

        String drawString = getDrawString();

        context.drawTextWithShadow(mc.textRenderer, drawString,
                x+width/2-(mc.textRenderer.getWidth(drawString)/2),
                y+height/2-mc.textRenderer.fontHeight/2,
                Color.WHITE.getRGB()
        );
    }

    @Override
    public boolean onLeftClick(double mouseX, double mouseY) {
        if (!listening) startListening();

        return false;
    }
}
