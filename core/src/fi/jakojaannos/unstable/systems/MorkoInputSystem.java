package fi.jakojaannos.unstable.systems;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.*;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.ArrayList;
import java.util.Optional;

public class MorkoInputSystem implements EcsSystem<MorkoInputSystem.Input> {
    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        input.entities().forEach(entity -> {
            updateState(entity, resources);
            doMovement(entity, resources);
        });
    }

// ========== MOVEMENT ==========

    private void doMovement(Input entity, Resources resources) {

        final var ai = entity.ai;
        switch (ai.state) {
            case IDLING, ATTACKING -> entity.input.direction.setZero();
            case SEARCHING -> ai.getAttackTargetPos()
                    .or(ai::getTargetPos)
                    .ifPresent(target ->
                            setInputTowards(entity, target, Math.min(ai.attackRadius2, ai.targetDistance2))
                    );
            case CHASING, WANDERING -> ai.getTargetPos()
                    .ifPresent(target -> setInputTowards(entity, target, 0.5f));
            case TASK -> ai.taskList.currentTask()
                    .ifPresentOrElse(
                            _task -> {
                                entity.input.direction.setZero();
                                ai.taskList.update(entity.entity, resources);
                            },
                            () -> {
                                // task finished, set state back to normal
                                wanderOrIdle(entity, resources);
                            }
                    );
        }
    }

    private void setInputTowards(Input entity, Vector2 target, float targetDistance2) {
        var direction = target.cpy().sub(entity.body.getPosition());
        if (direction.len2() >= targetDistance2) {
            entity.input.direction.set(direction).nor();
        } else {
            entity.input.direction.setZero();
        }
    }

    // ========== STATE UPDATES ==========

    private void updateState(Input entity, Resources resources) {
        // for all states:
        if (playerIsWithinAttackRange(entity, resources)) {
            attack(entity, resources);
            return;
        }
        // can still see player -> keep chasing (but don't interrupt attack)
        if (playerIsWithinSightRadius(entity, resources) && !attackNotFinished(entity, resources)) {
            chase(entity, resources);
            return;
        }

        switch (entity.ai.state) {
            case CHASING -> updateChaseState(entity, resources);
            case SEARCHING -> updateSearchState(entity, resources);
            case WANDERING -> updateWanderState(entity, resources);
            case IDLING -> updateIdleState(entity, resources);
            case ATTACKING -> updateAttackState(entity, resources);
        }
    }

    private void updateChaseState(Input entity, Resources resources) {
        // can't see player anymore -> start searching
        search(entity, resources);
    }

    private void updateSearchState(Input entity, Resources resources) {
        // if I'm near my target -> attack it to check for player
        if (nearMyAttackTarget(entity)) {
            attack(entity, resources);
            return;
        }

        final var noAttackTarget = entity.ai.getAttackTargetPos().isEmpty();
        if ((noAttackTarget && nearMyTarget(entity))
                || tiredOfSearching(entity, resources)
        ) {
            // I lost player and can't find hiding spots, or I got bored
            wanderOrIdle(entity, resources);
        }
    }

    private void updateWanderState(Input entity, Resources resources) {
        if (nearMyTarget(entity)) {
            wanderOrIdle(entity, resources);
        }
    }

    private void updateIdleState(Input entity, Resources resources) {
        if (shouldKeepIdling(entity, resources)) {
            return;
        }

        wander(entity, resources);
    }

    private void updateAttackState(Input entity, Resources resources) {
        if (attackNotFinished(entity, resources)) {
            return;
        }

        wanderOrIdle(entity, resources);
    }

    // ========== STATE SETTING ==========

    private void wanderOrIdle(Input entity, Resources resources) {
        if (MathUtils.randomBoolean()) {
            wander(entity, resources);
        } else {
            idle(entity, resources);
        }
    }

    private void idle(Input entity, Resources resources) {
        if (hasTask(entity)) {
            scriptedAction(entity);
            return;
        }
        entity.ai.state = MorkoAi.State.IDLING;

        if (!resources.timers.isActiveAndValid(entity.ai.idleHandle)) {
            entity.ai.idleHandle = resources.timers.set(entity.ai.idleTime, false, () -> {
            });
        }
    }

    private void wander(Input entity, Resources resources) {
        if (hasTask(entity)) {
            scriptedAction(entity);
            return;
        }
        entity.ai.state = MorkoAi.State.WANDERING;

        final var borders = resources.worldBounds;
        final var body = entity.body.getPosition();

        final var dx = entity.ai.sightRadius
                * MathUtils.random(0.25f, 2.0f)
                * (MathUtils.randomBoolean() ? -1f : 1f);

        final var x = MathUtils.clamp(
                body.x + dx,
                borders.min.x,
                borders.max.x
        );

        entity.ai.setTargetPos(new Vector2(x, body.y));
    }

    private void chase(Input entity, Resources resources) {
        entity.ai.state = MorkoAi.State.CHASING;

        resources.players
                .getPlayerPosition()
                .ifPresent(pos -> {
                    entity.ai.setTargetPos(pos);
                    entity.ai.setAttackTarget(pos);
                });
    }

    private void search(Input entity, Resources resources) {
        entity.ai.state = MorkoAi.State.SEARCHING;

        if (!resources.timers.isActiveAndValid(entity.ai.searchHandle)) {
            entity.ai.searchHandle = resources.timers.set(entity.ai.loseAggroTime, false, () -> {
            });
        }

        final var radius = entity.ai.sightRadius / 4.0f;
        entity.ai.getTargetPos()
                .flatMap(target -> getNearestAttackTarget(target, resources, radius))
                .ifPresentOrElse(
                        entity.ai::setAttackTarget,
                        entity.ai::clearAttackTarget
                );
    }

    /**
     * Excludes player, finds closets and other hidings spots.
     *
     * @param pos get hiding spots near this position
     */
    private Optional<Vector2> getNearestAttackTarget(
            Vector2 pos,
            Resources resources,
            float searchRadius
    ) {
        return new ArrayList<>(resources.interactItems.items)
                .stream()
                .filter(item -> item.entity().hasComponent(HidingSpot.class))
                .filter(item -> item.body().getPosition().dst2(pos) <= searchRadius * searchRadius)
                .min((o1, o2) -> Float.compare(
                        // find the closest one
                        o1.body().getPosition().dst2(pos),
                        o2.body().getPosition().dst2(pos)))
                .map(item -> item.body().getPosition());
    }

    private void attack(Input entity, Resources resources) {
        entity.ai.state = MorkoAi.State.ATTACKING;

        if (!resources.timers.isActiveAndValid(entity.ai.attackHandle)) {
            entity.ai.attackHandle = resources.timers.set(entity.ai.attackDuration, false, () -> {
            });
        }
    }

    private void scriptedAction(Input entity) {
        entity.ai.state = MorkoAi.State.TASK;
    }

    // ========== CHECKING ==========

    private boolean playerIsWithinSightRadius(Input entity, Resources resources) {
        final var body = entity.body;
        final var ai = entity.ai;
        final var optPlayer = resources.players.getPlayer();

        if (optPlayer.isEmpty()) {
            return false;
        }

        final var player = optPlayer.get();
        if (player.hasComponent(Hiding.class)) {
            return false;
        }

        return player.getComponent(PhysicsBody.class)
                .map(playerBody -> {
                    final var sightRadius2 = Math.pow(ai.sightRadius, 2);
                    return playerBody.getPosition().dst2(body.getPosition()) <= sightRadius2;
                })
                .orElse(false);
    }

    private boolean playerIsWithinAttackRange(Input entity, Resources resources) {
        final var body = entity.body;
        final var ai = entity.ai;
        final var optPlayer = resources.players.getPlayer();

        if (optPlayer.isEmpty()) {
            return false;
        }

        final var player = optPlayer.get();
        if (player.hasComponent(Hiding.class)) {
            return false;
        }

        return player.getComponent(PhysicsBody.class)
                .map(playerBody -> playerBody.getPosition().dst2(body.getPosition()) <= ai.attackRadius2)
                .orElse(false);
    }

    private boolean nearMyAttackTarget(Input entity) {
        return entity.ai.getAttackTargetPos()
                .map(target -> target.dst2(entity.body.getPosition()) <= entity.ai.attackRadius2)
                .orElse(false);
    }

    private boolean nearMyTarget(Input entity) {
        return entity.ai.getTargetPos()
                .map(target -> target.dst2(entity.body.getPosition()) <= entity.ai.targetDistance2)
                .orElse(false);
    }

    private boolean tiredOfSearching(Input entity, Resources resources) {
        return !resources.timers.isActiveAndValid(entity.ai.searchHandle);
    }

    private boolean shouldKeepIdling(Input entity, Resources resources) {
        return resources.timers.isActiveAndValid(entity.ai.idleHandle);
    }

    private boolean attackNotFinished(Input entity, Resources resources) {
        return resources.timers.isActiveAndValid(entity.ai.attackHandle);
    }

    private boolean hasTask(Input entity) {
        return entity.ai.taskList.currentTask().isPresent();
    }

    public record Input(
            Entity entity,
            MorkoAi ai,
            MovementInput input,
            PhysicsBody body,
            Tags.Morko morkoTag
    ) {
    }
}
