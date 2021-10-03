package fi.jakojaannos.unstable.acts.act1;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Poster;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.level.TileSet;
import fi.jakojaannos.unstable.renderer.TextRenderer;
import fi.jakojaannos.unstable.resources.PopUp;

import java.util.List;

public class CafeIntroRoom {
    private static final int WIDTH = 29;
    private static final int HEIGHT = 16;
    private static final String[] TILES = new String[]{
            // @formatter:off
            "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",
            "w+", "w_", "w_", "w_", "w_", "w_", "w_", "w+", "w_", "w_", "w_", "w_", "w_", "w_", "w+", "w_", "w_", "w_", "w_", "w_", "w_", "w+", "w_", "w_", "w_", "w_", "w_", "w_", "w+",
            "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2",
            "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2", "w1", "w1", "w1", "w1", "w1", "w1", "w2",
            "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2", "w2",
            // @formatter:on
    };

    public static Room create() {
        return new Room() {
            @Override
            public TileMap createMap() {
                return TileMap.parse(TileSet.CAFE, TILES, WIDTH, HEIGHT);
            }

            @Override
            public Vector2 playerStartPosition() {
                return new Vector2(2.0f, 1.0f);
            }

            @Override
            public void spawnInitialEntities(EcsWorld world, Entity player) {
                world.spawn(Poster.create(new Vector2(14.0f, 2.5f), player, Poster.Type.POSTER,
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

                for (int i = 0; i < 4; i++) {
                    world.spawn(Poster.create(
                            new Vector2(2.0f + i * 7.0f, 2.0f),
                            player,
                            Poster.Type.WINDOW,
                            null));
                }
            }
        };
    }
}
