package hu.bluestoplight.ui.screens.clickgui;

import hu.bluestoplight.modules.Mod;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Screen {
    public static final ClickGUI INSTANCE = new ClickGUI();
    private final List<Frame> frames;

    private ClickGUI() {
        super(Text.literal("ClickGUI"));

        frames = new ArrayList<>();

        int offset = 20;
        int offsetX = 20;

        for (Mod.Category category : Mod.Category.values()) {
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
            frame.render(context, mouseX, mouseY, deltaTicks);
            frame.updatePos(mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        for (Frame frame : frames) {
            frame.mouseClicked(click.x(), click.y(), click.button());
        }

        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseReleased(Click click) {
        for (Frame frame : frames) {
            frame.mouseReleased(click.x(), click.y(), click.button());
        }

        return super.mouseReleased(click);
    }
}
