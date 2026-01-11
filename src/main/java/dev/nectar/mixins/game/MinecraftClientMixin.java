package dev.nectar.mixins.game;

import dev.nectar.Nectar;
import dev.nectar.events.game.GameLeftEvent;
import dev.nectar.events.world.TickEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.profiler.Profilers;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Unique private boolean doItemUseCalled;
    @Unique private boolean rightClick;
    @Unique private boolean firstFrame;

    @Shadow public ClientWorld world;

    @Shadow
    @Nullable
    public ClientPlayerInteractionManager interactionManager;

    @Shadow protected abstract void doItemUse();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(CallbackInfo info) {
        Nectar.INSTANCE.onInitialize();
        firstFrame = true;
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void onPreTick(CallbackInfo info) {
        doItemUseCalled = false;

        Profilers.get().push(Nectar.MOD_ID + "_pre_update");
        Nectar.EVENT_BUS.post(TickEvent.Pre.get());
        Profilers.get().pop();

        if (rightClick && !doItemUseCalled && interactionManager != null) doItemUse();
        rightClick = false;
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void onTick(CallbackInfo info) {
        Profilers.get().push(Nectar.MOD_ID + "_post_update");
        Nectar.EVENT_BUS.post(TickEvent.Post.get());
        Profilers.get().pop();
    }

    @Inject(method = "doItemUse", at = @At("HEAD"))
    private void onDoItemUse(CallbackInfo info) {
        doItemUseCalled = true;
    }

    @Inject(method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;ZZ)V", at = @At("HEAD"))
    private void onDisconnect(Screen screen, boolean transferring, boolean stopSound, CallbackInfo info) {
        if (world != null) {
            Nectar.EVENT_BUS.post(GameLeftEvent.get());
        }
    }
}