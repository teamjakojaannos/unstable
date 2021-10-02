package fi.jakojaannos.unstable.acts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.GameState;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Player;
import fi.jakojaannos.unstable.level.Tile;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.renderer.RenderPlayer;
import fi.jakojaannos.unstable.renderer.RenderTiles;
import fi.jakojaannos.unstable.resources.Interactable;
import fi.jakojaannos.unstable.systems.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("rawtypes")
public class IntroAct {
    private static final int WIDTH = 8;
    private static final int HEIGHT = 5;
    private static final int[] TILES = new int[]{
            // @formatter:off
            80, 81, 82, 83, 84, 85, 86, 87,
            0,  0,  0,  0,  0,  0,  0,  1,
            0,  1,  0,  2,  0,  0,  2,  0,
            0,  2,  0,  0,  1,  0,  0,  0,
            0,  0,  0,  0,  0,  0,  0,  2,
            // @formatter:on
    };

    public Collection<EcsSystem> systems() {
        return List.of(
                new PlayerInputSystem(),
                new MoveCharacterSystem(),
                new PlayerLocatorSystem(),
                new CameraFollowsPlayerSystem(),
                new CollectInteractablesSystem(),
                new PlayerActionSystem()
        );
    }

    public GameState state() {
        final var gameState = new GameState();

        final var tiles = new ArrayList<Tile>();
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                final var index = y * WIDTH + x;
                final var id = TILES[index];
                tiles.add(new Tile(id, x, y));
            }
        }

        gameState.world()
                 .spawn(Entity.builder()
                              .component(new TileMap(tiles)));

        gameState.world()
                 .spawn(Player.create(new Vector2(2.0f, 0.0f)));

        // borders
        gameState.world().spawn(Entity.builder()
                                      .component(new PhysicsBody(-1.0f, 1.0f, 1.0f, 2.0f)));
        gameState.world().spawn(Entity.builder()
                                      .component(new PhysicsBody(101.0f, 1.0f, 1.0f, 2.0f)));

        // vending machine
        gameState.world().spawn(Entity.builder()
                                      .component(new PhysicsBody(10.0f, 0.0f, 3.0f, 3.0f))
                                      .component(new Interactable(() -> System.out.println("Yeet"))));

        return gameState;
    }

    public Collection<EcsSystem> renderSystems(final SpriteBatch batch) {
        return List.of(
                new RenderTiles(batch),
                new RenderPlayer(batch)
        );
    }
}
