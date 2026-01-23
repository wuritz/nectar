package dev.nectar.ui.screens.clickgui;

import dev.nectar.modules.Module;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Screen {

    public static final ClickGUI INSTANCE = new ClickGUI();
    private final List<Frame> frames = new ArrayList<>();;
    private final ModulesContainer modulesContainer;

    private ClickGUI() {
        super(Text.literal("ClickGUI"));

        int offsetY = 20;
        int offsetX = 20;

        modulesContainer = new ModulesContainer(20, 20, 500, 350);

        for (Module.Category category : Module.Category.values()) {
            frames.add(new Frame(offsetX, 20 + offsetY, 100, 20, category));
            offsetX += 100;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        modulesContainer.render(context);

        for (Frame frame : frames) {
            frame.render(context, mouseX, mouseY, deltaTicks);
            frame.updatePos(mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        for (Frame frame : frames) {
            frame.mouseClicked(click.x(), click.y(), click.button());
        }
        return true;
    }

    @Override
    public boolean mouseReleased(Click click) {
        for (Frame frame : frames) {
            frame.mouseReleased(click.x(), click.y(), click.button());
        }
        return true;
    }
}
