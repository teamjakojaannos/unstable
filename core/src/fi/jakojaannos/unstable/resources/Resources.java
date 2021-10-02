package fi.jakojaannos.unstable.resources;

import fi.jakojaannos.unstable.InputState;
import fi.jakojaannos.unstable.ecs.EcsWorld;

public class Resources {
    public final InputState playerInput = new InputState();
    public final Entities entities;

    public Resources(final EcsWorld world) {
        entities = new Entities(world);
    }
}
