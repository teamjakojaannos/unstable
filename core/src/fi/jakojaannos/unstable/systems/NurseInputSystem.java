package fi.jakojaannos.unstable.systems;

import fi.jakojaannos.unstable.components.MovementInput;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.Tags;
import fi.jakojaannos.unstable.components.TaskList;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class NurseInputSystem implements EcsSystem<NurseInputSystem.Input> {

    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        input.entities().forEach(entity -> {
            entity.input.direction.setZero();
            entity.tasks.update(entity.entity, resources);
        });
    }

    public record Input(
            Entity entity,
            TaskList tasks,
            MovementInput input,
            PhysicsBody body,
            Tags.Nurse nurseTag
    ) {
    }
}
