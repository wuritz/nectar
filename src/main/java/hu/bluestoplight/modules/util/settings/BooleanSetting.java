package hu.bluestoplight.modules.util.settings;

import net.minecraft.nbt.NbtCompound;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String name, boolean defaultValue) {
        super(name, defaultValue);
    }

    public void toggle() {
        this.value = !this.value;
    }

    public boolean isEnabled() {
        return value;
    }

    public void setEnabled(boolean enabled) {
        this.value = enabled;
    }

    @Override
    public NbtCompound save(NbtCompound tag) {
        tag.putBoolean("value", isEnabled());

        return tag;
    }

    @Override
    public Boolean load(NbtCompound tag) {
        setEnabled(tag.getBoolean("value", false));

        return isEnabled();
    }

}