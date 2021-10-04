package fi.jakojaannos.unstable.acts.act2;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.HidingSpot;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.SoundTags;
import fi.jakojaannos.unstable.components.Tags;
import fi.jakojaannos.unstable.components.Trigger;
import fi.jakojaannos.unstable.components.tasks.TaskMove;
import fi.jakojaannos.unstable.components.tasks.TaskWait;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Closet;
import fi.jakojaannos.unstable.entities.Morko;
import fi.jakojaannos.unstable.entities.Poster;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.level.TileSet;

import java.util.List;

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

                world.spawn(Poster.createDoor(new Vector2(WIDTH - 6, 1.0f),
                                              Act2.MIRROR_ROOM,
                                              null,
                                              null,
                                              (s, r) -> r.playerInventory.photo));
                world.spawn(Poster.createDoor(new Vector2(WIDTH - 12, 1.0f), Act2.SMALL_BEDROOM, null));

                world.spawn(Entity.builder()
                        .component(new PhysicsBody(16.0f, 1.0f, 1.0f, 1.0f))
                        .component(new Trigger(1.0f, false,
                                res -> res.entities.spawn(
                                        Morko.create(new Vector2(28.0f, 1.0f),
                                                List.of(
                                                        new TaskMove(new Vector2(5.0f, 1.0f), 1.0f),
                                                        new TaskWait(2.5f),
                                                        new TaskMove(new Vector2(35.0f, 1.0f), 1.0f)
                                                )
                                        )
                                )))
                );
            }
        };
    }
}
