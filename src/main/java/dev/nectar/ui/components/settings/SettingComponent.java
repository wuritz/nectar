package dev.nectar.ui.components.settings;

import dev.nectar.core.Color;
import dev.nectar.modules.setting.Setting;
import dev.nectar.ui.components.Component;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

import static dev.nectar.Nectar.mc;

public abstract class SettingComponent<T> extends Component {

    public Setting<?> setting;
    protected List<Component> components = new ArrayList<>();

    public SettingComponent(int x, int y, int width, int height, Setting<?> setting) {
        super(x, y, width, height);

        this.setting = setting;
    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        context.drawTextWithShadow(mc.textRenderer, setting.getName(), x, y, Color.WHITE.getPacked());

        components.forEach(component -> component.render(context, mouseX, mouseY));
    }

    public float getValueForSlider() {
        if (setting.get() instanceof Boolean bool) {
            if (bool) return 1f;
            else return 0f;
        } else if (setting.get() instanceof Integer integer) {
            return (float) integer;
        } else if (setting.get() instanceof Double doubleValue) {
            return doubleValue.floatValue();
        }

        return 0f;
    }

    public abstract void updateComponentsPos(int x, int y);

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (isHovered(mouseX, mouseY)) components.forEach(component -> component.mouseClicked(mouseX, mouseY, mouseButton));
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        components.forEach(component -> component.mouseReleased(mouseX, mouseY, mouseButton));
        return false;
    }

}
