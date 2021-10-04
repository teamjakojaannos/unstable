package fi.jakojaannos.unstable.acts.act3;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.acts.end.TheEnd;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Poster;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.level.TileSet;
import fi.jakojaannos.unstable.renderer.TextRenderer;
import fi.jakojaannos.unstable.resources.PopUp;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.List;

public class WaitingRoom {
    private static final int WIDTH = 16;
    private static final int HEIGHT = 9;
    private static final String[] TILES = new String[]{
            // @formatter:off
            "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",
            "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            // @formatter:on
    };

    public static Room create() {
        return new Room() {
            @Override
            public TileMap createMap() {
                return TileMap.parse(TileSet.MANSION, TILES, WIDTH, HEIGHT);
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
                world.spawn(Poster.createDoor(
                        new Vector2(WIDTH - 2.0f, 1.0f),
                        TheEnd.THE_OFFICE,
                        new TheEnd()
                ));

                world.spawn(Poster.create(
                        new Vector2(8.0f, 1.5f),
                        Poster.Type.POSTER,
                        new PopUp(List.of(), PopUp.Background.Article3),
                        (s, r) -> true,
                        List.of(
                                List.of(new TextRenderer.TextOnScreen("That... that can't be right!"),
                                        new TextRenderer.TextOnScreen("They are blaming this on me?")),
                                List.of(new TextRenderer.TextOnScreen("But I wasn't even here at that time!"))
                        )));

                world.spawn(Poster.create(
                        new Vector2(WIDTH - 3.5f, 1.5f),
                        Poster.Type.MedicalReport,
                        new PopUp(List.of(), PopUp.Background.PatientRecord),
                        (s, r) -> true,
                        List.of(
                                List.of(new TextRenderer.TextOnScreen("What.. the...")),
                                List.of(new TextRenderer.TextOnScreen("That is... mine? A patient record?")),
                                List.of(new TextRenderer.TextOnScreen("What is going on?"))
                        )));
            }
        };
    }
}
