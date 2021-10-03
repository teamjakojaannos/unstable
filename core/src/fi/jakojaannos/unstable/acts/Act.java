package fi.jakojaannos.unstable.acts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fi.jakojaannos.unstable.GameState;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Player;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.renderer.*;
import fi.jakojaannos.unstable.resources.Resources;
import fi.jakojaannos.unstable.systems.*;

import java.util.Collection;
import java.util.List;

import static fi.jakojaannos.unstable.Shaders.gradientFragmentShader;
import static fi.jakojaannos.unstable.Shaders.gradientVertexShader;

public interface Act {
    Room defaultRoom();

    default Collection<EcsSystem> systems() {
        return List.of(
                new PlayerInputSystem(),
                new MorkoInputSystem(),
                new MoveCharacterSystem(),
                new PlayerLocatorSystem(),
                new CameraFollowsPlayerSystem(),
                new CollectInteractablesSystem(),
                new PlayerActionSystem()
        );
    }

    default GameState state(Resources resources) {
        return state(defaultRoom(), resources);
    }

    default GameState state(Room room, Resources resources) {
        final var gameState = new GameState();

        final var tileMap = room.createMap();
        gameState.world().spawn(Entity.builder().component(tileMap));

        final var playerPosition = room.playerStartPosition();
        final var player = gameState.world().spawn(Player.create(playerPosition));

        room.spawnInitialEntities(gameState.world(), player);

        return gameState;
    }

    default Collection<EcsSystem> renderSystems(SpriteBatch batch) {
        return List.of(
                new SetShader(batch, gradientVertexShader, gradientFragmentShader),
                new SetCafeUniforms(),
                new RenderTiles(batch),
                new RenderHidingSpot(batch),
                new RenderPosters(batch),
                new RenderPlayer(batch),
                new RenderMorko(batch),
                new SetShader(batch, null, null),
                new DebugRenderer(),
                new TextRenderer(batch)
        );
    }
}
