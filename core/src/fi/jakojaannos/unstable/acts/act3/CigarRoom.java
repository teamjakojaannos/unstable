package fi.jakojaannos.unstable.acts.act3;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.acts.act2.Act2;
import fi.jakojaannos.unstable.components.Tags;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Poster;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.level.TileSet;
import fi.jakojaannos.unstable.resources.Resources;

public class CigarRoom {
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
            public int width() {
                return WIDTH;
            }
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
                world.spawn(Poster.create(
                        new Vector2(8.0f, 1.0f),
                        Poster.Type.Furnace,
                        null
                ));

                world.spawn(Poster.createSofa(new Vector2(3.0f, 1.0f)));

                world.spawn(Poster.createDoor(
                        new Vector2(WIDTH - 2.0f, 1.0f),
                        Act2.MIRROR_ROOM_SPOOPY,
                        new Act2(), // HACK: use act2 to re-enable special rendering
                        new Vector2(10.0f, 1.0f),
                        null,
                        (s, r) -> {
                            r.player.addComponent(new Tags.FreezeInput());
                            r.timers.set(2.5f, false, () -> {
                                if (r.player.hasComponent(Tags.FreezeInput.class)) {
                                    r.player.removeComponent(Tags.FreezeInput.class);
                                }
                            });

                            return true;
                        }
                ));
            }
        };
    }
}
