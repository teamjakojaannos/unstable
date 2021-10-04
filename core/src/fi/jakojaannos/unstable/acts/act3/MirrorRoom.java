package fi.jakojaannos.unstable.acts.act3;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.acts.act5.Act5;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.entities.Poster;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.level.TileSet;

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
                world.spawn(Poster.create(
                        new Vector2(WIDTH - 6, 1.0f),
                        player,
                        Poster.Type.Indoordoor,
                        null,
                        (s, r) -> {
                            r.nextAct = new Act5();
                            return true;
                        }));
            }
        };
    }
}
