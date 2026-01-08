/*
 * Copyright (c) 2025. TheBlueStopLight
 * All rights reserved.
 */

package hu.bluestoplight.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import hu.bluestoplight.ui.waypoints.Waypoint;
import hu.bluestoplight.ui.waypoints.WaypointManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Random;

public class WaypointCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, WaypointManager waypointManager) {
        SuggestionProvider<FabricClientCommandSource> waypointSuggestions = (context, builder) -> {
            List<Waypoint> currentWaypoints = waypointManager.getCurrentWorldWaypoints();
            return CommandSource.suggestMatching(currentWaypoints.stream().map(waypoint -> waypoint.name), builder);
        };

        dispatcher.register(ClientCommandManager.literal("sedna")
                .then(ClientCommandManager.literal("waypoint")
                        .then(ClientCommandManager.literal("add").then(ClientCommandManager.argument("name", StringArgumentType.greedyString())
                                .executes(context -> addWaypoint(context, waypointManager))))
                        .then(ClientCommandManager.literal("remote").then(ClientCommandManager.argument("x", IntegerArgumentType.integer()).then(ClientCommandManager.argument("y", IntegerArgumentType.integer()).then(ClientCommandManager.argument("z", IntegerArgumentType.integer()).then(ClientCommandManager.argument("name", StringArgumentType.greedyString())
                                .executes(context -> addRemoteWaypoint(context, waypointManager)))))))
                        .then(ClientCommandManager.literal("remove").then(ClientCommandManager.argument("name", StringArgumentType.greedyString()).suggests(waypointSuggestions)
                                .executes(context -> removeWaypoint(context, waypointManager))))
                        .then(ClientCommandManager.literal("get").then(ClientCommandManager.argument("name", StringArgumentType.greedyString()).suggests(waypointSuggestions)
                                .executes(context -> getWaypointLocation(context, waypointManager))))));
    }

    private static int addRemoteWaypoint(CommandContext<FabricClientCommandSource> context, WaypointManager waypointManager) {
        FabricClientCommandSource source = context.getSource();

        ClientPlayerEntity player = source.getPlayer();
        String name = StringArgumentType.getString(context, "name");
        int x = IntegerArgumentType.getInteger(context, "x");
        int y = IntegerArgumentType.getInteger(context, "y");
        int z = IntegerArgumentType.getInteger(context, "z");

        if (player == null) return 0;

        String dimension = player.getEntityWorld().getRegistryKey().getValue().toString();

        Random random = new Random();
        int color = (0xFF << 24) | (random.nextInt(256) << 16) | (random.nextInt(256) << 8) | random.nextInt(256);

        Waypoint waypoint = new Waypoint(name, x, y, z, dimension, color);
        waypointManager.addWaypoint(waypoint);

        source.sendFeedback(Text.literal("Sedna Waypoint has been saved: " + name).formatted(Formatting.AQUA));

        return 1;
    }

    private static int getWaypointLocation(CommandContext<FabricClientCommandSource> context, WaypointManager waypointManager) {
        FabricClientCommandSource source = context.getSource();
        String name = StringArgumentType.getString(context, "name");

        if (source.getPlayer() == null) return 0;

        Vec3d waypointLocation = waypointManager.getWaypointLocation(name);
        if (waypointLocation == null) {
            source.sendError(Text.literal("Error looking for Sedna Waypoint: " + name).formatted(Formatting.YELLOW));
            return 0;
        }

        source.sendFeedback(Text.literal("Sedna Waypoint [" + name + "]: " + Math.round(waypointLocation.x) + " " + Math.round(waypointLocation.y) + " " + Math.round(waypointLocation.z)).formatted(Formatting.AQUA));
        return 1;
    }

    private static int removeWaypoint(CommandContext<FabricClientCommandSource> context, WaypointManager waypointManager) {
        FabricClientCommandSource source = context.getSource();
        String name = StringArgumentType.getString(context, "name");

        boolean success = waypointManager.removeWaypoint(name);

        if (success) {
            source.sendFeedback(Text.literal("Sedna Waypoint has been deleted: " + name).formatted(Formatting.AQUA));
            return 1;
        } else {
            source.sendError(Text.literal("Sedna Waypoint not found: " + name).formatted(Formatting.YELLOW));
            return 0;
        }
    }

    private static int addWaypoint(CommandContext<FabricClientCommandSource> context, WaypointManager waypointManager) {
        FabricClientCommandSource source = context.getSource();

        ClientPlayerEntity player = source.getPlayer();
        String name = StringArgumentType.getString(context, "name");

        if (player == null) return 0;

        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        String dimension = player.getEntityWorld().getRegistryKey().getValue().toString();

        Random random = new Random();

        int color = (0xFF << 24) | (random.nextInt(256) << 16) | (random.nextInt(256) << 8) | random.nextInt(256);

        Waypoint waypoint = new Waypoint(name, x, y, z, dimension, color);
        waypointManager.addWaypoint(waypoint);

        source.sendFeedback(Text.literal("Sedna Waypoint has been saved: " + name).formatted(Formatting.AQUA));

        return 1;
    }
}
