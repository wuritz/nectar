package dev.nectar.modules.world;

import dev.nectar.events.core.render.Render2DEvent;
import dev.nectar.modules.Module;
import dev.nectar.modules.util.settings.BooleanSetting;
import dev.nectar.modules.util.settings.ModeSetting;
import dev.nectar.ui.UIUtils;
import meteordevelopment.orbit.EventHandler;

import java.time.LocalDateTime;

import static dev.nectar.Nectar.mc;

public class Clock extends Module {
    private static String dayString = "";

    private static ModeSetting mode = new ModeSetting("Mode", "Both", "In-Game", "Real World", "Both");
    private static ModeSetting format = new ModeSetting("Format", "24", "12", "24");
    private static BooleanSetting showDayCount = new BooleanSetting("Show Day Count", true);

    public Clock() {
        super("Clock", "Displays in-game, and computer time.", Category.WORLD);
        addSettings(mode, format, showDayCount);
    }

    public static String getClockString() {
        if (mode.isMode("In-Game")) {
            return getGameTime();
        } else if (mode.isMode("Real World")) {
            return getRealTime();
        } else if (mode.isMode("Both")) {
            return getGameTime() + " | " + getRealTime();
        } else {
            return "N/A";
        }
    }

    public static boolean shouldShowDayCount() {
        return showDayCount.isEnabled();
    }

    public static String getDayString() {
        return dayString;
    }

    private static String getRealTime() {
        int hour = LocalDateTime.now().getHour();
        int minutes = LocalDateTime.now().getMinute();

        String suffix = "";

        if (!format.isMode("24")) {
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

    private static String getGameTime() {
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

        if (!format.isMode("24")) {
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

        if (!format.isMode("24") && stringSplit[0].equals("0")) {
            stringTime = new StringBuilder(stringSplit[1] + ":" + stringMins.charAt(0) + stringMins.charAt(1));
        } else {
            stringTime = new StringBuilder(stringSplit[0] + stringSplit[1] + ":" + stringMins.charAt(0) + stringMins.charAt(1));
        }

        return stringTime + suffix;
    }

    @EventHandler
    public void onRender(Render2DEvent event) {
        event.drawContext.fill(0, mc.textRenderer.fontHeight + UIUtils.margin, mc.textRenderer.getWidth(Clock.getClockString()) + UIUtils.margin + (UIUtils.margin / 4), 2 * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.BACKGROUND_BASE.getRGB());
        event.drawContext.fill(mc.textRenderer.getWidth(Clock.getClockString()) + UIUtils.margin, mc.textRenderer.fontHeight + UIUtils.margin, mc.textRenderer.getWidth(Clock.getClockString()) + UIUtils.margin + (UIUtils.margin / 4), 2 * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.getSelectedPrimaryColor().getRGB());

        event.drawContext.drawTextWithShadow(mc.textRenderer, Clock.getClockString(), UIUtils.margin / 2, (UIUtils.margin / 2) + (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.getSelectedPrimaryColor().getRGB());

        if (Clock.shouldShowDayCount()) {
            event.drawContext.fill(0, 2 * (mc.textRenderer.fontHeight + UIUtils.margin), mc.textRenderer.getWidth(Clock.getDayString()) + UIUtils.margin + (UIUtils.margin / 4), 3 * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.BACKGROUND_BASE.getRGB());
            event.drawContext.fill(mc.textRenderer.getWidth(Clock.getDayString()) + UIUtils.margin, 2 * (mc.textRenderer.fontHeight + UIUtils.margin), mc.textRenderer.getWidth(Clock.getDayString()) + UIUtils.margin + (UIUtils.margin / 4), 3 * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.getSelectedPrimaryColor().getRGB());

            event.drawContext.drawTextWithShadow(mc.textRenderer, Clock.getDayString(), UIUtils.margin / 2, (UIUtils.margin / 2) + 2 * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.LIGHT.darker().getRGB());
        }
    }
}