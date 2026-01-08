package hu.bluestoplight.ui.screens.clickgui.settings;

import hu.bluestoplight.modules.util.settings.BooleanSetting;
import hu.bluestoplight.modules.util.settings.Setting;
import hu.bluestoplight.ui.UIUtils;
import hu.bluestoplight.ui.screens.clickgui.ModButton;
import net.minecraft.client.gui.DrawContext;

public class CheckBox extends Component {
    private BooleanSetting booleanSetting;
    private int margin = (parent.parent.height / 2) - (mc.textRenderer.fontHeight / 2);
    private int checkIconMargin = 4;

    public CheckBox(Setting setting, ModButton parent, int offset) {
        super(setting, parent, offset);
        this.booleanSetting = (BooleanSetting) setting;
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float deltaTicks) {
        super.render(drawContext, mouseX, mouseY, deltaTicks);

        drawContext.fill(parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y + parent.offset + offset + parent.parent.height, UIUtils.BACKGROUND_BASE.darker().getRGB());
        drawContext.drawTextWithShadow(mc.textRenderer, booleanSetting.getName(), parent.parent.x + margin, parent.parent.y + parent.offset + offset + margin, UIUtils.LIGHT.getRGB());

        if (booleanSetting.isEnabled()) {
            drawContext.fill(parent.parent.x + parent.parent.width - mc.textRenderer.getWidth("ˇ") - checkIconMargin, parent.parent.y + parent.offset + offset + checkIconMargin, parent.parent.x + parent.parent.width - checkIconMargin, parent.parent.y + parent.offset + offset + parent.parent.height - checkIconMargin, UIUtils.getSelectedPrimaryColor().getRGB());
        } else {
            drawContext.fill(parent.parent.x + parent.parent.width - mc.textRenderer.getWidth("ˇ") - checkIconMargin, parent.parent.y + parent.offset + offset + checkIconMargin, parent.parent.x + parent.parent.width - checkIconMargin, parent.parent.y + parent.offset + offset + parent.parent.height - checkIconMargin, UIUtils.BACKGROUND_MID.getRGB());
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        if (isHovered(mouseX, mouseY) && parent.extended && button == 0) booleanSetting.toggle();
    }
}
