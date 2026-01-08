package hu.bluestoplight.ui.screens.clickgui.settings;

import hu.bluestoplight.SednaClient;
import hu.bluestoplight.modules.util.settings.Setting;
import hu.bluestoplight.ui.screens.clickgui.ModButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class Component {
    public Setting setting;
    public ModButton parent;
    protected MinecraftClient mc = SednaClient.mc;

    public int offset;

    public Component (Setting setting, ModButton parent, int offset) {
        this.setting = setting;
        this.parent = parent;
        this.offset = offset;
    }

    public void render(DrawContext drawContext, int mouseX, int mouseY, float deltaTicks) {}

    public void mouseClicked(double mouseX, double mouseY, int button) {}

    public void mouseReleased(double mouseX, double mouseY, int button) {}

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX > parent.parent.x && mouseX < parent.parent.x + parent.parent.width
                && mouseY > parent.parent.y + parent.offset + offset
                && mouseY < parent.parent.y + parent.offset + offset + parent.parent.height;
    }
}