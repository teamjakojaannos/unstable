package fi.jakojaannos.unstable.entities;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.*;
import fi.jakojaannos.unstable.components.tasks.Task;
import fi.jakojaannos.unstable.ecs.Entity;

import java.util.List;

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

    public static Entity.Builder create(final Vector2 position, List<Task> tasks) {
        final var taskList = new TaskList(tasks, false);

        return Entity.builder()
                .component(new PhysicsBody(position, 2.0f, 4.0f))
                .component(new MovementInput())
                .component(new MovementAttributes(1.0f))
                .component(new MorkoAi(8.0f, 5.0f, 2.0f, MorkoAi.State.TASK, taskList))
                .component(new Tags.Morko());
    }
}
