package fi.jakojaannos.unstable.acts.act2;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.Mirror;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.Trigger;
import fi.jakojaannos.unstable.components.tasks.MoveKys;
import fi.jakojaannos.unstable.components.tasks.TaskDestroySelf;
import fi.jakojaannos.unstable.components.tasks.TaskDestroySelfOnContactWithPlayer;
import fi.jakojaannos.unstable.components.tasks.TaskMove;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Nurse;
import fi.jakojaannos.unstable.entities.Poster;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.level.TileSet;

import java.util.List;

public class MirrorRoom {
    private static final int WIDTH = 24;
    private static final int HEIGHT = 9;
    private static final String[] TILES = new String[]{
            // @formatter:off
            "f",       "f","f",       "f","f",       "f","f",       "f","f",       "f", "f",       "f", "f",       "f", "f",       "f","f",       "f","f",       "f","f",       "f","f",       "f",
            "tapetti", "w","tapetti", "w","tapetti", "w","tapetti", "w","tapetti", "w", "tapetti", "w", "tapetti", "w", "tapetti", "w","tapetti", "w","tapetti", "w","tapetti", "w","tapetti", "w",
            "w",       "w","w",       "w","w",       "w","w",       "w","w",       "w", "w",       "w", "w",       "w", "w",       "w","w",       "w","w",       "w","w",       "w","w",       "w",
            "w",       "w","w",       "w","w",       "w","w",       "w","w",       "w", "w",       "w", "w",       "w", "w",       "w","w",       "w","w",       "w","w",       "w","w",       "w",
            "w",       "w","w",       "w","w",       "w","w",       "w","w",       "w", "w",       "w", "w",       "w", "w",       "w","w",       "w","w",       "w","w",       "w","w",       "w",
            "w",       "w","w",       "w","w",       "w","w",       "w","w",       "w", "w",       "w", "w",       "w", "w",       "w","w",       "w","w",       "w","w",       "w","w",       "w",
            "w",       "w","w",       "w","w",       "w","w",       "w","w",       "w", "w",       "w", "w",       "w", "w",       "w","w",       "w","w",       "w","w",       "w","w",       "w",
            "w",       "w","w",       "w","w",       "w","w",       "w","w",       "w", "w",       "w", "w",       "w", "w",       "w","w",       "w","w",       "w","w",       "w","w",       "w",
            "w",       "w","w",       "w","w",       "w","w",       "w","w",       "w", "w",       "w", "w",       "w", "w",       "w","w",       "w","w",       "w","w",       "w","w",       "w",
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
                return new Vector2(new Vector2(2.0f, 1.0f));
            }

            @Override
            public void spawnInitialEntities(EcsWorld world, Entity player) {
                world.spawn(Poster.createDoor(new Vector2(WIDTH - 6, 1.0f), Act2.PUZZLE_ROOM, null));

                final var mirrorX = 12.0f;
                final var mirrorY = 1.0f;
                world.spawn(Entity.builder()
                                  .component(new PhysicsBody(mirrorX, mirrorY, 2.0f, 8.0f))
                                  .component(new Mirror()));


                world.spawn(Entity.builder()
                                  .component(new PhysicsBody(12.0f, 1.0f, 2.0f, 1.0f))
                                  .component(new Trigger(0.0f, false, resources -> {
                                      // lightning strike
                                      // TODO:
                                      // spawn nurse
                                      resources.entities.spawn(Nurse.createWithTasks(new Vector2(1.0f, 1.0f), true, List.of(
                                              new MoveKys<>(new Vector2(24.0f, 1.0f), 1.0f, false),
                                              new TaskDestroySelf<>(false)
                                      )));
                                  })));
            }
        };
    }
}
