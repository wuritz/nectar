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

import static dev.nectar.Nectar.LOG;
import static dev.nectar.Nectar.mc;

public class Slider extends Component {

    private final SettingComponent parentComponent;
    private double min, max, finalValue, scrollingValue;
    private boolean sliding;

    private String scrollingText = "";

    public Slider(int x, int y, int width, int height, double min, double max, SettingComponent<?> parentComponent) {
        super(x, y, width, height);

        this.parentComponent = parentComponent;
        this.min = min;
        this.max = max;

        sliding = false;
        this.scrollingValue = parentComponent.getValueForSlider();
    }

    private String getScrollingText(int maxDraw, int toDraw) {
        if (sliding) {
            float currentScrollingPercentage = (float) toDraw / (maxDraw-x);
            scrollingValue = max * currentScrollingPercentage;
        } else {
            scrollingValue = parentComponent.getValueForSlider();
        }

        BigDecimal bd = new BigDecimal(Double.toString(scrollingValue));
        return String.valueOf(bd.setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        int sliderMaxDraw = x+width + 15;
        float sliderPercentage = (float) (scrollingValue/max);
        int sliderToDraw = getToDraw(sliderMaxDraw, sliderPercentage, mouseX);

        scrollingText = getScrollingText(sliderMaxDraw, sliderToDraw);

        context.fill(x, y, sliderMaxDraw, y+height, new Color(24, 42, 51, 180).getRGB());
        context.fill(x, y, x+sliderToDraw, y+height, new Color(56, 101, 218).getRGB());
        context.drawTextWithShadow(mc.textRenderer, scrollingText, x-mc.textRenderer.getWidth(scrollingText)-5, y, Color.WHITE.getRGB());
    }

    private int getToDraw(int maxDraw, float percentage, int mouseX) {
        if (sliding) return Math.clamp(mouseX - x, 0, maxDraw-x);
        else return (int) ((maxDraw - x) * percentage);
    }

    @SuppressWarnings("unchecked")
    private void handleNewValue() {
        finalValue = Double.parseDouble(scrollingText);
        if (!parentComponent.setting.set(finalValue)) Nectar:LOG.error("Error on Slider");
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        sliding = true;

        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (sliding) {
            sliding = false;
            handleNewValue();
        }

        return true;
    }

}
