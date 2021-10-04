package fi.jakojaannos.unstable.components;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.ecs.Component;
import fi.jakojaannos.unstable.entities.Morko;
import fi.jakojaannos.unstable.resources.Resources;
import fi.jakojaannos.unstable.resources.TimerHandle;

public class Mirror implements Component<Mirror> {
    public State state = State.Idle;
    public TimerHandle stateProgress;

    public void nextState(Resources resources, Vector2 position) {
        if (this.state == State.Idle) {
            resources.spoopy = true;
        }

        this.state = switch (state) {
            case Idle -> State.GremlinsEnter;
            case GremlinsEnter -> State.Gremlins;
            case Gremlins -> State.GremlinsLeave;
            case GremlinsLeave -> State.LongBoiEnter;
            case LongBoiEnter -> State.PostLongBoi;
            case PostLongBoi -> State.Idle;
        };

        final var duration = switch (state) {
            case GremlinsEnter -> 2.5f;
            case Gremlins -> 0.5f;
            case GremlinsLeave -> 1.5f;
            case LongBoiEnter -> 3.5f;
            case PostLongBoi -> 9999.0f;
            case Idle -> 0.0f;
        };

        final boolean looping = switch (state) {
            case Gremlins, PostLongBoi -> true;
            default -> false;
        };
        final var s = this.state;
        resources.timers.clear(this.stateProgress);
        this.stateProgress = resources.timers.set(
                duration,
                looping,
                looping ? () -> {}
                        : () -> {
                    nextState(resources, position);
                    if (s == State.LongBoiEnter) {
                        resources.entities.spawn(Morko.create(position.cpy()));
                    } else if (s == State.GremlinsEnter) {
                        resources.timers.set(4.5f, false, () -> {
                            nextState(resources, position);
                        });
                    }
                });
    }

    @Override
    public Mirror cloneComponent() {
        return this;
    }

    public enum State {
        Idle,
        GremlinsEnter,
        Gremlins,
        GremlinsLeave,
        LongBoiEnter,
        PostLongBoi,
    }
}
