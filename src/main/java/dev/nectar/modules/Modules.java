package dev.nectar.modules;

import dev.nectar.Nectar;
import dev.nectar.core.input.Input;
import dev.nectar.core.input.KeyAction;
import dev.nectar.events.core.KeyEvent;
import dev.nectar.events.game.GameJoinedEvent;
import dev.nectar.events.game.GameLeftEvent;
import dev.nectar.modules.combat.ArmorStatus;
import dev.nectar.modules.combat.AutoLog;
import dev.nectar.modules.misc.Descriptions;
import dev.nectar.modules.movement.Sprint;
import dev.nectar.modules.player.AutoTool;
import dev.nectar.modules.player.FreeCam;
import dev.nectar.modules.player.Zoom;
import dev.nectar.modules.render.*;
import dev.nectar.modules.world.*;
import dev.nectar.modules.util.settings.Setting;
import dev.nectar.systems.System;
import dev.nectar.systems.Systems;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static dev.nectar.Nectar.mc;

public class Modules extends System<Modules> {

    private final List<Module> mods = new ArrayList<>();
    private final List<Module> active = new ArrayList<>();
    private final Map<Class<? extends Module>, Module> modInstances = new Reference2ReferenceOpenHashMap<>();

    public Modules() {
        super("modules");
    }

    public static Modules get() {
        return Systems.get(Modules.class);
    }

    @Override
    public void init() {
        add(new Sprint());
        add(new Coordinates());
        add(new Descriptions());
        add(new RainbowHue());
        add(new Zoom());
        add(new BiomeInfo());
        add(new Clock());
        add(new Active());
        add(new Radar());
        add(new Tracers());
        add(new MobESP());
        add(new StorageESP());
        add(new AutoLog());
        add(new FreeCam());
        add(new Fullbright());
        add(new ItemESP());
        add(new ArmorStatus());
        add(new AutoTool());
    }

    public List<Module> getAll() {
        return mods;
    }

    public  List<Module> getEnabled() {
        List<Module> enabledMods = new ArrayList<>();

        for (Module mod : mods) {
            if (mod.isEnabled()) enabledMods.add(mod);
        }

        return enabledMods;
    }

    public boolean isActive(Class<? extends Module> klass) {
        Module module = get(klass);
        return module != null && module.isEnabled();
    }

    public void disableAll() {
        for (Module mod : mods) {
            mod.setEnabled(false);
        }
    }

    public List<Module> getModsInCategory(Module.Category category) {
        List<Module> categoryMods = new ArrayList<>();

        for (Module mod : mods) {
            if (mod.getCategory() == category) {
                categoryMods.add(mod);
            }
        }

        return categoryMods;
    }

    public List<Module> getActive() {
        return active;
    }

    void addActive(Module module) {
        synchronized (active) {
            if (!active.contains(module)) {
                active.add(module);
            }
        }
    }

    void removeActive(Module module) {
        synchronized (active) {
            active.remove(module);
        }
    }

    @SuppressWarnings("unchecked") // To keep mr.IDE quiet
    @Nullable
    public <T extends Module> T get(Class<T> toFind) {
        return (T) modInstances.get(toFind);
    }

    @Nullable
    public Module get(String name) {
        for (Module module : modInstances.values()) {
            if (module.getName().equalsIgnoreCase(name)) return module;
        }

        return null;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKey(KeyEvent event) {
        if (event.action == KeyAction.Repeat) return;
        onAction(true, event.key(), event.modifiers(), event.action == KeyAction.Press);
    }

    // Init modules

    public void add(Module mod) {
        mods.add(mod);
        modInstances.put(mod.getClass(), mod);
    }

    // Keybind

    private void onAction(boolean isKey, int value, int modifiers, boolean isPress) {
        if (mc.currentScreen != null || Input.isKeyPressed(GLFW.GLFW_KEY_F3)) return;

        for (Module module : getAll()) {
            if (module.keybind.getValue() == value && isPress) {
                module.toggle();
                //module.sendToggledMsg();
            }
        }
    }

    // Subscribing & unsubscribing from the event bus

    @EventHandler
    private void onGameJoined(GameJoinedEvent event) {
        synchronized (active) {
            for (Module module : getAll()) {
                if (module.isEnabled()) {
                    Nectar.EVENT_BUS.subscribe(module);
                    module.onEnable();
                }
            }
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        synchronized (active) {
            for (Module module : getAll()) {
                if (module.isEnabled()) {
                    Nectar.EVENT_BUS.unsubscribe(module);
                    module.onDisable();
                }
            }
        }
    }

    // Saving & loading

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();

        NbtList modulesTag = new NbtList();
        for (Module mod : getAll()) {
            NbtCompound modTag = mod.toTag();
            if (modTag != null) modulesTag.add(modTag);
        }

        tag.put("modules", modulesTag);

        return tag;
    }

    @Override
    public Modules fromTag(NbtCompound tag) {
        disableAll();

        NbtList modulesTag = tag.getListOrEmpty("modules");
        for (NbtElement modTagI : modulesTag) {
            NbtCompound modTag = (NbtCompound) modTagI;
            Module mod = get(modTag.getString("name", ""));
            if (mod != null) mod.fromTag(modTag);
        }

        return this;
    }

    @Override
    public void load(File folder) {
        for (Module mod : mods) {
            for (Setting<?> setting : mod.getSettings()) setting.reset();
        }

        super.load(folder);
    }
}
