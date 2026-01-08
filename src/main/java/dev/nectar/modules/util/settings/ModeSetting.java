package dev.nectar.modules.util.settings;

import net.minecraft.nbt.NbtCompound;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ModeSetting extends Setting<String> {
    private List<String> modes;
    private int index;

    public ModeSetting(String name, String defaultMode, String... modes) {
        super(name, defaultMode);

        this.modes = Arrays.asList(modes);
        this.index = this.modes.indexOf(defaultMode);
    }

    public void setMode(String mode) {
        this.value = mode;
        this.index = modes.indexOf(mode);
    }

    public List<String> getModes() {
        return modes;
    }

    public String getMode() {
        return value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        this.value = modes.get(index);
    }

    public void cycle() {
        if (index < modes.size() - 1) {
            index++;

            value = modes.get(index);
        } else if (index >= modes.size() - 1) {
            index = 0;
            value = modes.getFirst();
        }
    }

    public boolean isMode(String mode) {
        return Objects.equals(this.value, mode);
    }

    @Override
    public NbtCompound save(NbtCompound tag) {
        tag.putString("value", getMode());

        return tag;
    }

    @Override
    public String load(NbtCompound tag) {
        setMode(tag.getString("value", ""));

        return getMode();
    }
}