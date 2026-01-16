package dev.nectar.modules.world;

import dev.nectar.events.core.render.Render2DEvent;
import dev.nectar.modules.Module;
import dev.nectar.ui.UIUtils;
import dev.nectar.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.apache.commons.lang3.StringUtils;

import static dev.nectar.Nectar.mc;

public class BiomeInfo extends Module {
    public BiomeInfo() {
        super("BiomeInfo", "Displays current biome.", Category.WORLD);
    }

    public static Text getBiomeName() {
        if (Utils.isToggleable()) {
            BlockPos pos = mc.getCameraEntity().getBlockPos();
            RegistryKey<Biome> key = mc.world.getBiome(pos).getKey().get();

            return Text.literal(snakeCaseToEnglish(key.getValue().getPath()));
        } else {
            return Text.literal("N/A");
        }
    }

    public static Text getBiomeKey() {
        if (Utils.isToggleable()) {
            BlockPos pos = mc.getCameraEntity().getBlockPos();
            RegistryKey<Biome> key = mc.world.getBiome(pos).getKey().get();

            return Text.literal(key.getValue().toString());
        } else {
            return Text.literal("N/A");
        }
    }

    private static String snakeCaseToEnglish(String biomePath) {
        String[] words = biomePath.split("_");
        StringBuilder formatted = new StringBuilder();

        for (String word : words) {
            formatted.append(StringUtils.capitalize(word)).append(" ");
        }

        return formatted.toString().trim();
    }


    @EventHandler
    public void onRender(Render2DEvent event) {
        event.drawContext.fill((mc.getWindow().getScaledWidth() / 2) - (mc.textRenderer.getWidth(BiomeInfo.getBiomeName()) / 2) - UIUtils.margin, 0, (mc.getWindow().getScaledWidth() / 2) - (mc.textRenderer.getWidth(BiomeInfo.getBiomeName()) / 2) + (mc.textRenderer.getWidth(BiomeInfo.getBiomeName())) + UIUtils.margin, mc.textRenderer.fontHeight + UIUtils.margin, UIUtils.BACKGROUND_BASE.getRGB());
        event.drawContext.fill((mc.getWindow().getScaledWidth() / 2) - (mc.textRenderer.getWidth(BiomeInfo.getBiomeKey()) / 2) - UIUtils.margin, mc.textRenderer.fontHeight + UIUtils.margin, (mc.getWindow().getScaledWidth() / 2) - (mc.textRenderer.getWidth(BiomeInfo.getBiomeKey()) / 2) + (mc.textRenderer.getWidth(BiomeInfo.getBiomeKey())) + UIUtils.margin, 2 * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.BACKGROUND_BASE.getRGB());

        event.drawContext.fill((mc.getWindow().getScaledWidth() / 2) - (mc.textRenderer.getWidth(BiomeInfo.getBiomeName()) / 2) - UIUtils.margin, 0,  ((mc.getWindow().getScaledWidth() / 2) - (mc.textRenderer.getWidth(BiomeInfo.getBiomeName()) / 2) - UIUtils.margin) + UIUtils.margin / 4, (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.getSelectedPrimaryColor().getRGB());
        event.drawContext.fill((mc.getWindow().getScaledWidth() / 2) - (mc.textRenderer.getWidth(BiomeInfo.getBiomeKey()) / 2) - UIUtils.margin, mc.textRenderer.fontHeight + UIUtils.margin, ((mc.getWindow().getScaledWidth() / 2) - (mc.textRenderer.getWidth(BiomeInfo.getBiomeKey()) / 2) - UIUtils.margin) + UIUtils.margin / 4, 2 * (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.getSelectedPrimaryColor().getRGB());

        event.drawContext.drawTextWithShadow(mc.textRenderer, BiomeInfo.getBiomeName(), (mc.getWindow().getScaledWidth() / 2) - (mc.textRenderer.getWidth(BiomeInfo.getBiomeName()) / 2), (UIUtils.margin / 2), UIUtils.getSelectedPrimaryColor().getRGB());
        event.drawContext.drawTextWithShadow(mc.textRenderer, BiomeInfo.getBiomeKey(), (mc.getWindow().getScaledWidth() / 2) - (mc.textRenderer.getWidth(BiomeInfo.getBiomeKey()) / 2),(UIUtils.margin / 2) + (mc.textRenderer.fontHeight + UIUtils.margin), UIUtils.LIGHT.darker().getRGB());
    }
}
