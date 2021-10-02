package fi.jakojaannos.unstable;

import com.badlogic.gdx.Gdx;

import java.util.HashMap;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys.*;

public class InputManager {

    private final Map<KeyInput, State> states = new HashMap<>();
    private int latestUpdate = -1;

    public void updateKeyStates(int currentTick) {
        // prevent updating twice in same tick. current should never be smaller than latest, but just in case...
        if (currentTick <= latestUpdate) {
            return;
        }

        latestUpdate = currentTick;

        for (final var key : KeyInput.values()) {

            if (!key.isPressed()) {
                this.states.put(key, State.RELEASED);
            } else {

                final var oldState = states.getOrDefault(key, State.RELEASED);
                switch (oldState) {
                    case RELEASED -> this.states.put(key, State.PRESSED);
                    case PRESSED -> this.states.put(key, State.DOWN);
                    case DOWN -> {/* NO-OP */ }
                }
            }
        }
    }

    public State getState(final KeyInput key) {
        return this.states.getOrDefault(key, State.RELEASED);
    }

    public enum KeyInput {
        LEFT(A), RIGHT(D), ACTION(F), ACTION_2(G);

        private final int keyCode;

        KeyInput(final int keyCode) {
            this.keyCode = keyCode;
        }

        private boolean isPressed() {
            return Gdx.input.isKeyPressed(this.keyCode);
        }
    }

    public enum State {
        RELEASED, PRESSED, DOWN
    }
}
