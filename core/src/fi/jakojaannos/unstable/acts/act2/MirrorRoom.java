package fi.jakojaannos.unstable.acts.act2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.acts.act3.Act3;
import fi.jakojaannos.unstable.components.Mirror;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.Trigger;
import fi.jakojaannos.unstable.components.tasks.MoveKys;
import fi.jakojaannos.unstable.components.tasks.TaskDestroySelf;
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

    public static Room create(boolean spoopy) {
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
            public void spawnInitialEntities(
                    EcsWorld world,
                    Resources res,
                    Entity player
            ) {
                world.spawn(Poster.createDoor(new Vector2(WIDTH - 6, 1.0f),
                                              Act2.PUZZLE_ROOM,
                                              null,
                                              null,
                                              (s, r) -> !r.combinationSolved));
                world.spawn(Poster.createDoor(new Vector2(2, 1.0f),
                                              Act3.LONG_HALLWAY,
                                              null,
                                              null,
                                              (s, r) -> r.combinationSolved));

                final var mirrorX = 12.0f;
                final var mirrorY = 1.0f;
                world.spawn(Entity.builder()
                                  .component(new PhysicsBody(mirrorX, mirrorY, 2.0f, 8.0f))
                                  .component(new Mirror(spoopy)));

                if (spoopy) {
                    Gdx.audio.newSound(Gdx.files.internal("ComingOut.ogg")).play(0.5f, 1.0f, 0.0f);
                }

                if (!spoopy) {
                    world.spawn(Poster.create(
                            new Vector2(8.0f, 1.5f),
                            Poster.Type.POSTER,
                            new PopUp(List.of(), PopUp.Background.Note5),
                            (s, r) -> true,
                            List.of(
                                    List.of(new TextRenderer.TextOnScreen("Oh these are ramblings of a madman"),
                                            new TextRenderer.TextOnScreen("it seems.")),
                                    List.of(new TextRenderer.TextOnScreen("...wait! The names match my missing"),
                                            new TextRenderer.TextOnScreen("friends!")),
                                    List.of(new TextRenderer.TextOnScreen("I should leave and call the police"))
                            )));

                    world.spawn(Entity.builder()
                                      .component(new PhysicsBody(12.0f, 1.0f, 2.0f, 1.0f))
                                      .component(new Trigger(0.0f, false, resources -> {
                                          // lightning strike
                                          resources.stormy = true;
                                          // spawn nurse
                                          resources.entities.spawn(Nurse.createWithTasks(new Vector2(1.0f, 1.0f), true, List.of(
                                                  new MoveKys<>(new Vector2(24.0f, 1.0f), 1.0f, false),
                                                  new TaskDestroySelf<>(false)
                                          )));
                                      })));
                }
            }
        };
    }
}
