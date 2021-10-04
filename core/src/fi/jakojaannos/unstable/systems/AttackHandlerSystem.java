package fi.jakojaannos.unstable.systems;

import fi.jakojaannos.unstable.components.AttackTargetComponent;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class AttackHandlerSystem implements EcsSystem<AttackHandlerSystem.Input> {

    @Override
    public void tick(SystemInput<Input> input, Resources resources) {

        resources.attacks
                .attackedTargets
                .forEach(target -> target.target().action.takeHit(target.self()));

        resources.attacks.attackedTargets.clear();
    }

    public record Input(
            Entity entity,
            PhysicsBody body,
            AttackTargetComponent target
    ) {
    }
}
