package fi.jakojaannos.unstable.systems;

import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.entities.BreakableBlocker;
import fi.jakojaannos.unstable.resources.Resources;

public class CollectBlockersSystem implements EcsSystem<CollectBlockersSystem.Input> {
    @Override
    public void tick(
            SystemInput<Input> input,
            Resources resources
    ) {
        resources.blockerBound = input
                .entities()
                .filter(e -> !e.bb.broken)
                .reduce(Float.MAX_VALUE, (acc, x) -> Math.min(acc, x.body.getPosition().x), Math::min);
    }

    public record Input(
            PhysicsBody body,
            BreakableBlocker bb
    ) {}
}
