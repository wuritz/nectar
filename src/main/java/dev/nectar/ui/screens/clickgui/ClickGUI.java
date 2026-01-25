package dev.nectar.ui.screens.clickgui;

import dev.nectar.modules.Module;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

import static dev.nectar.Nectar.mc;

public class ClickGUI extends Screen {

    public static final ClickGUI INSTANCE = new ClickGUI();
    private final List<Component> components = new ArrayList<>();
    private final List<CategoryButton> categoryButtons = new ArrayList<>();
    private final ModulesContainer modulesContainer;
    private CategoryButton selectedCategory = null;

    private ClickGUI() {
        super(Text.literal("ClickGUI"));

        modulesContainer = initClickGUI();
    }

     /**
     *  Init ClickGUI
     *
     *  @return The container
     */
    private @NonNull ModulesContainer initClickGUI() {
        final ModulesContainer modulesContainer;
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

        modulesContainer = new ModulesContainer(mcX, mcY, mcWidth, mcHeight);
        modulesContainer.updateCategory(selectedCategory.category);
        components.add(modulesContainer);

        return modulesContainer;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        components.forEach(component -> component.render(context, mouseX, mouseY));
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        components.forEach(component -> {
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
}
