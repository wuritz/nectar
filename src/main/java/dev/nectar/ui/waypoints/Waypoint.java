/*
 * Copyright (c) 2025. TheBlueStopLight
 * All rights reserved.
 */

package dev.nectar.ui.waypoints;

public class Waypoint {
    public String name;
    public double x, y, z;
    public String dimension;
    public int color;

    public Waypoint(String name, double x, double y, double z, String dimension, int color) {
        this.name = name;
        this.dimension = dimension;
        this.color = color;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
