package hu.bluestoplight.modules.render;

import hu.bluestoplight.modules.Mod;
import hu.bluestoplight.modules.util.settings.NumberSetting;
import hu.bluestoplight.ui.UIUtils;

public class RainbowHue extends Mod {
    private static NumberSetting saturation = new NumberSetting("Saturation", 0.1, 1.0, 0.2, 0.05);
    private static NumberSetting brightness = new NumberSetting("Brightness", 0.1, 1.0, 1.0, 0.05);
    private static NumberSetting seconds = new NumberSetting("Loop Length", 1.0, 20.0, 12.0, 1.0);

    public RainbowHue() {
        super("RainbowHue", "Makes the client's primary color a shifting rainbow hue.", Category.RENDER);
        addSettings(seconds, saturation, brightness);
    }

    public static int getPrimaryColor() {
        return UIUtils.getColor(seconds.getIntValue(), saturation.getFloatValue(), brightness.getFloatValue());
    }

    @Override
    public void onDisable() {
        super.onDisable();

        UIUtils.isRainbowEnabled = false;
    }

    @Override
    public void onEnable() {
        super.onEnable();

        UIUtils.isRainbowEnabled = true;
    }
}