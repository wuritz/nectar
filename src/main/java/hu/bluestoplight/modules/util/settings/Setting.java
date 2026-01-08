package hu.bluestoplight.modules.util.settings;

import hu.bluestoplight.utils.misc.interfaces.ISerializable;
import net.minecraft.nbt.NbtCompound;

public abstract class Setting<T> implements ISerializable<Setting<T>> {

    protected String name;
    protected boolean visible = true;

    protected final T defaultValue;
    protected T value;

    public Setting(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;

        reset();
    }

    public void reset() {
        value = defaultValue;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected abstract NbtCompound save(NbtCompound tag);
    protected abstract T load(NbtCompound tag);

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();

        tag.putString("name", name);
        save(tag);

        return tag;
    }

    @Override
    public Setting<T> fromTag(NbtCompound tag) {
        load(tag);

        return this;
    }

}
