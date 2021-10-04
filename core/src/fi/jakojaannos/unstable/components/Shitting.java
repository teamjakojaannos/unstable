package fi.jakojaannos.unstable.components;

import fi.jakojaannos.unstable.ecs.Component;
import fi.jakojaannos.unstable.resources.TimerHandle;

public class Shitting implements Component<Shitting> {
    public TimerHandle transition;

    public Shitting(TimerHandle handle) {
        this.transition = handle;
    }

    @Override
    public Shitting cloneComponent() {
        return new Shitting(this.transition);
    }
}
