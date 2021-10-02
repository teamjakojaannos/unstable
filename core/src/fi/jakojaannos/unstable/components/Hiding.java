package fi.jakojaannos.unstable.components;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.ecs.Component;

public class Hiding implements Component<Hiding> {
    public final Vector2 previousPosition;

    public Hiding(Vector2 previousPosition) {
        this.previousPosition = previousPosition;
    }

    @Override
    public Hiding cloneComponent() {
        return new Hiding(this.previousPosition);
    }
}
