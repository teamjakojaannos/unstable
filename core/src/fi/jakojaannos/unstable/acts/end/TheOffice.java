package fi.jakojaannos.unstable.acts.end;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Poster;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.level.TileSet;
import fi.jakojaannos.unstable.resources.Resources;

public class TheOffice {
    private static final int WIDTH = 16;
    private static final int HEIGHT = 9;
    private static final String[] TILES = new String[]{
            // @formatter:off
            "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",  "f",
            "tapetti", "w_", "tapetti", "w_", "tapetti", "w_", "tapetti", "w_", "tapetti", "w_", "tapetti", "w_", "tapetti", "w_", "tapetti", "w_",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3", "w3",
            // @formatter:on
    };

    public static Room create(boolean spoopy) {
        return new Room() {
            @Override
            public TileMap createMap() {
                return TileMap.parse(TileSet.MANSION, TILES, WIDTH, HEIGHT);
            }

            @Override
            public Vector2 playerStartPosition() {
                return new Vector2(6.9f, 1.0f);
            }

            @Override
            public void spawnInitialEntities(
                    EcsWorld world,
                    Resources res,
                    Entity player
            ) {
                res.spoopy = spoopy;

                world.spawn(Poster.create(
                        new Vector2(8.0f, 1.0f),
                        Poster.Type.Furnace,
                        null
                ));

                world.spawn(Poster.create(
                        new Vector2(3.0f, 1.0f),
                        Poster.Type.DOCTOR,
                        null
                ));

                world.spawn(Poster.create(
                        new Vector2(1.5f, 1.0f),
                        Poster.Type.StatueTable,
                        null
                ));
            }
        };
    }
}
