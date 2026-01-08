/*
 * Copyright (c) 2025. TheBlueStopLight
 * All rights reserved.
 */

package hu.bluestoplight.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import hu.bluestoplight.modules.player.Zoom;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyReturnValue(method = "getFov", at = @At("RETURN"))
    private float modifyFov(float fov, @Local(argsOnly = true) float tickDelta) {
        return fov * Zoom.getZoomModifier();
    }
}