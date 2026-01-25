package dev.nectar.modules.world;

import dev.nectar.events.core.render.Render2DEvent;
import dev.nectar.modules.Module;
import dev.nectar.modules.setting.Setting;
import dev.nectar.modules.setting.settings.BooleanSetting;
import dev.nectar.modules.setting.settings.EnumSetting;
import dev.nectar.ui.UIUtils;
import meteordevelopment.orbit.EventHandler;

import java.time.LocalDateTime;

import static dev.nectar.Nectar.mc;

public class Clock extends Module {
    private static String dayString = "";

    private final Setting<Mode> mode = new EnumSetting.Builder<Mode>()
            .name("Mode").description("")
            .defaultValue(Mode.Both)
            .build();

    private final Setting<Format> format = new EnumSetting.Builder<Format>()
            .name("Format").description("")
            .defaultValue(Format.Europe)
            .build();

    private final Setting<Boolean> showDayCount = new BooleanSetting.Builder()
            .name("Show day count").description("")
            .defaultValue(true)
            .build();

    public Clock() {
        super("Clock", "Displays in-game, and computer time.", Category.WORLD);

        addSettings(mode, format, showDayCount);
    }

    public String getClockString() {
        switch (mode.get()) {
            case InGame -> getGameTime();
            case IRL -> getRealTime();
            case Both -> {
                return getGameTime() + " | " + getRealTime();
            }
        }

        return "N/A";
    }

    public boolean shouldShowDayCount() {
        return showDayCount.get();
    }

    public String getDayString() {
        return dayString;
    }

    private String getRealTime() {
        int hour = LocalDateTime.now().getHour();
        int minutes = LocalDateTime.now().getMinute();

        String suffix = "";

        if (format.get() == Format.Europe) {
            if (hour >= 13) {
                hour = hour - 12;
                suffix = " PM";
            } else {
                if (hour >= 12) {
                    suffix = " PM";
                } else {
                    suffix = " AM";
                    if (hour <= 0.999) {
                        hour += 12;
                    }
                }
            }
        }

        String stringMinutes;
        String stringHour;

        if (minutes < 10) {
            stringMinutes = "0" + minutes;
        } else {
            stringMinutes = String.valueOf(minutes);
        }

        if (hour < 10) {
            stringHour = "0" + hour;
        } else {
            stringHour = String.valueOf(hour);
        }

        return stringHour + ":" + stringMinutes + suffix;
    }

    private String getGameTime() {
        int time;
        int gameTime = (int) mc.world.getTimeOfDay();
        int daysPlayed = 0;

        while (gameTime >= 24000) {
            gameTime -= 24000;
            daysPlayed++;
        }

        dayString = "Day " + daysPlayed;

        if (gameTime >= 18000) {
            time = gameTime - 18000;
        } else {
            time = 6000 + gameTime;
        }

        String suffix = "";

        if (format.get() != Format.Europe) {
            if (time >= 13000) {
                time = time - 12000;
                suffix = " PM";
            } else {
                if (time >= 12000) {
                    suffix = " PM";
                } else {
                    suffix = " AM";
                    if (time <= 999) {
                        time += 12000;
                    }
                }
            }
        }

        StringBuilder stringTime = new StringBuilder(time / 10 + "");
        for (int i = stringTime.length(); i < 4; i++) {
            stringTime.insert(0, "0");
        }

        String[] stringSplit = stringTime.toString().split("");

        int mins = (int) Math.floor(Double.parseDouble(stringSplit[2] + stringSplit[3]) / 100 * 60);
        String stringMins = mins + "";

        if (mins < 10) {
            stringMins = "0" + mins;
        }

        if (format.get() != Format.Europe && stringSplit[0].equals("0")) {
            stringTime = new StringBuilder(stringSplit[1] + ":" + stringMins.charAt(0) + stringMins.charAt(1));
        } else {
            stringTime = new StringBuilder(stringSplit[0] + stringSplit[1] + ":" + stringMins.charAt(0) + stringMins.charAt(1));
        }

        return stringTime + suffix;
    }

    @EventHandler
    public void onRender(Render2DEvent event) {
        event.drawContext.fill(0, mc.textRenderer.fontHeight + UIUtils.margin, mc.textRenderer.getWidth(getClockString()) + UIUtils.margin + (UIUtils.margin / 4), 2 * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.BACKGROUND_BASE.getRGB());
        event.drawContext.fill(mc.textRenderer.getWidth(getClockString()) + UIUtils.margin, mc.textRenderer.fontHeight + UIUtils.margin, mc.textRenderer.getWidth(getClockString()) + UIUtils.margin + (UIUtils.margin / 4), 2 * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.getSelectedPrimaryColor().getRGB());

        event.drawContext.drawTextWithShadow(mc.textRenderer, getClockString(), UIUtils.margin / 2, (UIUtils.margin / 2) + (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.getSelectedPrimaryColor().getRGB());

        if (shouldShowDayCount()) {
            event.drawContext.fill(0, 2 * (mc.textRenderer.fontHeight + UIUtils.margin), mc.textRenderer.getWidth(getDayString()) + UIUtils.margin + (UIUtils.margin / 4), 3 * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.BACKGROUND_BASE.getRGB());
            event.drawContext.fill(mc.textRenderer.getWidth(getDayString()) + UIUtils.margin, 2 * (mc.textRenderer.fontHeight + UIUtils.margin), mc.textRenderer.getWidth(getDayString()) + UIUtils.margin + (UIUtils.margin / 4), 3 * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.getSelectedPrimaryColor().getRGB());

            event.drawContext.drawTextWithShadow(mc.textRenderer, getDayString(), UIUtils.margin / 2, (UIUtils.margin / 2) + 2 * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.LIGHT.darker().getRGB());
        }
    }

    private enum Mode {
        Both, InGame, IRL
    }

    private enum Format {
        Europe, AM
    }
}