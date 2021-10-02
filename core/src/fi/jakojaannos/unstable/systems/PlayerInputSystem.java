package fi.jakojaannos.unstable.systems;

import fi.jakojaannos.unstable.components.MovementInput;
import fi.jakojaannos.unstable.components.Tags;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class PlayerInputSystem implements EcsSystem<PlayerInputSystem.Input> {

    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        input.entities()
             .forEach(entity -> {
                 final var movementInput = entity.movementInput();

                 final var inputState = resources.playerInput;

                 final var xAxis = asInt(inputState.rightPressed) - asInt(inputState.leftPressed);
                 final var yAxis = asInt(inputState.upPressed) - asInt(inputState.downPressed);

                 movementInput.direction.set(xAxis, yAxis);
             });
    }

    private static int asInt(boolean b) {
        return b ? 1 : 0;
    }

    public record Input(
            MovementInput movementInput,
            Tags.Player playerTag
    ) {}
}
