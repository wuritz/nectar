package dev.nectar.modules.render;

import dev.nectar.modules.Module;
import dev.nectar.modules.setting.Setting;
import dev.nectar.modules.setting.settings.DoubleSetting;
import dev.nectar.ui.UIUtils;

public class RainbowHue extends Module {

    private final Setting<Double> saturation = new DoubleSetting.Builder()
            .name("Saturation").description("")
            .min(0.1d).max(1d)
            .defaultValue(0.2d)
            .build();

    private final Setting<Double> brightness = new DoubleSetting.Builder()
            .name("Brightness").description("")
            .min(0.1d).max(1d)
            .defaultValue(1d)
            .build();

    private final Setting<Double> seconds = new DoubleSetting.Builder()
            .name("Loop Length").description("")
            .min(1d).max(20d)
            .defaultValue(12d)
            .build();

    public RainbowHue() {
        super("RainbowHue", "Makes the client's primary color a shifting rainbow hue.", Category.RENDER);

        addSettings(seconds, saturation, brightness);
    }

    public int getPrimaryColor() {
        return UIUtils.getColor(seconds.get().intValue(), saturation.get().floatValue(), brightness.get().floatValue());
    }

    @Override
    public void onDisable() {
        UIUtils.isRainbowEnabled = false;
    }

    @Override
    public void onEnable() {
        UIUtils.isRainbowEnabled = true;
    }
}