package dev.nectar.ui.screens.clickgui;

import dev.nectar.modules.Module;
import dev.nectar.ui.components.Component;
import dev.nectar.ui.components.clickgui.CategoryButton;
import dev.nectar.ui.components.clickgui.ModulesContainer;
import dev.nectar.ui.screens.clickgui.settings.ModuleWindow;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

import static dev.nectar.Nectar.mc;

public class ClickGUI extends Screen {

    private final List<Component> components = new ArrayList<>();
    private final List<CategoryButton> categoryButtons = new ArrayList<>();
    private final List<ModuleWindow> moduleWindows = new ArrayList<>();

    private boolean overlapping = false;

    private ModulesContainer modulesContainer;
    private CategoryButton selectedCategory = null;

    public ClickGUI() {
        super(Text.literal("ClickGUI"));

        modulesContainer = initClickGUI();
    }

    public void addModuleWindow(ModuleWindow moduleWindow) {
        moduleWindows.add(moduleWindow);
    }

    /**
     * Init ClickGUI
     *
     * @return The container
     */
    private @NonNull ModulesContainer initClickGUI() {
        int mcWidth = 500;
        int mcHeight = 350;
        int modulesWidth = 100;
        int modulesHeight = mcHeight / 6;

        int modulesX = mc.getWindow().getWidth() / 2 - mcWidth - (mcWidth / 2) - modulesWidth;
        int modulesY = mc.getWindow().getHeight() / 2 - mcHeight - (mcHeight / 4);

        for (Module.Category category : Module.Category.values()) {
            CategoryButton newButton = new CategoryButton(modulesX, modulesY, modulesWidth, modulesHeight, category);

            categoryButtons.add(newButton);
            components.add(newButton);

            modulesY += modulesHeight;
        }

        // Select the first category by default
        selectedCategory = categoryButtons.getFirst();
        selectedCategory.select();

        int mcX = modulesX + modulesWidth;
        int mcY = mc.getWindow().getHeight() / 2 - mcHeight - (mcHeight / 4);

        modulesContainer = new ModulesContainer(mcX, mcY, mcWidth, mcHeight, this);
        modulesContainer.updateCategory(selectedCategory.category);
        components.add(modulesContainer);

        return modulesContainer;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        components.forEach(component -> component.render(context, mouseX, mouseY));
        moduleWindows.forEach(moduleWindow -> {
            moduleWindow.render(context, mouseX, mouseY);
            moduleWindow.updatePos(mouseX, mouseY);
        });
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        components.forEach(component -> {
            moduleWindows.forEach(moduleWindow -> {
                if (!moduleWindow.isOpened()) {
                    overlapping = false;
                    return;
                }

                if (component.isHovered(click.x(), click.y()) && moduleWindow.isHovered(click.x(), click.y())) {
                    moduleWindow.mouseClicked(click.x(), click.y(), click.button());
                    overlapping = true;
                } else if (component.isHovered(click.x(), click.y())) {
                    overlapping = false;
                } else if (moduleWindow.isHovered(click.x(), click.y())) {
                    moduleWindow.mouseClicked(click.x(), click.y(), click.button());
                    overlapping = false;
                }
            });

            if (overlapping) return;

            if (component instanceof CategoryButton categoryButton) {
                // Handle CategoryButton
                if (!categoryButton.mouseClicked(click.x(), click.y(), click.button())) return;

                // Update the selected category
                selectedCategory.deselect();
                selectedCategory = categoryButton;
                modulesContainer.updateCategory(selectedCategory.category);
            } else {
                component.mouseClicked(click.x(), click.y(), click.button());
            }
        });

        return true;
    }


    @Override
    public boolean mouseReleased(Click click) {
        moduleWindows.forEach(moduleWindow -> moduleWindow.mouseReleased(click.x(), click.y(), click.button()));

        return super.mouseReleased(click);
    }

    public boolean isModuleWindowOpen(ModuleWindow moduleWindow) {
        return moduleWindows.contains(moduleWindow);
    }
}
