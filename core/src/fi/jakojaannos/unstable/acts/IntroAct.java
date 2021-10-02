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
import fi.jakojaannos.unstable.level.TileSet;
import fi.jakojaannos.unstable.renderer.RenderMorko;
import fi.jakojaannos.unstable.renderer.RenderPlayer;
import fi.jakojaannos.unstable.renderer.RenderTiles;
import fi.jakojaannos.unstable.resources.Interactable;
import fi.jakojaannos.unstable.systems.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("rawtypes")
public class IntroAct {
    private static final int WIDTH = 16;
    private static final int HEIGHT = 8;
    private static final String[] TILES = new String[]{
            // @formatter:off
            "f", "f", "f", "f", "f", "f",    "f", "f", "f", "f", "f", "f", "f", "f", "f", "f",
            "w", "w", "w", "w", "w", "hole", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
            "w", "w", "w", "w", "w", "w",    "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
            "w", "w", "w", "w", "w", "w",    "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
            "w", "w", "w", "w", "w", "w",    "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
            "w", "w", "w", "w", "w", "w",    "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
            "w", "w", "w", "w", "w", "w",    "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
            "w", "w", "w", "w", "w", "w",    "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
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

        final var tileset = new TileSet(16, 16);
        tileset.addTile("f", 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95);
        tileset.addTile("w", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2);

        tileset.addProp("hole", 5, 0, 4, 4);
        tileset.addProp("closet_01", 0, 1, 3, 4);
        tileset.addProp("closet_02", 3, 1, 2, 4);

        final var filledByProps = new ArrayList<Tile>();
        final var tiles = new ArrayList<Tile>();
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                int finalX = x;
                int finalY = y;
                if (filledByProps.stream().anyMatch(tile -> tile.x() == finalX && tile.y() == finalY)) {
                    continue;
                }

                final var index = y * WIDTH + x;
                final var id = TILES[index];

                final var tile = tileset.getTile(id, x, y);
                if (tile.isPresent()) {
                    tiles.add(new Tile(tile.get(), x, y));
                } else {
                    final var prop = List.of(tileset.getProp(id, x, y));
                    filledByProps.addAll(prop);
                    tiles.addAll(prop);
                }

            }
        }

        final var tilesForeground = List.of(tileset.getProp("closet_01", 1, 1));


        gameState.world()
                 .spawn(Entity.builder()
                              .component(new TileMap(tiles)));
        gameState.world()
                 .spawn(Entity.builder()
                              .component(new TileMap(tilesForeground)));

        gameState.world()
                 .spawn(Player.create(new Vector2(2.0f, 1.0f)));

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
                new RenderPlayer(batch),
                new RenderMorko(batch)
        );
    }

    private record TilePosition(int x, int y) {}
}