package dev.nectar.modules.setting;

import dev.nectar.utils.misc.interfaces.ISerializable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.List;

public class Settings implements ISerializable<Settings> {

    final List<Setting<?>> settings = new ArrayList<>();

    public <T extends Setting<?>> T add(T setting) {
        settings.add(setting);

        return setting;
    }

    public Setting<?> get(String name) {
        for (Setting<?> setting : settings) {
            if (name.equalsIgnoreCase(setting.name)) return setting;
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> Setting<T> get(String name, Class<T> tClass) {
        for (Setting<?> setting : settings) {
            Class<?> sClass = setting.getDefaultValue().getClass();
            if (name.equalsIgnoreCase(setting.name) && tClass.equals(sClass))
                return (Setting<T>) setting;
        }

        return null;
    }

    public void reset() {
        for (Setting<?> setting : settings) {
            setting.reset();
        }
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();
        NbtList settingsTag = new NbtList();

        for (Setting<?> setting : settings) {
            settingsTag.add(setting.toTag());
        }

        if (!settingsTag.isEmpty()) tag.put("settings", settingsTag);

        return tag;
    }

    @Override
    public Settings fromTag(NbtCompound tag) {
        reset();

        NbtList settingsTag = tag.getListOrEmpty("settings");

        for (NbtElement t : settingsTag) {
            NbtCompound settingTag = (NbtCompound) t;

            Setting<?> setting = get(settingTag.getString("name", ""));
            if (setting != null) setting.fromTag(settingTag);
        }

        return this;
    }

}
