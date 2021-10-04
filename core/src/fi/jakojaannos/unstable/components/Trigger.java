package fi.jakojaannos.unstable.components;

import fi.jakojaannos.unstable.ecs.Component;
import fi.jakojaannos.unstable.resources.Resources;
import fi.jakojaannos.unstable.resources.TimerHandle;

public class Trigger implements Component<Trigger> {
    public final float triggerCooldown;
    public final boolean canRepeat;
    public final TriggerAction action;

    public TimerHandle cooldown;
    public boolean triggered;

    public Trigger(float triggerCooldown, boolean canRepeat, TriggerAction action) {
        this.triggerCooldown = triggerCooldown;
        this.canRepeat = canRepeat;
        this.action = action;

        this.triggered = false;
    }

    @Override
    public Trigger cloneComponent() {
        return new Trigger(this.triggerCooldown, this.canRepeat, this.action);
    }

    public interface TriggerAction {
        void execute(Resources resources);
    }
}
