package dev.nectar.ui.components.settings;

import dev.nectar.core.input.Keybind;
import dev.nectar.modules.setting.settings.KeybindSetting;
import dev.nectar.ui.components.generic.KeybindBox;

public class KeybindComponent extends SettingComponent<Keybind> {

    public KeybindComponent(int x, int y, int width, int height, KeybindSetting setting) {
        super(x, y, width, height, setting);

        this.setting = setting;

        KeybindBox keybindBox = new KeybindBox(x+width-70, y, 50, height, this);
        components.add(keybindBox);
    }

    @Override
    public void updateComponentsPos(int x, int y) {
        components.forEach(component -> {
            component.x = x+width-70;
            component.y = y;
        });
    }
}
