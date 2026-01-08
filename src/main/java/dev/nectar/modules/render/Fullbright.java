/*
 * Copyright (c) 2025. TheBlueStopLight
 * All rights reserved.
 */

package dev.nectar.modules.render;

import dev.nectar.modules.Module;

public class Fullbright extends Module {
    public static boolean status = false;

    public Fullbright() {
        super("Fullbright", "I'm scared in the dark :(", Category.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        status = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();

        status = false;
    }
}
