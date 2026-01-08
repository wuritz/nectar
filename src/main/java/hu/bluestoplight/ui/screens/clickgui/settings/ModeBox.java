package hu.bluestoplight.ui.screens.clickgui.settings;

import hu.bluestoplight.modules.util.settings.ModeSetting;
import hu.bluestoplight.modules.util.settings.Setting;
import hu.bluestoplight.ui.UIUtils;
import hu.bluestoplight.ui.screens.clickgui.ModButton;
import net.minecraft.client.gui.DrawContext;

public class ModeBox extends Component {
    private ModeSetting modeSetting;
    private int margin = (parent.parent.height / 2) - (mc.textRenderer.fontHeight / 2);

    public ModeBox(Setting setting, ModButton parent, int offset) {
        super(setting, parent, offset);

        this.modeSetting = (ModeSetting) setting;
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float deltaTicks) {
        super.render(drawContext, mouseX, mouseY, deltaTicks);

        drawContext.fill(parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y + parent.offset + offset + parent.parent.height, UIUtils.BACKGROUND_BASE.darker().getRGB());
        drawContext.drawTextWithShadow(mc.textRenderer, modeSetting.getName() + ": " + modeSetting.getMode(), parent.parent.x + margin, parent.parent.y + parent.offset + offset + margin, UIUtils.LIGHT.getRGB());
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        if (isHovered(mouseX, mouseY) && parent.extended && (button == 0)) {
            modeSetting.cycle();
        }
    }

    @Override
    public boolean isHovered(double mouseX, double mouseY) {
        return super.isHovered(mouseX, mouseY);
    }
}
