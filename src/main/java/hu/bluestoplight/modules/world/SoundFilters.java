package hu.bluestoplight.modules.world;

import hu.bluestoplight.SednaClient;
import hu.bluestoplight.audio.SoundProcessor;
import hu.bluestoplight.events.world.TickEvent;
import hu.bluestoplight.modules.Mod;
import hu.bluestoplight.utils.Utils;
import meteordevelopment.orbit.EventHandler;

import static hu.bluestoplight.SednaClient.mc;

public class SoundFilters extends Mod {
    public SoundFilters() {
        super("SoundFilters", "Adds basic sound physics.", Category.WORLD);
    }

    @EventHandler
    public void onTick(TickEvent.Pre event) {
        if (Utils.isToggleable())
            SoundProcessor.update(mc.player);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (SednaClient.INSTANCE.hasLoaded()) SoundProcessor.initializeOpenAL();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (SednaClient.INSTANCE.hasLoaded()) SoundProcessor.cleanUpOpenAL();
    }
}
