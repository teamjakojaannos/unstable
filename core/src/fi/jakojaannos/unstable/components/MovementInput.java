package fi.jakojaannos.unstable.components;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.ecs.Component;

public class MovementInput implements Component<MovementInput> {
    public Vector2 direction;

    public MovementInput() {
        this(new Vector2(0.0f, 0.0f));
    }

    public MovementInput(Vector2 direction) {
        this.direction = direction;
    }

    @Override
    public MovementInput cloneComponent() {
        return new MovementInput(this.direction.cpy());
    }
}
