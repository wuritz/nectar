package dev.nectar.ui.screens.clickgui.settings;

import dev.nectar.Nectar;
import dev.nectar.modules.util.settings.Setting;
import dev.nectar.ui.screens.clickgui.ModButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class Component {
    public Setting setting;
    public ModButton parent;
    protected MinecraftClient mc = Nectar.mc;

    public int offset;

    public Component (Setting setting, ModButton parent, int offset) {
        this.setting = setting;
        this.parent = parent;
        this.offset = offset;
    }

    public void render(DrawContext drawContext, float deltaTicks) {}

    public void mouseClicked(double mouseX, double mouseY, int button) {}

    public void mouseReleased(double mouseX, double mouseY, int button) {}

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > parent.parent.x && mouseX < parent.parent.x + parent.parent.width
                && mouseY > parent.parent.y + parent.offset + offset
                && mouseY < parent.parent.y + parent.offset + offset + parent.parent.height;
    }
}