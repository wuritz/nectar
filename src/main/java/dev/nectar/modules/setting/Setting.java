package dev.nectar.modules.setting;

import dev.nectar.modules.Module;
import dev.nectar.utils.misc.interfaces.IGetter;
import dev.nectar.utils.misc.interfaces.ISerializable;
import net.minecraft.nbt.NbtCompound;

import java.util.function.Consumer;


@SuppressWarnings("unchecked")
public abstract class Setting<T> implements IGetter<T>, ISerializable<T> {

    protected String name, description;

    protected final T defaultValue;
    protected T value;

    private final Consumer<T> onChange;

    public Module module;

    public Setting(String name, String description, T defaultValue, Consumer<T> onChange) {
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
        this.onChange = onChange;

        resetImpl();
    }

    @Override
    public T get() {
        return value;
    }

    public boolean set(T value) {
        if (!isValueValid(value)) return false;

        this.value = value;
        onChange();

        return true;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    protected abstract boolean isValueValid(T value);

    protected void resetImpl() {
        value = defaultValue;
    }

    public void reset() {
        resetImpl();
        onChange();
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public boolean parse(String str) {
        T newValue = parseImpl(str);

        if (newValue != null) {
            if (isValueValid(newValue)) {
                value = newValue;
                onChange();
            }
        }

        return newValue != null;
    }

    public void onChange() {
        if (onChange != null) onChange.accept(value);
    }

    // Saving & loading

    protected abstract T parseImpl(String str);

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
    public T fromTag(NbtCompound tag) {
        T value = load(tag);
        onChange();

        return (T) this;
    }

    // Builder

    @SuppressWarnings("unchecked")
    public abstract static class SettingBuilder<B, V, S> {
        protected String name = "undefined", description = "";
        protected V defaultValue;
        protected Consumer<V> onChange;

        protected SettingBuilder(V defaultValue) {
            this.defaultValue = defaultValue;
        }

        public B name(String name) {
            this.name = name;
            return (B) this;
        }

        public B description(String description) {
            this.description = description;
            return (B) this;
        }

        public B defaultValue(V defaultValue) {
            this.defaultValue = defaultValue;
            return (B) this;
        }

        public B onChange(Consumer<V> onChange) {
            this.onChange = onChange;
            return (B) this;
        }

        public abstract S build();
    }

}
