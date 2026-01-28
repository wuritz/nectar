package dev.nectar.ui.components.generic;

import dev.nectar.modules.setting.settings.BooleanSetting;
import dev.nectar.ui.components.Component;
import dev.nectar.ui.components.settings.BoolComponent;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

public class Checkbox extends Component {

    private final BooleanSetting booleanSetting;
    private boolean checked;

    public Checkbox(int x, int y, int width, int height, BoolComponent parentComponent) {
        super(x, y, width, height);

        this.booleanSetting = (BooleanSetting) parentComponent.setting;
        this.checked = booleanSetting.get();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        x -= 10;

        // Background
        if (isHovered(mouseX, mouseY)) context.fill(x, y, x+width, y+height, new Color(131, 131, 131, 187).getRGB());
        context.fill(x, y, x+width, y+height, new Color(71, 71, 71, 155).getRGB());

        if (checked) {
            if (isHovered(mouseX, mouseY)) context.fill(x, y, x+width, y+height, new Color(213, 213, 213, 187).getRGB());
            else context.fill(x, y, x+width, y+height, Color.WHITE.getRGB());
        }
    }

    private void toggle() {
        checked = !checked;
        booleanSetting.set(checked);
    }

    @Override
    public boolean onLeftClick(double mouseX, double mouseY) {
        toggle();

        return false;
    }



}
