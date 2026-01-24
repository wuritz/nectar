package dev.nectar.ui.screens.clickgui;

import dev.nectar.Nectar;
import dev.nectar.modules.Module;
import dev.nectar.modules.Modules;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

import static dev.nectar.Nectar.mc;

public class ModuleButton extends Component {

    private final Module module;

    public ModuleButton(int x, int y, int width, int height, Module module) {
        super(x, y, width, height);

        this.module = module;
    }

    @Override
    protected void render(DrawContext context, int mouseX, int mouseY) {
        // Background
        if (isHovered(mouseX, mouseY)) context.fill(x, y, x+width, y+height, new Color(175, 175, 175, 180).getRGB());
        else context.fill(x, y, x+width, y+height, new Color(144, 144, 145, 180).getRGB());

        context.drawTextWithShadow(mc.textRenderer, module.getDisplayName(), x + 10, y + height / 2 - (mc.textRenderer.fontHeight / 2), Color.WHITE.getRGB());
    }

    @Override
    protected boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            if (mouseButton == 0) {
                Modules.get().get(module.getName()).toggle();
                return true;
            }
        }

        return false;
    }
}
