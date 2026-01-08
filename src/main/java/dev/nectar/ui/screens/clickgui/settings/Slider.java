package dev.nectar.ui.screens.clickgui.settings;

import dev.nectar.modules.util.settings.NumberSetting;
import dev.nectar.modules.util.settings.Setting;
import dev.nectar.ui.UIUtils;
import dev.nectar.ui.screens.clickgui.ModButton;
import net.minecraft.client.gui.DrawContext;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Slider extends Component {
    private NumberSetting numberSetting;

    private int margin = (parent.parent.height / 2) - (mc.textRenderer.fontHeight / 2);
    private boolean sliding = false;

    public Slider(Setting setting, ModButton parent, int offset) {
        super(setting, parent, offset);

        this.numberSetting = (NumberSetting) setting;
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);

        sliding = false;
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float deltaTicks) {
        super.render(drawContext, mouseX, mouseY, deltaTicks);
        double difference = Math.min(parent.parent.width, Math.max(0, mouseX - parent.parent.x));

        drawContext.fill(parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y + parent.offset + offset + parent.parent.height, UIUtils.BACKGROUND_BASE.darker().getRGB());

        int renderWidth = (int)(parent.parent.width * (numberSetting.getValue() - numberSetting.getMinimum()) / (numberSetting.getMaximum() - numberSetting.getMinimum()));
        drawContext.fill(parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + renderWidth, parent.parent.y + parent.offset + offset + parent.parent.height, UIUtils.getSelectedPrimaryColor().darker().getRGB());

        if (sliding) {
            if (difference == 0) {
                numberSetting.setValue(numberSetting.getMinimum());
            } else {
                numberSetting.setValue(roundToPlace((difference / parent.parent.width) * (numberSetting.getMaximum() - numberSetting.getMinimum()) + numberSetting.getMinimum(), 2));
            }
        }

        drawContext.drawTextWithShadow(mc.textRenderer, numberSetting.getName() + ": " + roundToPlace(numberSetting.getValue(), 2), parent.parent.x + margin, parent.parent.y + parent.offset + offset + margin, UIUtils.LIGHT.getRGB());
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        if (isHovered(mouseX, mouseY) && parent.extended) {
            sliding = true;
        }
    }

    @Override
    public boolean isHovered(double mouseX, double mouseY) {
        return super.isHovered(mouseX, mouseY);
    }

    private double roundToPlace(double value, int place) {
        if (place < 0) {
            return value;
        }

        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(place, RoundingMode.HALF_UP);

        return bigDecimal.doubleValue();
    }
}
