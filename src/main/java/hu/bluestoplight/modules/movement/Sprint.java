package hu.bluestoplight.modules.movement;

import hu.bluestoplight.events.world.TickEvent;
import hu.bluestoplight.modules.Mod;
import hu.bluestoplight.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;

import static hu.bluestoplight.SednaClient.mc;

public class Sprint extends Mod {
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