package fi.jakojaannos.unstable.acts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fi.jakojaannos.unstable.GameState;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Player;
import fi.jakojaannos.unstable.level.Room;

import java.util.Collection;

public interface Act {
    Collection<EcsSystem> systems();

    Room defaultRoom();

    default GameState state() {
        return state(defaultRoom());
    }

    default GameState state(Room room) {
        final var gameState = new GameState();

        final var tileMap = room.createMap();
        gameState.world().spawn(Entity.builder().component(tileMap));

        final var playerPosition = room.playerStartPosition();
        final var player = gameState.world().spawn(Player.create(playerPosition));

        room.spawnInitialEntities(gameState.world(), player);

        return gameState;
    }

    Collection<EcsSystem> renderSystems(SpriteBatch batch);
}
