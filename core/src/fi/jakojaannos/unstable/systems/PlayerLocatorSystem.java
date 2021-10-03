package fi.jakojaannos.unstable.systems;

import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.Tags;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class PlayerLocatorSystem implements EcsSystem<PlayerLocatorSystem.Input> {

    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        input.entities()
                .findFirst()
                .ifPresentOrElse(player -> resources.players.setPlayer(player.entity),
                        () -> resources.players.setPlayer(null)
                );
    }

    public record Input(
            Entity entity,
            Tags.Player playerTag,
            PhysicsBody body
    ) {
    }
}
