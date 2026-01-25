package dev.nectar.modules.render;

import dev.nectar.core.Color;
import dev.nectar.core.renderer.ShapeMode;
import dev.nectar.events.core.render.Render3DEvent;
import dev.nectar.modules.Module;
import dev.nectar.modules.setting.Setting;
import dev.nectar.modules.setting.settings.DoubleSetting;
import dev.nectar.modules.setting.settings.EnumSetting;
import dev.nectar.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

import static dev.nectar.Nectar.mc;

public class MobESP extends Module {

    private final Setting<Double> range = new DoubleSetting.Builder()
            .name("Range").description("")
            .min(10d).max(125d)
            .defaultValue(32d)
            .build();

    private final Setting<Mode> type = new EnumSetting.Builder<Mode>()
            .name("Target").description("")
            .defaultValue(Mode.Hostile)
            .build();

    public MobESP() {
        super("MobESP", "Highlights mobs with bounding boxes.", Category.RENDER);

        addSettings(range, type);
    }

    @EventHandler
    public void onRender3D(Render3DEvent event) {
        if (!Utils.isToggleable()) return;

        for (Entity entity : mc.world.getEntities()) {
            if (entity == mc.player) continue;
            if (!(entity instanceof LivingEntity)) continue;

            switch (type.get()) {
                case Hostile:
                    if (!(entity instanceof HostileEntity)) return;
                    break;
                case Passive:
                    if (!(entity instanceof PassiveEntity)) return;
                    break;
                case Player:
                    if (!(entity instanceof PlayerEntity)) return;
                    break;
            }

            double dist = entity.getEntityPos().distanceTo(mc.player.getEntityPos());
            if (dist > range.get()) continue;

            Box box = entity.getBoundingBox();
            event.renderer.box(box, Color.BLUE.a(10), Color.WHITE, ShapeMode.Both, 0);
        }
    }

    private enum Mode {
        Hostile, Passive, Player, All
    }
}