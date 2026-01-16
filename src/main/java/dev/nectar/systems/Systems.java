package dev.nectar.systems;

import dev.nectar.Nectar;
import dev.nectar.events.game.GameLeftEvent;
import dev.nectar.modules.Modules;
import dev.nectar.systems.hud.HUD;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import meteordevelopment.orbit.EventHandler;

import java.io.File;
import java.util.Map;

public class Systems {

    @SuppressWarnings("rawtypes")
    private static final Map<Class<? extends System>, System<?>> systems = new Reference2ReferenceOpenHashMap<>();

    public static void init() {
        add(new Modules());
        add(new HUD());

        // TODO: Implement Waypoints
        Nectar.EVENT_BUS.subscribe(Systems.class);
    }

    public static System<?> add(System<?> system) {
        systems.put(system.getClass(), system);
        Nectar.EVENT_BUS.subscribe(system);
        system.init();

        return system;
    }

    @EventHandler
    private static void onGameLeft(GameLeftEvent event) {
        save();
    }

    public static void save(File folder) {
        long start = java.lang.System.currentTimeMillis();
        Nectar.LOG.info("Saving systems...");

        for (System<?> system : systems.values()) system.save(folder);

        Nectar.LOG.info("Saved in {} milliseconds.", java.lang.System.currentTimeMillis() - start);
    }

    public static void save() {
        save(null);
    }

    public static void load(File folder) {
        long start = java.lang.System.currentTimeMillis();
        Nectar.LOG.info("Loading systems...");

        for (System<?> system : systems.values()) system.load(folder);

        Nectar.LOG.info("Loaded in {} milliseconds", java.lang.System.currentTimeMillis() - start);
    }

    public static void load() {
        load(null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends System<?>> T get(Class<T> klass) {
        return (T) systems.get(klass);
    }

}
