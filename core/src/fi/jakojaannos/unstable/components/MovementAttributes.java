package fi.jakojaannos.unstable.components;

import fi.jakojaannos.unstable.ecs.Component;

public class MovementAttributes implements Component<MovementAttributes> {
    public float force;

    public MovementAttributes(final float force) {
        this.force = force;
    }

    @Override
    public MovementAttributes cloneComponent() {
        return new MovementAttributes(this.force);
    }
}
