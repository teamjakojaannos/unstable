package fi.jakojaannos.unstable.systems;

import fi.jakojaannos.unstable.components.AttackTargetComponent;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Attacks;
import fi.jakojaannos.unstable.resources.Resources;

public class AttackTargetCollectorSystem implements EcsSystem<AttackTargetCollectorSystem.Input> {
    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        resources.attacks.availableTargets.clear();

        input.entities().forEach(entity ->
                resources.attacks.availableTargets.add(
                        new Attacks.AttackTarget(entity.entity(), entity.body.getPosition(), entity.target)
                ));
    }

    public record Input(
            Entity entity,
            PhysicsBody body,
            AttackTargetComponent target
    ) {
    }
}
