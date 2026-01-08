package hu.bluestoplight;

import hu.bluestoplight.core.input.CoreKeybinds;
import hu.bluestoplight.core.input.KeyAction;
import hu.bluestoplight.events.core.KeyEvent;
import hu.bluestoplight.events.game.GameJoinedEvent;
import hu.bluestoplight.events.game.GameLeftEvent;
import hu.bluestoplight.misc.References;
import hu.bluestoplight.modules.ModManager;
import hu.bluestoplight.modules.render.*;
import hu.bluestoplight.systems.Systems;
import hu.bluestoplight.ui.screens.clickgui.ClickGUI;
import hu.bluestoplight.ui.waypoints.WaypointManager;
import hu.bluestoplight.ui.waypoints.WaypointRenderer;
import hu.bluestoplight.utils.Utils;
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

public class SednaClient implements ModInitializer {

	public static WaypointManager waypointManager;
	public static SednaClient INSTANCE;
	public static final String MOD_ID = "sedna";
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

		LOGGER.info("[" + References.CLIENT_NAME + "]: Initializing...");
		loaded = false;

		mc = MinecraftClient.getInstance();

		if (!FOLDER.exists()) {
			firstLoad = true;
			FOLDER.getParentFile().mkdirs();
			FOLDER.mkdir();
		}

		waypointManager = new WaypointManager();

		EVENT_BUS.registerLambdaFactory("hu.bluestoplight", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));

		WaypointRenderer.register(waypointManager);

		Systems.init();

		EVENT_BUS.subscribe(this);

		Systems.load();

		LOGGER.info("[" + References.CLIENT_NAME + "]: Welcome to Sedna!");

		// Save on shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(Systems::save));

		// Enable "Active" if it's the first load ever
		if (firstLoad) ModManager.get().get(Active.class).setEnabled(true);

		loaded = true;
	}

	// TODO: AutoWalk, AntiAFK [v.1.6]
	// TODO: Trajectories, SafeWalk [v.1.6.1]
	// TODO: AutoEat, FancyChat, PortalESP [v.1.7]
	// TODO: Tidal API support [v.1.8]
	// TODO: Ambient Sounds [v.1.9]

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
		return Identifier.of(SednaClient.MOD_ID, path);
	}

	public boolean hasLoaded() {
		return loaded;
	}

}