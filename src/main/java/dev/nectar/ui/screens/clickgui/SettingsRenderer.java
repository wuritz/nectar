package dev.nectar.ui.screens.clickgui;

import dev.nectar.modules.Module;
import dev.nectar.modules.setting.Setting;
import dev.nectar.modules.setting.Settings;
import dev.nectar.modules.setting.settings.BooleanSetting;
import dev.nectar.modules.setting.settings.DoubleSetting;
import dev.nectar.modules.setting.settings.IntSetting;
import dev.nectar.modules.setting.settings.KeybindSetting;
import dev.nectar.ui.components.Component;
import dev.nectar.ui.components.generic.HorizontalSeparator;
import dev.nectar.ui.components.settings.*;
import dev.nectar.ui.window.Window;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class SettingsRenderer {

    private final Module module;
    private final Settings settings;
    private final Window parentWindow;

    private final List<Component> components = new ArrayList<>();

    private int x, y;

    public SettingsRenderer(Module module, Window parentWindow) {
        this.module = module;
        this.settings = module.settings;
        this.parentWindow = parentWindow;

        addComponents();
    }

    private void addComponents() {
        components.clear();
        HorizontalSeparator sep = new HorizontalSeparator(x, y, parentWindow.width - 20, 5);
        components.add(sep);

        y += 15;

        for (Setting<?> setting : settings.getSettings()) {
            if (setting instanceof IntSetting) {
                IntComponent intComponent = new IntComponent(x, y, parentWindow.width, 10, (IntSetting) setting);
                components.add(intComponent);
            } else if (setting instanceof DoubleSetting) {
                DoubleComponent doubleComponent = new DoubleComponent(x, y, parentWindow.width, 10, (DoubleSetting) setting);
                components.add(doubleComponent);
            } else if (setting instanceof BooleanSetting) {
                BoolComponent boolComponent = new BoolComponent(x, y, parentWindow.width, 10, (BooleanSetting) setting);
                components.add(boolComponent);
            } else if (setting instanceof KeybindSetting) {
                KeybindComponent keybindComponent = new KeybindComponent(x, y, parentWindow.width, 10, (KeybindSetting) setting);
                components.add(keybindComponent);
            }

            y += 20;
        }
    }

    public void updatePos(int x, int y) {
        this.x = x;
        this.y = y;

        refreshComponents();
    }

    private void refreshComponents() {
        int internalX = x;
        int internalY = y;

        for (Component component : components) {
            if (component instanceof HorizontalSeparator sep) {
                sep.x = internalX;
                sep.y = internalY;

                internalY += 15;
            } else if (component instanceof SettingComponent<?> settingComponent) {
                settingComponent.x = internalX;
                settingComponent.y = internalY;
                settingComponent.updateComponentsPos(internalX, internalY);

                internalY += 20;
            }
        }
    }

    public void renderSettings(DrawContext context, int mouseX, int mouseY) {
        components.forEach(component -> component.render(context, mouseX, mouseY));
    }

    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        components.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        components.forEach(component -> component.mouseReleased(mouseX, mouseY, mouseButton));
    }

}
