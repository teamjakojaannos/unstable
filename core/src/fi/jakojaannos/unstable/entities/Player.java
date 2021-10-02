package fi.jakojaannos.unstable.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import fi.jakojaannos.unstable.components.*;
import fi.jakojaannos.unstable.ecs.Entity;

public class Player {
    private Player() {
    }

    public static Entity.Builder create(final Vector2 position) {
        final var min = new Vector3(-0.5f, 0.0f, 0.0f);
        final var max = new Vector3(0.5f, 1.5f, 0.0f);

        final var bounds = new BoundingBox(min, max);
        final var force = 2.0f;
        return Entity.builder()
                     .component(new PhysicsBody(position, bounds))
                     .component(new MovementAttributes(force))
                     .component(new MovementInput())
                     .component(new PlayerInput())
                     .component(new Tags.Player());
    }
}
