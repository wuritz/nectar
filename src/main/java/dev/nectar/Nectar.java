package dev.nectar;

import dev.nectar.core.init.PostInit;
import dev.nectar.core.init.PreInit;
import dev.nectar.core.init.ReflectInit;
import dev.nectar.core.input.CoreKeybinds;
import dev.nectar.core.input.KeyAction;
import dev.nectar.events.core.input.KeyEvent;
import dev.nectar.modules.Modules;
import dev.nectar.modules.render.Active;
import dev.nectar.systems.Systems;
import dev.nectar.ui.screens.clickgui.ClickGUI;
import dev.nectar.utils.Utils;
import dev.nectar.utils.misc.Version;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.IEventBus;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.invoke.MethodHandles;

public class Nectar implements ModInitializer {

	public static Nectar INSTANCE;
	public static final String MOD_ID = "nectar";
	public static final ModMetadata MOD_META;
	public static final String NAME;
	public static final Version VERSION;

	public static MinecraftClient mc;
	public static final IEventBus EVENT_BUS = new EventBus();

	public static final File FOLDER = FabricLoader.getInstance().getGameDir().resolve(MOD_ID).toFile();
	public static final Logger LOG;

	private boolean firstLoad = false;

	static {
		MOD_META = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata();

		NAME = MOD_META.getName();
		LOG = LoggerFactory.getLogger(NAME);

		String verString = MOD_META.getVersion().getFriendlyString();

		VERSION = new Version(verString);
	}

	@Override
	public void onInitialize() {
		if (INSTANCE == null) {
			INSTANCE = this;
			return;
		}

		LOG.info("Initializing Nectar...");

		mc = MinecraftClient.getInstance();

		if (!FOLDER.exists()) {
			firstLoad = true;
			FOLDER.getParentFile().mkdirs();
			FOLDER.mkdir();
		}

		// Register init classes, then launch PreInit
		ReflectInit.register();
		ReflectInit.init(PreInit.class);

		EVENT_BUS.registerLambdaFactory("dev.nectar", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));

		Systems.init();

		EVENT_BUS.subscribe(this);

		Systems.load();

		// Launch PostInit
		ReflectInit.init(PostInit.class);

		LOG.info("Welcome to Nectar!");

		// Save on shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(Systems::save));

		// Enable "Active" if it's the first load ever
		if (firstLoad && !Modules.get().isActive(Active.class)) Modules.get().get(Active.class).toggle();
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

}