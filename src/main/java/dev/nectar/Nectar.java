package dev.nectar;

import dev.nectar.core.input.CoreKeybinds;
import dev.nectar.core.input.KeyAction;
import dev.nectar.events.core.KeyEvent;
import dev.nectar.modules.ModuleManager;
import dev.nectar.modules.render.Active;
import dev.nectar.systems.Systems;
import dev.nectar.ui.screens.clickgui.ClickGUI;
import dev.nectar.utils.Utils;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.IEventBus;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.invoke.MethodHandles;

public class Nectar implements ModInitializer {

	public static Nectar INSTANCE;
	public static final String MOD_ID = "nectar";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static MinecraftClient mc;
	public static final IEventBus EVENT_BUS = new EventBus();
	public static final File FOLDER = FabricLoader.getInstance().getGameDir().resolve(MOD_ID).toFile();

	public boolean loaded = false;
	private boolean firstLoad = false;

	@Override
	public void onInitialize() {
		if (INSTANCE == null) {
			INSTANCE = this;
			return;
		}

		LOGGER.info("Initializing Nectar...");
		loaded = false;

		mc = MinecraftClient.getInstance();

		if (!FOLDER.exists()) {
			firstLoad = true;
			FOLDER.getParentFile().mkdirs();
			FOLDER.mkdir();
		}

		EVENT_BUS.registerLambdaFactory("dev.nectar", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));

		Systems.init();

		EVENT_BUS.subscribe(this);

		Systems.load();

		LOGGER.info("Welcome to Nectar!");

		// Save on shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(Systems::save));

		// Enable "Active" if it's the first load ever
		if (firstLoad) ModuleManager.get().get(Active.class).setEnabled(true);

		loaded = true;
	}

	@EventHandler
	private void onKey(KeyEvent event) {
		if (event.action == KeyAction.Press && CoreKeybinds.OPEN_GUI.matchesKey(event.input)) {
			toggleGui();
		}
	}

	private void toggleGui() {
		if (Utils.canCloseGui()) mc.currentScreen.close();
		else if (Utils.canOpenGui()) mc.setScreen(ClickGUI.INSTANCE);
	}

	public static Identifier identifier(String path) {
		return Identifier.of(Nectar.MOD_ID, path);
	}

	public boolean hasLoaded() {
		return loaded;
	}

}