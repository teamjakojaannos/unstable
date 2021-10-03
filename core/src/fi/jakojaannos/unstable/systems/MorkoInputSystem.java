package fi.jakojaannos.unstable.systems;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.*;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.Optional;

public class MorkoInputSystem implements EcsSystem<MorkoInputSystem.Input> {
    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        input.entities().forEach(entity -> {
            updateState(entity, resources);
            doMovement(entity);
        });
    }

    private void updateState(Input entity, Resources resources) {
        if (canSeePlayer(entity, resources)) {
            entity.ai.state = MorkoAi.State.CHASING;
            entity.ai.playerLastSightingTimestamp = resources.timeManager.currentTick();
            // this should be always available, so I should be able to unwrap, but better safe than sorry
            resources.playerPosition()
                    .ifPresent(entity.ai::setTargetPos);
            return;
        }

        // can't see player and I haven't had my coffee break -> keep idling
        if (shouldKeepIdling(entity, resources)) {
            return;
        }

        var targetPos = entity.ai.getTargetPos();
        if (targetPos.isEmpty()) {
            // no target, player is not in sight: roll a die, idle or wander
            if (MathUtils.randomBoolean()) {
                entity.ai.state = MorkoAi.State.IDLING;
                entity.ai.idleStartTimestamp = resources.timeManager.currentTick();
            } else {
                pickNewTarget(entity, resources);
            }
            return;
        }

        // I can't see player, but I have a target. Did I see player recently near me?
        if (sawPlayerNearMeRecently(entity, resources)) {
            entity.ai.state = MorkoAi.State.SEARCHING;
            return;
        }

        // I can't see player, and he wasn't near me recently. Either:
        // continue to the target, pick a new one or start idling

        if (imFarFromMyTarget(entity, targetPos.get())) {
            // I'm not at my target, keep going
            return;
        }


        // flip coin: take a break or pick new target
        if (MathUtils.randomBoolean()) {
            entity.ai.state = MorkoAi.State.IDLING;
            entity.ai.idleStartTimestamp = resources.timeManager.currentTick();
            entity.ai.clearTargetPos();
        } else {
            pickNewTarget(entity, resources);
        }
    }

    private boolean imFarFromMyTarget(Input entity, Vector2 targetPos) {
        return entity.body.getPosition().dst2(targetPos) > entity.ai.targetDistance2;
    }

    private void pickNewTarget(Input entity, Resources resources) {
        entity.ai.state = MorkoAi.State.WANDERING;
        final var direction = MathUtils.randomBoolean() ? -1.0f : 1.0f;
        final var multiplier = MathUtils.random(0.25f, 1.0f);
        final var dx = multiplier * entity.ai.sightRadius * direction;

        final var clampedX = MathUtils.clamp(
                dx + entity.body.getPosition().x,
                resources.worldBounds.min.x,
                resources.worldBounds.max.x);

        var target = new Vector2(clampedX, 0.0f);
        entity.ai.setTargetPos(target);
    }

    private boolean sawPlayerNearMeRecently(Input entity, Resources resources) {
        final var ai = entity.ai;

        final var pos = entity.ai.getTargetPos();
        if (pos.isEmpty()) {
            return false;
        }

        final var isNearMe = pos.get().dst2(entity.body.getPosition()) <= ai.sightRadius * ai.sightRadius;
        final var isRecent =
                resources.timeManager.currentTick() - ai.playerLastSightingTimestamp < ai.loseAggroTimeInTicks();

        return isNearMe && isRecent;
    }

    private boolean shouldKeepIdling(Input entity, Resources resources) {
        if (entity.ai.state != MorkoAi.State.IDLING) {
            return false;
        }

        final var timeIdled = resources.timeManager.currentTick() - entity.ai.idleStartTimestamp;
        return timeIdled < entity.ai.idleTimeInTicks();
    }

    private boolean canSeePlayer(Input entity, Resources resources) {
        var optPlayer = resources.players.getPlayer();
        if (optPlayer.isEmpty()) {
            return false;
        }
        var player = optPlayer.get();

        if (player.hasComponent(Hiding.class)) {
            return false;
        }

        var playerPos = resources.players.getPlayerPosition();
        if (playerPos.isEmpty()) {
            return false;
        }

        final var sightRadius2 = Math.pow(entity.ai.sightRadius, 2);
        return entity.body.getPosition().dst2(playerPos.get()) <= sightRadius2;
    }


    private void doMovement(Input entity) {
        final var targetDistance2 = 0.5f;

        final var ai = entity.ai;
        switch (ai.state) {
            case IDLING -> entity.input.direction.setZero();
            case CHASING, WANDERING, SEARCHING -> ai.getTargetPos().ifPresent(target -> {
                var direction = target.cpy().sub(entity.body.getPosition());
                if (direction.len2() >= targetDistance2) {
                    entity.input.direction.set(direction).nor();
                } else {
                    entity.input.direction.setZero();
                }
            });
        }
    }

    public record Input(
            MorkoAi ai,
            MovementInput input,
            PhysicsBody body,
            Tags.Morko morkoTag
    ) {
    }
}
