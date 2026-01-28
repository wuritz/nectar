package dev.nectar.ui.components.settings;

import dev.nectar.core.Color;
import dev.nectar.core.input.Keybind;
import dev.nectar.modules.setting.settings.KeybindSetting;
import dev.nectar.ui.components.generic.KeybindBox;
import net.minecraft.client.gui.DrawContext;

import static dev.nectar.Nectar.mc;

public class KeybindComponent extends SettingComponent<Keybind> {

    public KeybindComponent(int x, int y, int width, int height, KeybindSetting setting) {
        super(x, y, width, height, setting);

        this.setting = setting;

        components.clear();

        KeybindBox keybindBox = new KeybindBox(x+width-70, y, 50, height, this);
        components.add(keybindBox);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        context.drawTextWithShadow(mc.textRenderer, setting.getName(), x, y, Color.WHITE.getPacked());

        components.forEach(component -> component.render(context, mouseX, mouseY));
    }

    @Override
    public void updateComponentsPos(int x, int y) {
        components.forEach(component -> {
            component.x = x+width-70;
            component.y = y;
        });
    }
}
