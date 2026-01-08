package hu.bluestoplight.systems;

import hu.bluestoplight.SednaClient;
import hu.bluestoplight.events.game.GameLeftEvent;
import hu.bluestoplight.modules.ModManager;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import meteordevelopment.orbit.EventHandler;

import java.io.File;
import java.util.Map;

public class Systems {

    @SuppressWarnings("rawtypes")
    private static final Map<Class<? extends System>, System<?>> systems = new Reference2ReferenceOpenHashMap<>();

    public static void init() {
        add(new ModManager());

        // TODO: Implement Waypoints
        SednaClient.EVENT_BUS.subscribe(Systems.class);
    }

    public static System<?> add(System<?> system) {
        systems.put(system.getClass(), system);
        SednaClient.EVENT_BUS.subscribe(system);
        system.init();

        return system;
    }

    @EventHandler
    private static void onGameLeft(GameLeftEvent event) {
        save();
    }

    public static void save(File folder) {
        long start = java.lang.System.currentTimeMillis();
        SednaClient.LOGGER.info("Saving");

        for (System<?> system : systems.values()) system.save(folder);

        SednaClient.LOGGER.info("Saved in {} milliseconds.", java.lang.System.currentTimeMillis() - start);
    }

    public static void save() {
        save(null);
    }

    public static void load(File folder) {
        long start = java.lang.System.currentTimeMillis();
        SednaClient.LOGGER.info("Loading systems...");

        for (System<?> system : systems.values()) system.load(folder);

        SednaClient.LOGGER.info("Loaded in {} milliseconds", java.lang.System.currentTimeMillis() - start);
    }

    public static void load() {
        load(null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends System<?>> T get(Class<T> klass) {
        return (T) systems.get(klass);
    }

}
