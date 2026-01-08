/*
 * Copyright (c) 2025. TheBlueStopLight
 * All rights reserved.
 */

package hu.bluestoplight.mixins;

import hu.bluestoplight.SednaClient;
import hu.bluestoplight.core.input.Input;
import hu.bluestoplight.core.input.KeyAction;
import hu.bluestoplight.events.core.KeyEvent;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyInput;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int action, KeyInput input, CallbackInfo ci) {
        int modifiers = input.modifiers();

        if (input.key() != GLFW.GLFW_KEY_UNKNOWN) {
            if (action == GLFW.GLFW_PRESS) {
                modifiers |= Input.getModifier(input.key());
            } else if (action == GLFW.GLFW_RELEASE) {
                modifiers &= ~Input.getModifier(input.key());
            }

            Input.setKeyState(input.key(), action != GLFW.GLFW_RELEASE);
            if (SednaClient.EVENT_BUS.post(KeyEvent.get(new KeyInput(input.key(), input.scancode(), modifiers), KeyAction.get(action))).isCancelled())
                ci.cancel();
        }
    }
}