package fi.jakojaannos.unstable.entities;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.*;
import fi.jakojaannos.unstable.components.tasks.TaskMove;
import fi.jakojaannos.unstable.components.tasks.TaskWait;
import fi.jakojaannos.unstable.ecs.Entity;

import java.util.List;

public class Nurse {
    private Nurse() {
    }

    public static Entity.Builder create(final Vector2 position) {
        final var x = position.x;
        final var y = position.y;
        final var targetDistance = 1.0f;
        final var tasks = new TaskList<>(List.of(
                new TaskMove<>(new Vector2(x + 5.0f, y), targetDistance, false),
                new TaskWait<>(1.0f, false),
                new TaskMove<>(new Vector2(x - 5.0f, y), targetDistance, false),
                new TaskWait<>(1.5f, false)
        ), true);
        return create2(position).component(tasks);
    }

    public static Entity.Builder create2(final Vector2 position) {
        return Entity.builder()
                     .component(new PhysicsBody(position, 1.0f, 1.3f))
                     .component(new MovementInput())
                     .component(new MovementAttributes(0.75f))
                     .component(new Tags.Nurse());
    }
}
