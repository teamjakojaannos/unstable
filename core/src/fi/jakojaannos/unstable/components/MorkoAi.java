package fi.jakojaannos.unstable.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Null;
import fi.jakojaannos.unstable.ecs.Component;
import fi.jakojaannos.unstable.resources.TimerHandle;

import java.util.ArrayList;
import java.util.Optional;

public class MorkoAi implements Component<MorkoAi> {
    public final float sightRadius;
    public final float targetDistance2 = 0.75f;
    public final float attackRadius2 = 1.0f;
    public final float loseAggroTime;
    public final float idleTime;
    public final float attackDuration = 0.66f;
    public final TaskList<State> taskList;
    public State state;

    @Null
    public TimerHandle searchHandle;
    @Null
    public TimerHandle idleHandle;
    @Null
    public TimerHandle attackHandle;

    @Null
    private Vector2 targetPos = null;
    @Null
    private Vector2 attackTarget = null;

    public MorkoAi(float sightRadius, float loseAggroTime, float idleTime, State state, TaskList<State> taskList) {
        this.sightRadius = sightRadius;
        this.loseAggroTime = loseAggroTime;
        this.idleTime = idleTime;
        this.state = state;
        this.taskList = taskList;
    }

    public MorkoAi(float sightRadius, float loseAggroTime, float idleTime, State state) {
        this(sightRadius, loseAggroTime, idleTime, state, new TaskList<>(new ArrayList<>(), false));
    }

    public MorkoAi(float sightRadius, float loseAggroTime, float idleTime) {
        this(sightRadius, loseAggroTime, idleTime, State.IDLING);
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

    public Optional<Vector2> getAttackTargetPos() {
        return Optional.ofNullable(this.attackTarget);
    }

    public void setAttackTarget(Vector2 target) {
        if (this.attackTarget == null) {
            this.attackTarget = new Vector2(target);
        } else {
            this.attackTarget.set(target);
        }
    }

    public void clearAttackTarget() {
        this.attackTarget = null;
    }

    @Override
    public MorkoAi cloneComponent() {
        return new MorkoAi(this.sightRadius, this.loseAggroTime, this.idleTime, this.state, this.taskList);
    }

    public enum State {
        IDLING, CHASING, WANDERING, SEARCHING, ATTACKING, TASK
    }
}
