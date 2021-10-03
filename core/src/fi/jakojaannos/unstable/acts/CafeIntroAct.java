package fi.jakojaannos.unstable.acts;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.GameState;
import fi.jakojaannos.unstable.components.HidingSpot;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Closet;
import fi.jakojaannos.unstable.entities.Player;
import fi.jakojaannos.unstable.entities.Poster;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.level.TileSet;
import fi.jakojaannos.unstable.renderer.*;
import fi.jakojaannos.unstable.resources.PopUp;
import fi.jakojaannos.unstable.systems.*;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("rawtypes")
public class CafeIntroAct {
    private static final int WIDTH = 17;
    private static final int HEIGHT = 9;
    private static final String[] TILES = new String[]{
            // @formatter:off
            "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",
            "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_",
            "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2",
            "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w2",
            "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2",
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

        final var tileMap = TileMap.parse(TileSet.CAFE, TILES, WIDTH, HEIGHT);
        gameState.world()
                 .spawn(Entity.builder()
                              .component(tileMap));

        final var player = gameState
                .world()
                .spawn(Player.create(new Vector2(2.0f, 1.0f)));

        gameState.world().spawn(Closet.create(new Vector2(1.0f, 1.0f), player, HidingSpot.Type.MansionClosetThin));
        gameState.world().spawn(Poster.create(
                new Vector2(8.0f, 2.5f),
                player,
                new PopUp(List.of(new TextRenderer.TextOnScreen("Myydaan potkukelkkoja!\nJa paskoja vihanneksia.\nTerveisin Teslak Aarisaari",
                                                                0.55f,
                                                                0.60f,
                                                                0.5f),
                                  new TextRenderer.TextOnScreen("""
                                                                        Viime yona skotlantilainen juoppo sticky jumppasi pankin holviin ja rajaytti noin 400kg kultaa ja seitseman sentrya.
                                                                                                
                                                                        Han pakeni paikalta traktorilla ja soitti sakkipillia aamunkoittoon asti.""",
                                                                0.05f,
                                                                0.6f,
                                                                0.45f))
                )));

        // borders
        gameState.world().spawn(Entity.builder()
                                      .component(new PhysicsBody(-1.0f, 1.0f, 1.0f, 2.0f)));

        return gameState;
    }

    public Collection<EcsSystem> renderSystems(final SpriteBatch batch) {
        return List.of(
                new RenderTiles(batch),
                new RenderHidingSpot(batch),
                new RenderPosters(batch),
                new RenderPlayer(batch),
                new RenderMorko(batch),
                new TextRenderer(batch)
        );
    }
}
