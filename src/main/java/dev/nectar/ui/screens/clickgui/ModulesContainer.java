package dev.nectar.ui.screens.clickgui;

import dev.nectar.modules.Module;
import dev.nectar.modules.Modules;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static dev.nectar.Nectar.mc;

public class ModulesContainer extends Component {

    private final List<ModuleButton> moduleButtons = new ArrayList<>();
    public Module.Category currentCategory = null;
    private String categoryLabel = "";

    public ModulesContainer(int x, int y, int width, int height) {
        super(x, y, width, height);
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
            moduleButtons.add(new ModuleButton(modX, modY, modWidth, modHeight, mod));
            modY += modHeight + yOffset;
        }
    }

    @Override
    protected void render(DrawContext context, int mouseX, int mouseY) {
        // Background
        context.fill(this.x, y, x + width, y + height, new Color(56, 56, 56, 100).getRGB());

        if (currentCategory == null) categoryLabel = "None selected";

        // Label
        context.drawTextWithShadow(mc.textRenderer, categoryLabel, x + 10, y + 10, Color.WHITE.getRGB());
        moduleButtons.forEach((moduleButton) -> moduleButton.render(context, mouseX, mouseY));
    }

    @Override
    protected boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        moduleButtons.forEach((moduleButton) -> {
            if (moduleButton.mouseClicked(mouseX, mouseY, mouseButton)) {
                Modules.get().get(moduleButton.module.getName()).toggle();
            }
        });

        return false;
    }
}
