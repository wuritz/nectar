package dev.nectar.ui.waypoints;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.nectar.Nectar;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.math.Vec3d;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaypointManager {
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("sedna_waypoints.json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, List<Waypoint>> waypoints = new HashMap<>();

    public WaypointManager() {
        load();
    }

    public void addWaypoint(Waypoint waypoint) {
        String worldID = getCurrentWorldID();

        waypoints.computeIfAbsent(worldID, k -> new ArrayList<>()).add(waypoint);
        save();
    }

    private String getCurrentWorldID() {
        if (Nectar.mc.isInSingleplayer() && Nectar.mc.getServer() != null) {
            return "singleplayer:" + Nectar.mc.getServer().getSaveProperties().getLevelName();
        } else if (Nectar.mc.getCurrentServerEntry() != null) {
            if (Nectar.mc.getServer() == null) {
                return "realm:" + Nectar.mc.getCurrentServerEntry().name;
            } else {
                return "multiplayer:" + Nectar.mc.getCurrentServerEntry().address;
            }
        } else {
            return "global";
        }
    }

    public List<Waypoint> getCurrentWorldWaypoints() {
        String worldID = getCurrentWorldID();

        return waypoints.getOrDefault(worldID, new ArrayList<>());
    }

    public Vec3d getWaypointLocation(String name) {
        for (Waypoint waypoint : getCurrentWorldWaypoints()) {
            if (waypoint.name.equals(name)) return new Vec3d(waypoint.x, waypoint.y, waypoint.z);
        }

        return null;
    }

    public void save() {
        try (Writer writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(waypoints, writer);
        } catch (IOException e) {
            Nectar.LOGGER.error("[SEDNA]: Error in Waypoint Manager! " + e.toString());
        }
    }

    public void load() {
        if (!CONFIG_FILE.exists()) return;

        try (Reader reader = new FileReader(CONFIG_FILE)) {
            Map<String, List<Waypoint>> loaded = GSON.fromJson(reader, new TypeToken<Map<String, List<Waypoint>>>(){}.getType());
            if (loaded != null) {
                waypoints.clear();
                waypoints.putAll(loaded);
            }
        } catch (IOException e) {
            Nectar.LOGGER.error("[SEDNA]: Error in Waypoint Manager! " + e.toString());
        }
    }

    public boolean removeWaypoint(String query) {
        String worldID = getCurrentWorldID();
        List<Waypoint> worldWaypoints = waypoints.get(worldID);

        if (worldWaypoints == null || worldWaypoints.isEmpty()) return false;

        boolean removed = worldWaypoints.removeIf(waypoint -> waypoint.name.equals(query));

        if (removed) save();

        return removed;
    }
}
