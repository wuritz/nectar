package hu.bluestoplight.modules.util.settings;

import hu.bluestoplight.core.input.Keybind;
import net.minecraft.nbt.NbtCompound;

public class KeybindSetting extends Setting<Keybind> {

    private final Keybind keybind;
    private int value;

    public KeybindSetting(String name, Keybind defaultValue) {
        super(name, defaultValue);

        this.keybind = Keybind.fromKey(value);
    }

    public void setIntValue(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return value;
    }

    public Keybind getKeybind() {
        return this.keybind;
    }

    @Override
    public NbtCompound save(NbtCompound tag) {
        tag.put("value", getKeybind().toTag());

        return tag;
    }

    @Override
    public Keybind load(NbtCompound tag) {
        getKeybind().fromTag(tag.getCompoundOrEmpty("value"));

        return getKeybind();
    }

}
