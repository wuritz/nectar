package dev.nectar.ui.components.generic;

import dev.nectar.ui.components.Component;
import dev.nectar.ui.components.settings.SettingComponent;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static dev.nectar.Nectar.LOG;
import static dev.nectar.Nectar.mc;

public class Slider extends Component {

    private final SettingComponent parentComponent;

    private final double max;
    private double scrollingValue;

    private boolean sliding;
    private final boolean decimalNeeded;

    // There's probably a better way to do this, but nevermind
    private String scrollingText = "";

    public Slider(int x, int y, int width, int height, double max, SettingComponent<?> parentComponent, boolean decimalNeeded) {
        super(x, y, width, height);

        this.parentComponent = parentComponent;
        this.max = max;
        this.decimalNeeded = decimalNeeded;

        sliding = false;
        this.scrollingValue = parentComponent.getValueForSlider();
    }

    public Slider(int x, int y, int width, int height, double max, SettingComponent<?> parentComponent) {
        this(x, y, width, height, max, parentComponent, false);
    }

    private String getScrollingText(int maxDraw, int toDraw) {
        if (sliding) {
            float currentScrollingPercentage = (float) toDraw / (maxDraw-x);
            scrollingValue = max * currentScrollingPercentage;
        } else {
            scrollingValue = parentComponent.getValueForSlider();
        }

        BigDecimal bd = new BigDecimal(Double.toString(scrollingValue)).setScale(2, RoundingMode.HALF_UP);

        if (!decimalNeeded) return String.valueOf(bd.intValue());
        return String.valueOf(bd.doubleValue());
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
        double finalValue = Double.parseDouble(scrollingText);
        if (!parentComponent.setting.set(finalValue)) LOG.error("Error on Slider");
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
