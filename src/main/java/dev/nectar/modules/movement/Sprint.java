package dev.nectar.modules.movement;

import dev.nectar.events.world.TickEvent;
import dev.nectar.modules.Module;
import dev.nectar.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

import static dev.nectar.Nectar.mc;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", "Automatically sprints for you.", Category.MOVEMENT);
    }

    @EventHandler
    public void onTick(TickEvent.Post event) {
        if (!Utils.isToggleable()) return;

        if (mc.player.forwardSpeed > 0) {
            mc.player.setSprinting(true);
        } else { // Possibly not necessary
            mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
            mc.player.setSprinting(false);
        }
    }
}