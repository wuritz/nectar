package hu.bluestoplight.modules;

import hu.bluestoplight.SednaClient;
import hu.bluestoplight.core.input.Input;
import hu.bluestoplight.core.input.KeyAction;
import hu.bluestoplight.events.core.KeyEvent;
import hu.bluestoplight.events.game.GameJoinedEvent;
import hu.bluestoplight.events.game.GameLeftEvent;
import hu.bluestoplight.modules.combat.ArmorStatus;
import hu.bluestoplight.modules.combat.AutoLog;
import hu.bluestoplight.modules.misc.Descriptions;
import hu.bluestoplight.modules.movement.Sprint;
import hu.bluestoplight.modules.player.AutoTool;
import hu.bluestoplight.modules.player.FreeCam;
import hu.bluestoplight.modules.player.Zoom;
import hu.bluestoplight.modules.render.*;
import hu.bluestoplight.modules.util.settings.Setting;
import hu.bluestoplight.modules.world.*;
import hu.bluestoplight.systems.System;
import hu.bluestoplight.systems.Systems;
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

import static hu.bluestoplight.SednaClient.mc;

public class ModManager extends System<ModManager> {

    private final List<Mod> mods = new ArrayList<>();
    private final Map<Class<? extends Mod>, Mod> modInstances = new Reference2ReferenceOpenHashMap<>();

    public ModManager() {
        super("modules");
    }

    public static ModManager get() {
        return Systems.get(ModManager.class);
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
        add(new SoundFilters());
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

    public List<Mod> getMods() {
        return mods;
    }

    public  List<Mod> getEnabled() {
        List<Mod> enabledMods = new ArrayList<>();

        for (Mod mod : mods) {
            if (mod.isEnabled()) enabledMods.add(mod);
        }

        return enabledMods;
    }

    public void disableAll() {
        for (Mod mod : mods) {
            mod.setEnabled(false);
        }
    }

    public List<Mod> getModsInCategory(Mod.Category category) {
        List<Mod> categoryMods = new ArrayList<>();

        for (Mod mod : mods) {
            if (mod.getCategory() == category) {
                categoryMods.add(mod);
            }
        }

        return categoryMods;
    }

    @SuppressWarnings("unchecked") // To keep mr.IDE quiet
    @Nullable
    public <T extends Mod> T get(Class<T> toFind) {
        return (T) modInstances.get(toFind);
    }

    @Nullable
    public Mod get(String name) {
        for (Mod module : modInstances.values()) {
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

    public void add(Mod mod) {
        mods.add(mod);
        modInstances.put(mod.getClass(), mod);
    }

    // Keybind

    private void onAction(boolean isKey, int value, int modifiers, boolean isPress) {
        if (mc.currentScreen != null || Input.isKeyPressed(GLFW.GLFW_KEY_F3)) return;

        for (Mod module : getMods()) {
            if (module.keybind.getValue() == value && isPress) {
                module.toggle();
                //module.sendToggledMsg();
            }
        }
    }

    // Subscribing & unsubscribing from the event bus

    @EventHandler
    private void onGameJoined(GameJoinedEvent event) {
        for (Mod module : getMods()) {
            if (module.isEnabled()) {
                SednaClient.EVENT_BUS.subscribe(module);
                module.onEnable();
            }
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        for (Mod module : getMods()) {
            if (module.isEnabled()) {
                if (!Objects.equals(module.getName(), "DiscordRPC")) {
                    SednaClient.EVENT_BUS.unsubscribe(module);
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
        for (Mod mod : getMods()) {
            NbtCompound modTag = mod.toTag();
            if (modTag != null) modulesTag.add(modTag);
        }

        tag.put("modules", modulesTag);

        return tag;
    }

    @Override
    public ModManager fromTag(NbtCompound tag) {
        disableAll();

        NbtList modulesTag = tag.getListOrEmpty("modules");
        for (NbtElement modTagI : modulesTag) {
            NbtCompound modTag = (NbtCompound) modTagI;
            Mod mod = get(modTag.getString("name", ""));
            if (mod != null) mod.fromTag(modTag);
        }

        return this;
    }

    @Override
    public void load(File folder) {
        for (Mod mod : mods) {
            for (Setting<?> setting : mod.getSettings()) setting.reset();
        }

        super.load(folder);
    }
}
