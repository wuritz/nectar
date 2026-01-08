/*
 * Copyright (c) 2025. TheBlueStopLight
 * All rights reserved.
 */

package hu.bluestoplight.modules.combat;

import hu.bluestoplight.events.world.TickEvent;
import hu.bluestoplight.modules.Mod;
import hu.bluestoplight.modules.util.settings.NumberSetting;
import hu.bluestoplight.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import static hu.bluestoplight.SednaClient.mc;

public class AutoLog extends Mod {
    private static final NumberSetting entityThreshold = new NumberSetting("Entities", 1, 100, 35, 1);
    private static final NumberSetting healthThreshold = new NumberSetting("Health", 1, 19, 2, 0.5f);
    private static final NumberSetting entityRange = new NumberSetting("Range", 2.0f, 50.0f, 7.0f, 1.0f);

    public AutoLog() {
        super("AutoLog", "Disconnect from server when something sketchy happens.", Category.COMBAT);
        addSettings(entityThreshold, healthThreshold, entityRange);
    }

    @EventHandler
    public void onTick(TickEvent.Pre event) {
        if (!Utils.isToggleable()) return;
        if (mc.player.isDead()) return;

        int entities = 0;
        for (Entity entity : mc.world.getEntities()) {
            if (entity == mc.player) continue;
            if (!(entity instanceof LivingEntity)) continue;

            double dist = entity.getEntityPos().distanceTo(mc.player.getEntityPos());
            if (dist > entityRange.getValue()) continue;

            entities++;
        }

        if (entities >= entityThreshold.getIntValue()) disconnect();
        if (mc.player.getHealth() <= healthThreshold.getFloatValue()) disconnect();
    }

    private void disconnect() {
        MutableText reason = Text.literal("[AutoLog]: Disconnected because something spooky has happened! ooOOOOOoOOOoo");
        mc.getNetworkHandler().onDisconnect(new DisconnectS2CPacket(reason));
    }
}
