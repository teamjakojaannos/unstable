package fi.jakojaannos.unstable.acts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.GameState;
import fi.jakojaannos.unstable.components.HidingSpot;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Closet;
import fi.jakojaannos.unstable.entities.Morko;
import fi.jakojaannos.unstable.entities.Player;
import fi.jakojaannos.unstable.level.Tile;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.level.TileSet;
import fi.jakojaannos.unstable.renderer.*;
import fi.jakojaannos.unstable.systems.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("rawtypes")
public class MansionIntroAct {
    private static final int WIDTH = 16;
    private static final int HEIGHT = 9;
    private static final String[] TILES = new String[]{
            // @formatter:off
            "f", "f", "f", "f", "f", "f", "f", "f", "f", "f", "f",       "f", "f",       "f", "f",       "f",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "tapetti", "w", "tapetti", "w", "tapetti", "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",       "w", "w",       "w", "w",       "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",       "w", "w",       "w", "w",       "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",       "w", "w",       "w", "w",       "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",       "w", "w",       "w", "w",       "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",       "w", "w",       "w", "w",       "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",       "w", "w",       "w", "w",       "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",       "w", "w",       "w", "w",       "w",
            // @formatter:on
    };

    private static final String[] WALL_IDS = new String[]{"w", "tapetti"};

    public Collection<EcsSystem> systems() {
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

    public GameState state() {
        final var gameState = new GameState();

        final var tileMap = TileMap.parse(TileSet.MANSION, TILES, WIDTH, HEIGHT);
        gameState.world()
                 .spawn(Entity.builder()
                              .component(tileMap));

        final var player = gameState
                .world()
                .spawn(Player.create(new Vector2(2.0f, 1.0f)));

        gameState.world().spawn(Closet.create(new Vector2(1.0f, 1.0f), player, HidingSpot.Type.MansionClosetLarge));
        gameState.world().spawn(Closet.create(new Vector2(6.0f, 1.0f), player, HidingSpot.Type.WallHole));


        gameState.world()
                .spawn(Morko.create(new Vector2(16.0f, 1.0f)));

        // borders
        gameState.world().spawn(Entity.builder()
                                      .component(new PhysicsBody(-1.0f, 1.0f, 1.0f, 2.0f)));
        gameState.world().spawn(Entity.builder()
                                      .component(new PhysicsBody(101.0f, 1.0f, 1.0f, 2.0f)));

        return gameState;
    }

    public Collection<EcsSystem> renderSystems(final SpriteBatch batch) {
        return List.of(
                new RenderTiles(batch),
                new RenderHidingSpot(batch),
                new RenderPlayer(batch),
                new RenderMorko(batch),
                new TextRenderer(batch)
        );
    }
}
