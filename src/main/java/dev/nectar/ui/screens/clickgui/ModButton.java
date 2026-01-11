package dev.nectar.ui.screens.clickgui;

import dev.nectar.modules.Module;
import dev.nectar.modules.ModuleManager;
import dev.nectar.modules.util.settings.*;
import dev.nectar.ui.screens.clickgui.settings.*;
import dev.nectar.ui.screens.clickgui.settings.Component;
import dev.nectar.ui.UIUtils;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModButton {
    public Module mod;
    public Frame parent;
    public List<Component> components;

    public int offset;
    private int pageMargin = 8;
    public boolean extended;

    public ModButton(Module mod, Frame parent, int offset) {
        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.components = new ArrayList<>();
        this.extended = false;

        int setOffset = parent.height;
        for (Setting<?> setting : mod.getSettings()) {
            if (setting instanceof BooleanSetting) {
                components.add(new CheckBox(setting, this, setOffset));
            } else if (setting instanceof ModeSetting) {
                components.add(new ModeBox(setting, this, setOffset));
            } else if (setting instanceof NumberSetting) {
                components.add(new Slider(setting, this, setOffset));
            } else if (setting instanceof KeybindSetting) {
                components.add(new KeybindBox(setting, this, setOffset));
            }

            setOffset += parent.height;
        }
    }

    public void render(DrawContext drawContext, float deltaTicks) {
        int margin = (parent.height / 2) - (parent.mc.textRenderer.fontHeight / 2);

        if (extended) drawContext.fill(parent.x, parent.y + offset, parent.x + parent.width, parent.y + offset + parent.height, new Color(38,38,38,180).getRGB());
        else drawContext.fill(parent.x, parent.y + offset, parent.x + parent.width, parent.y + offset + parent.height, UIUtils.BACKGROUND_BASE.brighter().getRGB());

        if (extended) {
            for (Component component : components) {
                component.render(drawContext, deltaTicks);
            }
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered(mouseX, mouseY)) {
            if (button == 0) {
                mod.toggle();
            } else if (button == 1) {
                extended = !extended;
                parent.updateButtons();
            }
        }

        for (Component component : components) {
            component.mouseClicked(mouseX, mouseY, button);
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        for (Component component : components) {
            component.mouseReleased(mouseX, mouseY, button);
        }
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > parent.x && mouseX < parent.x + parent.width
                && mouseY > parent.y + offset && mouseY < parent.y + offset + parent.height;
    }
}