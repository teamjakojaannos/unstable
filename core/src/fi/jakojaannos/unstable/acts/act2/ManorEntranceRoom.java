package fi.jakojaannos.unstable.acts.act2;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.HidingSpot;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Closet;
import fi.jakojaannos.unstable.entities.Morko;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.level.TileSet;

public class ManorEntranceRoom {
    private static final int WIDTH = 16;
    private static final int HEIGHT = 9;
    private static final String[] TILES = new String[]{
            // @formatter:off
            "f", "f", "f", "f", "f", "f", "f", "f", "f", "f", "f",       "f", "f",       "f", "f",       "f",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "tapetti", "w", "tapetti", "w", "tapetti", "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",       "w", "w",       "w", "w",       "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",       "w", "w",       "w", "w",       "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",       "w", "w",       "w", "w",       "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",       "w", "w",       "w", "w",       "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",       "w", "w",       "w", "w",       "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",       "w", "w",       "w", "w",       "w",
            "w", "w", "w", "w", "w", "w", "w", "w", "w", "w", "w",       "w", "w",       "w", "w",       "w",
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


                world.spawn(Morko.create(new Vector2(16.0f, 1.0f)));

                // borders
                world.spawn(Entity.builder()
                                  .component(new PhysicsBody(-1.0f, 1.0f, 1.0f, 2.0f)));
                world.spawn(Entity.builder()
                                  .component(new PhysicsBody(101.0f, 1.0f, 1.0f, 2.0f)));
            }
        };
    }
}
