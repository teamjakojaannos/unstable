package fi.jakojaannos.unstable.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import fi.jakojaannos.unstable.components.MovementAttributes;
import fi.jakojaannos.unstable.components.MovementInput;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.Entity;

public class Player {
    private Player() {
    }

    public static Entity.Builder create(final World physicsWorld, final Vector2 position) {
        final var force = 2.0f;
        return Entity.builder()
                .component(new PhysicsBody(position))
                .component(new MovementAttributes(force))
                .component(new MovementInput());
    }
}
