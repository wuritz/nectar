package dev.nectar.modules.player;

import dev.nectar.events.world.TickEvent;
import dev.nectar.modules.Module;
import dev.nectar.modules.util.settings.BooleanSetting;
import dev.nectar.modules.util.settings.NumberSetting;
import dev.nectar.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

import static dev.nectar.Nectar.mc;

public class AutoTool extends Module {
    private static BooleanSetting antiBreak = new BooleanSetting("AntiBreak", true);
    private static NumberSetting breakDurability = new NumberSetting("Durability", 1, 100, 10, 1);

    public AutoTool() {
        super("AutoTool", "Automatically switches to the most effective tool when performing an action.", Category.PLAYER);
        addSettings(antiBreak, breakDurability);
    }

    @EventHandler
    public void onTick(TickEvent.Post event) {
        if (Utils.isToggleable()) {
            if (mc.player.isCreative()) return;
            if (!mc.options.attackKey.isPressed()) return;

            if (mc.crosshairTarget == null || mc.crosshairTarget.getType() != HitResult.Type.BLOCK) return;

            BlockHitResult hitResult = (BlockHitResult) mc.crosshairTarget;
            BlockState target = mc.world.getBlockState(hitResult.getBlockPos());

            if (target.getHardness(mc.world, hitResult.getBlockPos()) < 0) return;
            PlayerInventory inventory = mc.player.getInventory();

            int bestSlot = -1;
            float bestSpeed = 1.0f;

            for (int i = 0; i < 9; i++) {
                ItemStack stack = inventory.getStack(i);

                float speed = stack.getMiningSpeedMultiplier(target);

                if ((speed > bestSpeed) && !shouldSkip(i, inventory)) {
                    bestSpeed = speed;
                    bestSlot = i;
                }
            }

            if (bestSlot != -1 && inventory.getSelectedSlot() != bestSlot) {
                inventory.setSelectedSlot(bestSlot);
            }
        }
    }

    private static boolean shouldSkip(int bestSlot, PlayerInventory inventory) {
        if (antiBreak.isEnabled() && inventory.getStack(bestSlot).isDamageable() && (inventory.getStack(bestSlot).getMaxDamage() - inventory.getStack(bestSlot).getDamage()) < breakDurability.getIntValue()) {
            return true;
        } else {
            return false;
        }
    }
}
