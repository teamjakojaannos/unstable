package fi.jakojaannos.unstable.acts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fi.jakojaannos.unstable.GameState;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Player;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.renderer.*;
import fi.jakojaannos.unstable.resources.Resources;
import fi.jakojaannos.unstable.systems.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fi.jakojaannos.unstable.Shaders.gradientFragmentShader;
import static fi.jakojaannos.unstable.Shaders.gradientVertexShader;

public interface Act {
    Map<Room, GameState> states = new HashMap<>();

    Room defaultRoom();

    default Collection<EcsSystem> systems() {
        return List.of(
                new CollectBlockersSystem(),
                new AttackTargetCollectorSystem(),
                new PlayerInputSystem(),
                new MorkoInputSystem(),
                new NurseInputSystem(),
                new MoveCharacterSystem(),
                new PlayerLocatorSystem(),
                new CameraFollowsPlayerSystem(),
                new CollectInteractablesSystem(),
                new TriggerObserverSystem(),
                new PlayerActionSystem(),
                new AttackHandlerSystem()
        );
    }

    default boolean isLightningEnabled() {
        return false;
    }

    default GameState state(Resources resources) {
        return state(defaultRoom(), resources);
    }

    default GameState state(Room room, Resources resources) {
        if (states.containsKey(room)) {
            final var state = states.get(room);

            final var playerPosition = resources.spawnPos != null
                    ? resources.spawnPos
                    : room.playerStartPosition();
            resources.spawnPos = null;

            resources.resetPlayer();
            resources.player.getComponent(PhysicsBody.class)
                            .ifPresent(body -> body.setPosition(playerPosition));
            state.world().spawn_player_hack(resources.player);

            return state;
        }

        final var gameState = new GameState();

        final var tileMap = room.createMap();
        gameState.world().spawn(Entity.builder().component(tileMap));

        final var playerPosition = resources.spawnPos != null
                ? resources.spawnPos
                : room.playerStartPosition();
        resources.spawnPos = null;

        if (resources.player == null) {
            resources.player = gameState.world().spawn(Player.create(playerPosition));
        } else {
            resources.resetPlayer();

            resources.player.getComponent(PhysicsBody.class)
                            .ifPresent(body -> body.setPosition(playerPosition));
            gameState.world().spawn_player_hack(resources.player);
        }

        room.spawnInitialEntities(gameState.world(), resources.player);

        states.put(room, gameState);
        return gameState;
    }

    default Collection<EcsSystem> renderSystems(SpriteBatch batch) {
        return List.of(
                new SetShader(batch, gradientVertexShader, gradientFragmentShader),
                new SetCafeUniforms(),
                new RenderTiles(batch),
                new RenderHidingSpot(batch),
                new RenderPosters(batch),
                new RenderBBs(batch),
                new RenderMirror(batch),
                new RenderPlayer(batch),
                new RenderMorko(batch),
                new RenderNurse(batch),
                new SetShader(batch, null, null),
                new DebugRenderer(),
                new TextRenderer(batch),
                new DialogueRenderer(batch),
                new RenderSoundTags(),
                new RenderAmbience()
        );
    }
}
