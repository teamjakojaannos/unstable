package fi.jakojaannos.unstable.components;

import fi.jakojaannos.unstable.ecs.Component;

public class MovementAttributes implements Component<MovementAttributes> {
    public float moveSpeed;

    public MovementAttributes(final float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    @Override
    public MovementAttributes cloneComponent() {
        return new MovementAttributes(this.moveSpeed);
    }
}
