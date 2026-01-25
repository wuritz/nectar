package dev.nectar.ui.components.clickgui;

import dev.nectar.modules.Module;
import dev.nectar.modules.Modules;
import dev.nectar.ui.components.Component;
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
    public void render(DrawContext context, int mouseX, int mouseY) {
        // Background
        context.fill(this.x, y, x + width, y + height, new Color(56, 56, 56, 100).getRGB());

        if (currentCategory == null) categoryLabel = "None selected"; // how

        // Label
        context.drawTextWithShadow(mc.textRenderer, categoryLabel, x + 10, y + 10, Color.WHITE.getRGB());
        moduleButtons.forEach((moduleButton) -> moduleButton.render(context, mouseX, mouseY));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        moduleButtons.forEach((moduleButton) -> {
            if (moduleButton.isHovered(mouseX, mouseY) && button == 0) moduleButton.onLeftClick();
        });

        return false;
    }

    @Override
    public boolean onLeftClick() {
        //moduleButtons.forEach(ModuleButton::onLeftClick);
        return false;
    }
}
