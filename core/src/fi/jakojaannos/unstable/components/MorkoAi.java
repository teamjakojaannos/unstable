package fi.jakojaannos.unstable.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Null;
import fi.jakojaannos.unstable.UnstableGame;
import fi.jakojaannos.unstable.ecs.Component;

import java.util.Optional;

public class MorkoAi implements Component<MorkoAi> {


    public final float sightRadius;
    public final float targetDistance2 = 1.0f;
    private final float loseAggroTime;
    private final float idleTime;

    public State state;
    public long idleStartTimestamp;
    public long playerLastSightingTimestamp;

    @Null
    private Vector2 targetPos = null;

    public MorkoAi(float sightRadius, float loseAggroTime, float idleTime, State state) {
        this.sightRadius = sightRadius;
        this.loseAggroTime = loseAggroTime;
        this.idleTime = idleTime;
        this.state = state;

        this.idleStartTimestamp = (long) -idleTime;
        this.playerLastSightingTimestamp = -10000;
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

    public long loseAggroTimeInTicks() {
        return (long) (this.loseAggroTime * UnstableGame.Constants.GameLoop.TICKS_PER_SECOND);
    }

    public long idleTimeInTicks() {
        return (long) (this.idleTime * UnstableGame.Constants.GameLoop.TICKS_PER_SECOND);
    }

    public enum State {
        IDLING, CHASING, WANDERING, SEARCHING
    }
}
