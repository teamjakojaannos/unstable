package fi.jakojaannos.unstable.acts.act2;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.HidingSpot;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Closet;
import fi.jakojaannos.unstable.entities.Poster;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.level.TileSet;

public class ManorEntranceRoom {
    public static final int WIDTH = 24;
    private static final int HEIGHT = 9;
    private static final String[] TILES = new String[]{
            // @formatter:off
            "f", "f", "f", "f", "f", "f", "f", "f", "f", "f", "f", "f", "f", "f", "f", "f", "f", "f", "f", "f", "f", "f", "f", "f",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",
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
            public void spawnInitialEntities(EcsWorld world, Entity player) {
                world.spawn(Closet.create(new Vector2(1.0f, 1.0f), player, HidingSpot.Type.MansionClosetLarge));
                world.spawn(Closet.create(new Vector2(6.0f, 1.0f), player, HidingSpot.Type.WallHole));

                world.spawn(Poster.createDoor(new Vector2(WIDTH - 12, 1.0f),
                                              Act2.NURSE_HALLWAY,
                                              null,
                                              null,
                                              (s, r) -> r.playerInventory.photo));
                world.spawn(Poster.createDoor(new Vector2(WIDTH - 6, 1.0f), Act2.SMALL_BEDROOM, null));
            }
        };
    }
}
