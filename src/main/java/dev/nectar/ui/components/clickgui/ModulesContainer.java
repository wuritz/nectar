package dev.nectar.ui.components.clickgui;

import dev.nectar.modules.Module;
import dev.nectar.modules.Modules;
import dev.nectar.ui.components.Component;
import dev.nectar.ui.screens.clickgui.ClickGUI;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static dev.nectar.Nectar.mc;

public class ModulesContainer extends Component {

    private final List<ModuleButton> moduleButtons = new ArrayList<>();
    public final ClickGUI clickGUI;
    public Module.Category currentCategory = null;
    private String categoryLabel = "";

    public ModulesContainer(int x, int y, int width, int height, ClickGUI parent) {
        super(x, y, width, height);

        this.clickGUI = parent;
    }

    public void updateCategory(Module.Category category) {
        currentCategory = category;
        categoryLabel = currentCategory.name;

        updateModuleButtons();
    }

    private void updateModuleButtons() {
        moduleButtons.clear();

        int modX = x + 10;
        int modY = y + 30;
        int modWidth = 100;
        int modHeight = 25;

        int xOffset = 10;
        int yOffset = 10;

        for (Module mod : Modules.get().getModsInCategory(currentCategory)) {
            moduleButtons.add(new ModuleButton(modX, modY, modWidth, modHeight, mod, this));
            modY += modHeight + yOffset;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        // Background
        context.fill(this.x, y, x + width, y + height, new Color(56, 56, 56, 100).getRGB());

        if (currentCategory == null) categoryLabel = "None selected"; // how

        // Label
        context.drawTextWithShadow(mc.textRenderer, categoryLabel, x + 10, y + 10, Color.WHITE.getRGB());
        moduleButtons.forEach((moduleButton) -> moduleButton.render(context, mouseX, mouseY));

        // Explainer
        context.drawTextWithShadow(mc.textRenderer, "Right click to open a module", x + 10, y + height + mc.textRenderer.fontHeight - 5, Color.WHITE.getRGB());
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        moduleButtons.forEach((moduleButton) -> {
            if (moduleButton.isHovered(mouseX, mouseY)) {
                if (button == 0) moduleButton.onLeftClick(mouseX, mouseY);
                else moduleButton.onRightClick(mouseX, mouseY);
            }
        });

        return false;
    }
}
