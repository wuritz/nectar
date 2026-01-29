package dev.nectar.ui.components.clickgui;

import dev.nectar.modules.Module;
import dev.nectar.ui.components.Component;
import dev.nectar.ui.window.windows.ModuleWindow;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;

import static dev.nectar.Nectar.mc;

public class ModuleButton extends Component {

    public final Module module;
    private ModuleWindow moduleWindow;
    private final ModulesContainer parent;
    private final boolean overlapping = false;

    public ModuleButton(int x, int y, int width, int height, Module module, ModulesContainer parent) {
        super(x, y, width, height);

        this.parent = parent;
        this.module = module;
        this.moduleWindow = null;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        // Background
        if (module.isEnabled()) {
            if (isHovered(mouseX, mouseY)) context.fill(x, y, x+width, y+height, new Color(85, 255, 38, 119).getRGB());
            else context.fill(x, y, x+width, y+height, new Color(85, 255, 38, 205).getRGB());
        } else {
            if (isHovered(mouseX, mouseY)) context.fill(x, y, x+width, y+height, new Color(175, 175, 175, 180).getRGB());
            else context.fill(x, y, x+width, y+height, new Color(101, 101, 101, 194).getRGB());
        }

        context.drawTextWithShadow(mc.textRenderer, module.getDisplayName(), x + 10, y + height / 2 - (mc.textRenderer.fontHeight / 2), Color.WHITE.getRGB());
    }


    @Override
    public boolean onLeftClick(double mouseX, double mouseY) {
        if (overlapping) return false;

        module.toggle();
        return true;
    }

    @Override
    public boolean onRightClick(double mouseX, double mouseY) {
        if (overlapping) return false;

        if (moduleWindow == null) moduleWindow = new ModuleWindow(200, 300, 400, 300, module);
        if (parent.clickGUI.isModuleWindowOpen(moduleWindow)) return false;

        parent.clickGUI.addModuleWindow(moduleWindow);
        moduleWindow.open();

        return true;
    }
}
