/*
 * Copyright (c) 2025. TheBlueStopLight
 * All rights reserved.
 */

package hu.bluestoplight.mixins;

import hu.bluestoplight.audio.SourceGetter;
import net.minecraft.client.sound.Source;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Source.class)
public class SourceMixin implements SourceGetter {
    @Shadow
    @Final
    private int pointer;

    @Override
    public int getID() {
        return pointer;
    }
}
