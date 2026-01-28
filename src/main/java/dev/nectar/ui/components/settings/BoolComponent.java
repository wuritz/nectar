package dev.nectar.ui.components.settings;

import dev.nectar.core.Color;
import dev.nectar.modules.setting.settings.BooleanSetting;
import dev.nectar.ui.components.generic.Checkbox;
import net.minecraft.client.gui.DrawContext;

import static dev.nectar.Nectar.mc;

public class BoolComponent extends SettingComponent<Boolean> {

    public BoolComponent(int x, int y, int width, int height, BooleanSetting setting) {
        super(x, y, width, height, setting);

        this.setting = setting;

        components.clear();

        Checkbox checkbox  = new Checkbox(x+width, y-5, 10, height, this);
        components.add(checkbox);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        context.drawTextWithShadow(mc.textRenderer, setting.getName(), x, y, Color.WHITE.getPacked());

        components.forEach(component -> component.render(context, mouseX, mouseY));
    }

    @Override
    public void updateComponentsPos(int x, int y) {
        components.forEach(component -> {
            component.x = x+width-20;
            component.y = y;
        });
    }
}
