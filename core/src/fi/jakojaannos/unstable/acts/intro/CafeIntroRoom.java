package fi.jakojaannos.unstable.acts.intro;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.acts.act1.Act1;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Nurse;
import fi.jakojaannos.unstable.entities.Poster;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.level.TileSet;
import fi.jakojaannos.unstable.renderer.TextRenderer;
import fi.jakojaannos.unstable.resources.PopUp;
import fi.jakojaannos.unstable.resources.Resources;

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
            public int width() {
                return WIDTH;
            }
            
            @Override
            public TileMap createMap() {
                return TileMap.parse(TileSet.CAFE, TILES, WIDTH, HEIGHT);
            }

            @Override
            public Vector2 playerStartPosition() {
                return new Vector2(2.0f, 1.0f);
            }

            @Override
            public void spawnInitialEntities(
                    EcsWorld world,
                    Resources res,
                    Entity player
            ) {
                world.spawn(Poster.create(new Vector2(14.0f, 2.5f), Poster.Type.POSTER,
                                          new PopUp(List.of(), PopUp.Background.Newspaper)));

                world.spawn(Poster.create(
                        new Vector2(WIDTH - 8, 1.0f),
                        Poster.Type.CAFE_COUNTER,
                        new PopUp(List.of(), PopUp.Background.Article1),
                        (s, r) -> {
                            // TODO: open another popup, a close-up of the image before changing the act
                            //  - should have a dimmer to hide the background transition
                            r.nextAct = new Act1();
                            return true;
                        },
                        List.of(
                                List.of(new TextRenderer.TextOnScreen("There is an article about weird"),
                                        new TextRenderer.TextOnScreen("disappearances nearby.")),
                                List.of(new TextRenderer.TextOnScreen("Wonder if this is related to my investigation."),
                                        new TextRenderer.TextOnScreen("I should pay a visit to that old castle…")),
                                List.of(new TextRenderer.TextOnScreen("Guess that’s where I'm headed next."))
                        )));

                world.spawn(Poster.create(
                        new Vector2(WIDTH - 8 + 0.75f, 2.05f),
                        Poster.Type.NEWSPAPER_ABOUT_TO_FALL,
                        null));

                for (int i = 0; i < 3; i++) {
                    world.spawn(Poster.create(
                            new Vector2(3.0f + i * 7.0f, 12.0f),
                            Poster.Type.Lambu,
                            null));

                    world.spawn(Poster.create(
                            new Vector2(2.0f + i * 7.0f, 3.0f),
                            Poster.Type.WINDOW,
                            null));

                    world.spawn(Poster.create(
                            new Vector2(3.0f + i * 7.0f, 1.0f),
                            Poster.Type.TABLE,
                            null));

                    world.spawn(Poster.create(
                            new Vector2(1f + i * 7.0f, 1.0f),
                            i % 2 == 0 ? Poster.Type.NpcNAINE : Poster.Type.NpcMIES,
                            null));

                    world.spawn(Poster.create(
                            new Vector2(5.0f + i * 7.0f, 1.0f),
                            i % 2 == 0 ? Poster.Type.NpcMIES2 : Poster.Type.NpcNAINE2,
                            null));
                }

                world.spawn(Poster.create(
                        new Vector2(23.0f, 4.75f),
                        Poster.Type.PAINTING,
                        null));

                world.spawn(Nurse.create(new Vector2(10.0f, 1.0f)));
            }
        };
    }
}
