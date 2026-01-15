package dev.nectar.ui.screens.clickgui;

import dev.nectar.Nectar;
import dev.nectar.events.core.render.Render2DEvent;
import dev.nectar.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Screen {
    public static final ClickGUI INSTANCE = new ClickGUI();
    private final List<Frame> frames = new ArrayList<>();;

    private ClickGUI() {
        super(Text.literal("ClickGUI"));

        int offset = 20;
        int offsetX = 20;

        for (Module.Category category : Module.Category.values()) {
            frames.add(new Frame(offsetX, 20 + offset, 100, 20, category));

            if ((offset += 120) >= 260) {
                offset = 20;
                offsetX += 120;
            } else {
                offset += 60;
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);

        for (Frame frame : frames) {
            frame.render(context, deltaTicks);
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
