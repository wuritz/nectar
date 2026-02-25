package dev.nectar.ui.components.settings;

import dev.nectar.modules.setting.settings.IntSetting;
import dev.nectar.ui.components.generic.Slider;

public class IntComponent extends SettingComponent<Integer> {

    public IntComponent(int x, int y, int width, int height, IntSetting setting) {
        super(x, y, width, height, setting);

        this.setting = setting;

        Slider slider = new Slider(x+(width/2), y, width/2-35, height, setting.getMax(), this);
        components.add(slider);
    }

    @Override
    public void updateComponentsPos(int x, int y) {
        components.forEach(component -> {
            if (component instanceof Slider slider) {
                slider.x = x+(width/2);
                slider.y = y;
            }
        });
    }

}
