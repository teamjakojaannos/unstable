package fi.jakojaannos.unstable.components.tasks;

import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.resources.Resources;

public interface Task<EntityState> {

    /**
     * Do any necessary initialization when this task becomes active (as in next in the list).
     */
    void initialize(Entity entity, Resources resources);

    boolean isCompleted(Entity entity, Resources resources);

    void doAction(Entity entity, Resources resources);

    /**
     * If entity has states (such as walking, idling), you can use this to map the task-state to
     * the existing states.
     */
    EntityState getState();

}
