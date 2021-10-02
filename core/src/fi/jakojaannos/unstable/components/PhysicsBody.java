package fi.jakojaannos.unstable.components;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.ecs.Component;

public class PhysicsBody implements Component<PhysicsBody> {
    public final Vector2 position;

    public PhysicsBody(Vector2 position) {
        this.position = position;
    }

    @Override
    public PhysicsBody cloneComponent() {
        return new PhysicsBody(new Vector2(this.position));
    }
}
