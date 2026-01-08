/*
 * Copyright (c) 2025. TheBlueStopLight
 * All rights reserved.
 */

package hu.bluestoplight.modules.render;

import hu.bluestoplight.modules.Mod;

public class Fullbright extends Mod {
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
