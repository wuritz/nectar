package hu.bluestoplight.ui.screens;

import hu.bluestoplight.misc.References;
import hu.bluestoplight.ui.MenuButton;
import hu.bluestoplight.ui.UIUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.text.Text;

import java.lang.reflect.Method;

public class SednaMainMenuScreen extends Screen {
    public SednaMainMenuScreen() {
        super(Text.literal("Sedna"));
    }

    @Override
    protected void init() {
        int startX = 20;
        int startY = (this.height / 4) + 20;
        int spacing = 24;

        this.addDrawableChild(new MenuButton(startX, startY, 150, 20, "Singleplayer", btn -> {
                this.client.setScreen(new SelectWorldScreen(this));
            }
        ));

        this.addDrawableChild(new MenuButton(startX, startY + spacing, 150, 20, "Multiplayer", btn -> {
                this.client.setScreen(new MultiplayerScreen(this));
            }
        ));

        this.addDrawableChild(new MenuButton(startX, startY + (2 * spacing), 150, 20, "Realms", btn -> {
                this.client.setScreen(new RealmsMainScreen(this));
            }
        ));

        this.addDrawableChild(new MenuButton(startX, startY + (3 * spacing), 150, 20, "Settings", btn -> {
                this.client.setScreen(new OptionsScreen(this, this.client.options));
            }
        ));

        boolean isIntegrated = integrateModMenu(startX, startY + (4 * spacing), 150, 20);

        this.addDrawableChild(new MenuButton(startX, startY + ((isIntegrated ? 5 : 4) * spacing), 150, 20, "Quit", btn -> {
                this.client.scheduleStop();
            }
        ));
    }

    private boolean integrateModMenu(int x, int y, int w, int h) {
        if (!FabricLoader.getInstance().isModLoaded("modmenu")) return false;

        try {
            Class<?> apiClass = Class.forName("com.terraformersmc.modmenu.api.ModMenuApi");
            Method textMethod = apiClass.getMethod("createModsButtonText");
            Text buttonText = (Text) textMethod.invoke(null);

            Method screenMethod = apiClass.getMethod("createModsScreen", Screen.class);

            this.addDrawableChild(new MenuButton(x, y, w, h, buttonText.getString(), btn -> {
                try {
                    Screen modsScreen = (Screen) screenMethod.invoke(null, this);
                    this.client.setScreen(modsScreen);
                } catch (Exception ignored) { }
            }));
        } catch (Exception ignored) { }

        return true;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderPanoramaBackground(context, delta);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);

        int winW = this.client.getWindow().getScaledWidth();
        int winH = this.client.getWindow().getScaledHeight();

        context.drawTextWithShadow(this.textRenderer, "(c) Mojang AB",
                20, winH - (2 * textRenderer.fontHeight) - 8,
                UIUtils.LIGHT.darker().getRGB());

        context.drawTextWithShadow(this.textRenderer,
                References.CLIENT_NAME + " " + References.CLIENT_VERSION + " (c) " + References.CLIENT_AUTHOR,
                20, winH - (textRenderer.fontHeight) - 8,
                UIUtils.LIGHT.darker().getRGB());

        String contrib = "Additional Contribution by " + References.CLIENT_CONTRIB;
        context.drawTextWithShadow(this.textRenderer, contrib,
                winW - 20 - textRenderer.getWidth(contrib),
                winH - (textRenderer.fontHeight) - 8,
                UIUtils.LIGHT.darker().darker().getRGB());

        int logoX = 20;
        int logoY = 20;
        float scale = 5.0f;

        context.getMatrices().pushMatrix();
        context.getMatrices().scale(scale, scale, context.getMatrices());
        context.drawTextWithShadow(this.textRenderer, "Sedna",
                (int) (logoX / scale),
                (int) (logoY / scale),
                UIUtils.PRIMARY.getRGB());
        context.getMatrices().popMatrix();
    }
}