package dev.nectar.events.core.input;


import dev.nectar.core.input.KeyAction;
import dev.nectar.events.Cancellable;
import net.minecraft.client.gui.Click;
import net.minecraft.client.input.MouseInput;

public class MouseClickEvent extends Cancellable {
    private static final MouseClickEvent INSTANCE = new MouseClickEvent();

    public Click click;
    public MouseInput input;
    public KeyAction action;

    public static MouseClickEvent get(Click click, KeyAction action) {
        INSTANCE.setCancelled(false);
        INSTANCE.click = click;
        INSTANCE.input = click.buttonInfo();
        INSTANCE.action = action;
        return INSTANCE;
    }

    public int button() {
        return INSTANCE.input.button();
    }
}
