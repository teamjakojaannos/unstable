package fi.jakojaannos.unstable.entities;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.Mirror;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.Entity;

public class MirrorProp {
    public static Entity.Builder create(Vector2 position) {
        return Entity.builder()
                     .component(new PhysicsBody(position, 2.0f, 16.0f))
                     .component(new Mirror());
    }
}
