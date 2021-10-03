package fi.jakojaannos.unstable.entities;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.*;
import fi.jakojaannos.unstable.ecs.Entity;

public class Morko {
    private Morko() {
    }

    public static Entity.Builder create(final Vector2 position) {
        return Entity.builder()
                .component(new PhysicsBody(position, 2.0f, 4.0f))
                .component(new MovementInput())
                .component(new MovementAttributes(1.0f))
                .component(new MorkoAi(8.0f, 5.0f, 2.0f, MorkoAi.State.IDLING))
                .component(new Tags.Morko());
    }
}
