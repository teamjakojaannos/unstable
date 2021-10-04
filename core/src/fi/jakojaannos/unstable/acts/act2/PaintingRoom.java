package fi.jakojaannos.unstable.acts.act2;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.acts.act3.Act3;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Poster;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.level.TileSet;
import fi.jakojaannos.unstable.renderer.TextRenderer;
import fi.jakojaannos.unstable.resources.PopUp;

import java.util.List;

public class PaintingRoom {
    private static final int WIDTH = 24;
    private static final int HEIGHT = 9;
    private static final String[] TILES = new String[]{
            // @formatter:off
            "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",
            "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_", "w_",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
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
                return new Vector2(WIDTH - 8.0f, 1.0f);
            }

            @Override
            public void spawnInitialEntities(EcsWorld world, Entity player) {
                // Dummy door
                world.spawn(Poster.createDoor(new Vector2(WIDTH - 7.0f, 1.0f),
                                              null,
                                              null,
                                              null,
                                              (s, r) -> false));

                world.spawn(Poster.create(
                        new Vector2(19.0f, 2.25f),
                        Poster.Type.PuzzlePaintingA,
                        new PopUp(List.of(new TextRenderer.TextOnScreen("yee")),
                                  PopUp.Background.PuzzlePaintingA)
                ));

                world.spawn(Poster.create(
                        new Vector2(12.0f, 1.5f),
                        Poster.Type.PuzzlePaintingB,
                        new PopUp(List.of(new TextRenderer.TextOnScreen("yee")),
                                  PopUp.Background.PuzzlePaintingB)
                ));

                world.spawn(Poster.create(
                        new Vector2(7.0f, 1.75f),
                        Poster.Type.PuzzlePaintingC,
                        new PopUp(List.of(new TextRenderer.TextOnScreen("yee")),
                                  PopUp.Background.PuzzlePaintingC)
                ));

                // TODO: number lock
                world.spawn(Poster.createDoor(new Vector2(3, 1.0f), Act3.CIGAR_ROOM, new Act3()));
            }
        };
    }
}
