package dev.nectar.ui.components.settings;

import dev.nectar.modules.setting.settings.BooleanSetting;
import dev.nectar.ui.components.generic.Checkbox;

public class BoolComponent extends SettingComponent<Boolean> {

    public BoolComponent(int x, int y, int width, int height, BooleanSetting setting) {
        super(x, y, width, height, setting);

        this.setting = setting;

        Checkbox checkbox  = new Checkbox(x+width-30, y-5, 10, height, this);
        components.add(checkbox);
    }

    @Override
    public void updateComponentsPos(int x, int y) {
        components.forEach(component -> {
            component.x = x+width-30;
            component.y = y;
        });
    }
}
