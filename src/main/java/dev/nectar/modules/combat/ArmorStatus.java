package dev.nectar.modules.combat;

import dev.nectar.events.core.render.Render2DEvent;
import dev.nectar.modules.Module;
import dev.nectar.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static dev.nectar.Nectar.mc;

public class ArmorStatus extends Module {

    public ArmorStatus() {
        super("ArmorStatus", "Renders your armor durability on the HUD", Category.COMBAT);
    }

    @EventHandler
    public void onRender(Render2DEvent event) {
        if (!Utils.isToggleable()) return;
        if (mc.player.isSpectator()) return;

        int width = event.drawContext.getScaledWindowWidth();
        int height = event.drawContext.getScaledWindowHeight();

        ItemStack[] armor = new ItemStack[]{getItem(EquipmentSlot.FEET), getItem(EquipmentSlot.LEGS), getItem(EquipmentSlot.CHEST), getItem(EquipmentSlot.HEAD)};

        List<ItemStack> itemsToRender = new ArrayList<>();
        for (ItemStack stack : armor) {
            if (!stack.isEmpty()) {
                itemsToRender.add(stack);
            }
        }

        int x = (width / 2) + 101;
        int y = height - 15;

        for (ItemStack stack : itemsToRender) {
            event.drawContext.drawItem(stack, x, y);
            event.drawContext.drawStackOverlay(mc.textRenderer, stack, x, y);

            y -= 20;
        }
    }

    private static ItemStack getItem(EquipmentSlot slot) {
        ItemStack stack = mc.player.getEquippedStack(slot);
        return stack;
    }
}
