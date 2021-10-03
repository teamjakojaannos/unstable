package fi.jakojaannos.unstable.components.tasks;

import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.resources.Resources;

public interface Task {

    boolean isCompleted(Entity entity, Resources resources);

    void doAction(Entity entity, Resources resources);

}
