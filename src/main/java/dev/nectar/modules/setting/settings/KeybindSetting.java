package dev.nectar.modules.setting.settings;

import dev.nectar.Nectar;
import dev.nectar.core.input.KeyAction;
import dev.nectar.core.input.Keybind;
import dev.nectar.events.core.input.KeyEvent;
import dev.nectar.events.core.input.MouseClickEvent;
import dev.nectar.modules.setting.Setting;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.nbt.NbtCompound;

import java.util.function.Consumer;

public class KeybindSetting extends Setting<Keybind> {

    private final Runnable action;

    public KeybindSetting(String name, String description, Keybind defaultValue, Consumer<Keybind> onChange, Runnable action) {
        super(name, description, defaultValue, onChange);

        this.action = action;
        Nectar.EVENT_BUS.subscribe(this);
    }

    // Key handling

    @EventHandler(priority = EventPriority.HIGH)
    private void onKey(KeyEvent event) {
        if (event.action == KeyAction.Release && get().matches(event.input) && (module == null || module.isEnabled()) && action != null) {
            action.run();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onMouseClick(MouseClickEvent event) {
        if (event.action == KeyAction.Release && get().matches(event.input) && (module == null || module.isEnabled()) && action != null) {
            action.run();
        }
    }

    // Saving & loading

    @Override
    protected boolean isValueValid(Keybind value) {
        return true;
    }

    @Override
    public void resetImpl() {
        if (value == null) value = defaultValue.copy();
        else value.set(defaultValue);
    }

    @Override
    protected Keybind parseImpl(String str) {
        try {
            return Keybind.fromKey(Integer.parseInt(str.trim()));
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    @Override
    public NbtCompound save(NbtCompound tag) {
        tag.put("value", get().toTag());

        return tag;
    }

    @Override
    public Keybind load(NbtCompound tag) {
        get().fromTag(tag.getCompoundOrEmpty("value"));

        return get();
    }


    public static class Builder extends SettingBuilder<Builder, Keybind, KeybindSetting> {
        private Runnable action;

        public Builder() {
            super(Keybind.none());
        }

        public Builder action(Runnable action) {
            this.action = action;
            return this;
        }

        @Override
        public KeybindSetting build() {
            return new KeybindSetting(name, description, defaultValue, onChange, action);
        }
    }

}
