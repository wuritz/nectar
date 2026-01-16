package dev.nectar.ui.screens.clickgui;

import dev.nectar.Nectar;
import dev.nectar.modules.Module;
import dev.nectar.modules.Modules;
import dev.nectar.ui.UIUtils;
import dev.nectar.ui.screens.clickgui.settings.Component;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class Frame {
    public int x, y, width, height, dragX, dragY;
    public Module.Category category;
    public boolean dragging, extended;
    private List<ModButton> buttons;

    protected MinecraftClient mc = Nectar.mc;

    public Frame(int x, int y, int width, int height, Module.Category category) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.category = category;
        this.dragging = false;
        this.extended = false;

        buttons = new ArrayList<>();
        int offset = height;

        for (Module mod : Modules.get().getModsInCategory(category)) {
            buttons.add(new ModButton(mod, this, offset));
            offset += height;
        }
    }

    public void render(DrawContext drawContext, int mouseX, int mouseY, float deltaTicks) {
        int margin = (height / 2) - (mc.textRenderer.fontHeight / 2);

        drawContext.fill(x, y, x + width, y + height, UIUtils.BACKGROUND_BASE.getRGB());
        drawContext.drawTextWithShadow(mc.textRenderer, category.name, x + margin, y + margin, UIUtils.LIGHT.getRGB());
        drawContext.drawTextWithShadow(mc.textRenderer, extended ? "^" : "-", x + width - (mc.textRenderer.getWidth("^") * 2), y + margin, UIUtils.LIGHT.getRGB());

        if (extended) {
            for (ModButton button : buttons) {
                button.render(drawContext, mouseX, mouseY, deltaTicks);
            }
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            if (mouseButton == 0) {
                dragging = true;

                dragX = (int) (mouseX - x);
                dragY = (int) (mouseY - y);
            } else if (mouseButton == 1) {
                extended = !extended;
            }
        }

        if (extended) {
            for (ModButton button : buttons) {
                button.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        if (mouseButton == 0 && dragging == true) {
            dragging = false;
        }

        for (ModButton button : buttons) {
            button.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
    }

    public void updatePos(double mouseX, double mouseY) {
        if (dragging) {
            x = (int) (mouseX - dragX);
            y = (int) (mouseY - dragY);
        }
    }

    public void updateButtons() {
        int offset = height;

        for (ModButton button : buttons) {
            button.offset = offset;
            offset += height;

            if (button.extended) {
                for (Component component : button.components) {
                    if (component.setting.isVisible()) offset += height;
                }
            }
        }
    }
}