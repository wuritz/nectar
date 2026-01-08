/*
 * Copyright (c) 2025. TheBlueStopLight
 * All rights reserved.
 */

package hu.bluestoplight.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import hu.bluestoplight.SednaClient;
import hu.bluestoplight.audio.SoundProcessor;
import hu.bluestoplight.audio.SourceGetter;
import hu.bluestoplight.modules.Mod;
import hu.bluestoplight.modules.ModManager;
import net.minecraft.client.sound.Channel;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(SoundSystem.class)
@SuppressWarnings("rawtypes")
public abstract class SoundSystemMixin {
    @Inject(
            method = "play(Lnet/minecraft/client/sound/SoundInstance;)Lnet/minecraft/client/sound/SoundSystem$PlayResult;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/sound/Channel$SourceManager;run(Ljava/util/function/Consumer;)V"
            )
    )

    public void onPlay(SoundInstance sound, CallbackInfoReturnable<SoundSystem.PlayResult> cir, @Local Channel.SourceManager sourceManager) {
        sourceManager.run(source -> {
            SourceGetter sourceGetter = (SourceGetter) source;

            manageSoundPlay(sound, sourceGetter.getID());
        });
    }

    private void manageSoundPlay(SoundInstance sound, int source) {
        for (Mod i : ModManager.get().getEnabled()) {
            if (Objects.equals(i.getName(), "SoundFilters")) {
                if (sound.getCategory() == SoundCategory.MUSIC || sound.getCategory() == SoundCategory.MASTER) return;

                if (SednaClient.mc.player == null) return;

                SoundProcessor.applyFilter(sound, source);
            }
        }
    }
}
