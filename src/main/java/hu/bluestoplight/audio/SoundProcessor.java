package hu.bluestoplight.audio;

import hu.bluestoplight.SednaClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.EXTEfx;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import static hu.bluestoplight.SednaClient.mc;

public class SoundProcessor {
    private static List<SoundInstance> sounds = new CopyOnWriteArrayList<>();

    private static boolean currentlyIndoors;
    private static int reverbSlot;
    private static int reverb;

    public static float getDecayTime() {
        return MathHelper.clamp(getSpaceFactor() / 10f, 0.7f, 5.0f);
    }

    public static float getSpaceFactor() {
        int radius = 20;
        int openFactor = 0;

        for (Direction dir : Direction.values()) {
            int dist = 0;

            while (dist < radius && SednaClient.mc.world.isAir(SednaClient.mc.player.getBlockPos().offset(dir, dist))) {
                dist++;
            }

            openFactor += dist;
        }

        return openFactor;
    }

    public static boolean isIndoors(ClientPlayerEntity player, SoundInstance sound) {
        if (player == null) return false;

        if (!(player.getEntityWorld().isSkyVisible(player.getBlockPos()))) {
            return true;
        }

       return false;
    }

    public static void initializeOpenAL() {
        reverbSlot = EXTEfx.alGenAuxiliaryEffectSlots();
        checkALErrors("alGenAuxiliaryEffectSlots");

        reverb = EXTEfx.alGenEffects();
        checkALErrors("alGenEffects");

        EXTEfx.alEffecti(reverb, EXTEfx.AL_EFFECT_TYPE, EXTEfx.AL_EFFECT_REVERB);
        checkALErrors("alEffecti - Reverb");

        EXTEfx.alEffectf(reverb, EXTEfx.AL_REVERB_DECAY_TIME, 0.7f);
        EXTEfx.alEffectf(reverb, EXTEfx.AL_REVERB_DENSITY, 0.7f);
        EXTEfx.alEffectf(reverb, EXTEfx.AL_REVERB_GAIN, 0.8f);
        checkALErrors("alEffectf");

        EXTEfx.alAuxiliaryEffectSloti(reverbSlot, EXTEfx.AL_EFFECTSLOT_EFFECT, reverb);
        checkALErrors("alAuxiliaryEffectSloti");
    }

    public static void cleanUpOpenAL() {
        EXTEfx.alDeleteEffects(reverb);
        EXTEfx.alDeleteAuxiliaryEffectSlots(reverbSlot);
    }

    public static void update(ClientPlayerEntity player) {
        if (mc.player == null || mc.player.getEntityWorld() == null) {
            sounds.clear();
            return;
        }

        sounds.removeIf(Objects::isNull);

        for (SoundInstance i : sounds) {
            if (currentlyIndoors != isIndoors(player, i)) {
                if (isIndoors(player, i)) {
                    EXTEfx.alEffectf(reverb, EXTEfx.AL_REVERB_DECAY_TIME, getDecayTime());
                    checkALErrors("alEffectf - Reverb Profile Update");
                } else {
                    EXTEfx.alEffectf(reverb, EXTEfx.AL_REVERB_DECAY_TIME, 0.7f);
                    checkALErrors("alEffectf - Reverb Profile Update");
                }

                EXTEfx.alAuxiliaryEffectSloti(reverbSlot, EXTEfx.AL_EFFECTSLOT_EFFECT, reverb);
                checkALErrors("alAuxiliaryEffectSloti - Reattach Reverb Profile Update");

                currentlyIndoors = isIndoors(player, i);
            } else {
                if (isIndoors(player, i)) {
                    EXTEfx.alEffectf(reverb, EXTEfx.AL_REVERB_DECAY_TIME, getDecayTime());
                    checkALErrors("alEffectf - Reverb Profile Update");

                    EXTEfx.alAuxiliaryEffectSloti(reverbSlot, EXTEfx.AL_EFFECTSLOT_EFFECT, reverb);
                    checkALErrors("alAuxiliaryEffectSloti - Reattach Reverb Profile Update");
                }
            }
        }
    }

    public static void applyFilter(SoundInstance sound, int source) {
        if (!AL10.alIsSource(source)) {
            System.err.println("Invalid source ID: " + source);
            return;
        }

        sounds.add(sound);

        AL11.alSource3i(source, EXTEfx.AL_AUXILIARY_SEND_FILTER, reverbSlot, 0, EXTEfx.AL_FILTER_NULL);
        checkALErrors("alSource3i - Reverb");
    }

    private static void checkALErrors(String action) {
        int error = AL10.alGetError();
        if (error != AL10.AL_NO_ERROR) {
            throw new RuntimeException("OpenAL error during " + action + ": " + error);
        }
    }
}