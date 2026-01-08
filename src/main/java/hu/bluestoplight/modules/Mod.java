package hu.bluestoplight.modules;

import hu.bluestoplight.SednaClient;
import hu.bluestoplight.core.input.Keybind;
import hu.bluestoplight.modules.util.settings.KeybindSetting;
import hu.bluestoplight.modules.util.settings.Setting;
import hu.bluestoplight.modules.util.settings.Settings;
import hu.bluestoplight.utils.misc.interfaces.ISerializable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.List;

public class Mod implements ISerializable<Mod> {
    private Category category;
    private String name, displayName, description;
    private int key;
    private boolean enabled;

    public final Settings settings = new Settings();

    private final KeybindSetting keybindSetting = new KeybindSetting("Keybind", Keybind.none());
    public final Keybind keybind = Keybind.none();

    public enum Category {
        COMBAT("Combat"), PLAYER("Player"), MOVEMENT("Movement"), RENDER("Render"), MISC("Misc"), WORLD("World");

        public String name;
        Category(String name) {
            this.name = name;
        }
    }

    public Mod(String name, String description, Category category) {
        this.name = name;
        this.displayName = name;
        this.description = description;
        this.category = category;

        addSetting(keybindSetting);
    }

    public void toggle() {
        this.enabled = !this.enabled;

        if (enabled) {
            SednaClient.EVENT_BUS.subscribe(this);
            onEnable();
        } else {
            SednaClient.EVENT_BUS.unsubscribe(this);
            onDisable();
        }
    }

    public void onEnable() {}
    public void onDisable() {}

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<Setting<?>> getSettings() {
        return settings.getSettings();
    }

    public void addSetting(Setting setting) {
        settings.add(setting);
    }

    public void addSettings(Setting... settings) {
        for (Setting setting : settings) {
            addSetting(setting);
        }
    }

    public Keybind getKeybind() {
        return keybindSetting.getKeybind();
    }

    @Override
    public NbtCompound toTag() {
        // if (!serialize) return null; // if there would be a module that doesn't need to be saved
        NbtCompound tag = new NbtCompound();

        tag.putString("name", name);
        tag.put("keybind", keybind.toTag());
        tag.put("settings", settings.toTag());
        tag.putBoolean("enabled", isEnabled());

        return tag;
    }

    @Override
    public Mod fromTag(NbtCompound tag) {
        // Keybind
        keybind.fromTag(tag.getCompoundOrEmpty("keybind"));

        // Settings
        NbtElement settingsTag = tag.get("settings");
        if (settingsTag instanceof NbtCompound) settings.fromTag((NbtCompound) settingsTag);

        // Enabled
        boolean enabled = tag.getBoolean("enabled", false);
        if (enabled != isEnabled()) toggle();

        return this;
    }

}