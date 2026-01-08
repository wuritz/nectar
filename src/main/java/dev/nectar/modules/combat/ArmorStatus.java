/*
 * Copyright (c) 2026. TheBlueStopLight
 * All rights reserved.
 */

package dev.nectar.modules.combat;

import dev.nectar.modules.Module;
import dev.nectar.utils.Utils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static dev.nectar.Nectar.mc;

public class ArmorStatus extends Module {

    public ArmorStatus() {
        super("ArmorStatus", "Renders your armor durability on the HUD", Category.COMBAT);
    }

    public static void render(DrawContext context) {
        if (!Utils.isToggleable()) return;
        if (mc.player.isSpectator()) return;

        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();

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
            context.drawItem(stack, x, y);
            context.drawStackOverlay(mc.textRenderer, stack, x, y);

            y -= 20;
        }
    }

    private static ItemStack getItem(EquipmentSlot slot) {
        ItemStack stack = mc.player.getEquippedStack(slot);
        return stack;
    }
}
