package fi.jakojaannos.unstable.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Null;
import fi.jakojaannos.unstable.ecs.Component;
import fi.jakojaannos.unstable.resources.TimerHandle;

import java.util.Optional;

public class MorkoAi implements Component<MorkoAi> {
    public final float sightRadius;
    public final float targetDistance2 = 0.75f;
    public final float attackRadius2 = 1.0f;
    public final float loseAggroTime;
    public final float idleTime;
    public final float attackDuration = 0.66f;
    public State state;

    @Null
    public TimerHandle searchHandle;
    @Null
    public TimerHandle idleHandle;
    @Null
    public TimerHandle attackHandle;

    @Null
    private Vector2 targetPos = null;

    public MorkoAi(float sightRadius, float loseAggroTime, float idleTime, State state) {
        this.sightRadius = sightRadius;
        this.loseAggroTime = loseAggroTime;
        this.idleTime = idleTime;
        this.state = state;
    }

    public MorkoAi(float sightRadius, float loseAggroTime, float idleTime) {
        this(sightRadius, loseAggroTime, idleTime, State.IDLING);
    }

    public void clearTargetPos() {
        this.targetPos = null;
    }

    public Optional<Vector2> getTargetPos() {
        return Optional.ofNullable(targetPos);
    }

    public void setTargetPos(Vector2 target) {
        if (this.targetPos == null) {
            this.targetPos = new Vector2(target);
        } else {
            this.targetPos.set(target);
        }
    }

    @Override
    public MorkoAi cloneComponent() {
        return new MorkoAi(this.sightRadius, this.loseAggroTime, this.idleTime, this.state);
    }

    public enum State {
        IDLING, CHASING, WANDERING, SEARCHING, ATTACKING
    }
}
