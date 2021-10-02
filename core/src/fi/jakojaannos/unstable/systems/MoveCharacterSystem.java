package fi.jakojaannos.unstable.systems;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.MovementAttributes;
import fi.jakojaannos.unstable.components.MovementInput;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.Tags;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.ecs.Without;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.Optional;

public class MoveCharacterSystem implements EcsSystem<MoveCharacterSystem.Input> {
    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        input.entities()
             .forEach(entity -> {
                 final var movementInput = entity.input();
                 final var attributes = entity.attributes();
                 final var physics = entity.body();

                 final var clampedInput = movementInput.direction.len2() > 1
                         ? movementInput.direction.cpy().nor()
                         : movementInput.direction.cpy();

                 final var position = physics.body.getPosition();

                 physics.body.applyLinearImpulse(
                         clampedInput.scl(attributes.force),
                         position,
                         true
                 );
             });
    }

    public record Input(
            MovementInput input,
            MovementAttributes attributes,
            PhysicsBody body,
            Optional<Tags.InAir> isInAir,
            Without<Tags.FreezeInput> frozenInputTag
    ) {}
}
