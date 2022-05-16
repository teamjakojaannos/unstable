package fi.jakojaannos.unstable.systems;

import fi.jakojaannos.unstable.InputManager;
import fi.jakojaannos.unstable.components.MovementInput;
import fi.jakojaannos.unstable.components.PlayerInput;
import fi.jakojaannos.unstable.components.Tags;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

import static fi.jakojaannos.unstable.InputManager.KeyInput.*;

public class PlayerInputSystem implements EcsSystem<PlayerInputSystem.Input> {

    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        input.entities()
             .forEach(entity -> {
                 final var movementInput = entity.movementInput();
                 final var inputState = resources.playerInput;

                 final var xAxis = asInt(inputState.getState(RIGHT)) - asInt(inputState.getState(LEFT));
                 movementInput.direction.set(xAxis, 0.0f);

                 if (resources.getDialogueText() != null && !resources.getDialogueText().isEmpty()) {
                     movementInput.direction.set(0.0f, 0.0f);
                 }

                 // set to false when DOWN or RELEASED
                 entity.playerInput.actionPressed = (inputState.getState(ACTION) == InputManager.State.PRESSED);

                 // cheat for opening door
                 if (inputState.getState(KeyC) == InputManager.State.PRESSED) {
                     resources.combinationSolved = true;
                 }
             });
    }

    private static int asInt(final InputManager.State state) {
        return state == InputManager.State.RELEASED ? 0 : 1;
    }

    public record Input(
            MovementInput movementInput,
            PlayerInput playerInput,
            Tags.Player playerTag
    ) {}
}
