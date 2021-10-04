package fi.jakojaannos.unstable.acts.act1;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.acts.act2.Act2;
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
                                                                                          0.45f)),
                                                    PopUp.Background.Newspaper)));

                world.spawn(Poster.create(
                        new Vector2(WIDTH - 8, 1.0f),
                        player,
                        Poster.Type.CAFE_COUNTER,
                        new PopUp(List.of(new TextRenderer.TextOnScreen("""
                                                                                Group of youngsters disappeared near {{ family name }} manor.
                                                                                """,
                                                                        0.05f,
                                                                        0.6f,
                                                                        0.45f)),
                                  PopUp.Background.Newspaper),
                        (s, r) -> {
                            // TODO: open another popup, a close-up of the image before changing the act
                            //  - should have a dimmer to hide the background transition
                            r.nextAct = new Act2();
                            return true;
                        },
                        List.of(
                                List.of(new TextRenderer.TextOnScreen("Hey… wait a minute, there is an article"),
                                        new TextRenderer.TextOnScreen("about my missing buddies here?")),
                                List.of(new TextRenderer.TextOnScreen("They disappeared right after they went to"),
                                        new TextRenderer.TextOnScreen("visit that old castle…")),
                                List.of(new TextRenderer.TextOnScreen("Guess that’s where I'm headed next.")),
                                List.of(new TextRenderer.TextOnScreen("There is something oddly familiar about that picture…"),
                                        new TextRenderer.TextOnScreen("...why do I feel like I’ve seen it before?"))
                        )));

                world.spawn(Poster.create(
                        new Vector2(WIDTH - 8 + 0.75f, 2.05f),
                        player,
                        Poster.Type.NEWSPAPER_ABOUT_TO_FALL,
                        null));

                for (int i = 0; i < 3; i++) {
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
