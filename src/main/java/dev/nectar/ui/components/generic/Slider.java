package dev.nectar.ui.components.generic;

import dev.nectar.Nectar;
import dev.nectar.ui.components.Component;
import dev.nectar.ui.components.settings.SettingComponent;
import net.minecraft.client.gui.DrawContext;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicBoolean;

import static dev.nectar.Nectar.mc;

public class Slider extends Component {

    private int dragX, sliderMaxDraw;
    private final SettingComponent parentComponent;
    private double min, max, finalValue, scrollingValue;
    private boolean sliding;
    private int activeButton = -1;

    public Slider(int x, int y, int width, int height, double min, double max, SettingComponent<?> parentComponent) {
        super(x, y, width, height);

        this.parentComponent = parentComponent;
        this.min = min;
        this.max = max;

        sliding = false;
        this.scrollingValue = parentComponent.getValueForSlider();
    }

    private String getScrollingText() {
        BigDecimal bd = new BigDecimal(Double.toString(scrollingValue));
        return String.valueOf(bd.setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        String scrollingText = getScrollingText();

        sliderMaxDraw = x+width + 15;
        float sliderPercentage = (float) (scrollingValue/max);
        int sliderToDraw = sliding ? dragX : (int) ((sliderMaxDraw - x) * sliderPercentage);

        context.fill(x, y, sliderMaxDraw, y+height, new Color(24, 42, 51, 180).getRGB());
        context.fill(x, y, x+sliderToDraw, y+height, new Color(56, 101, 218).getRGB());
        context.drawTextWithShadow(mc.textRenderer, scrollingText, x-mc.textRenderer.getWidth(scrollingText)-5, y, Color.WHITE.getRGB());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        sliding = true;

        dragX = (int) (mouseX - x);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (sliding) sliding = false;

        return true;
    }

}
