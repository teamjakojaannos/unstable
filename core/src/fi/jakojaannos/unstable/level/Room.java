package fi.jakojaannos.unstable.level;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.ecs.Entity;

public interface Room {
    TileMap createMap();

    Vector2 playerStartPosition();

    void spawnInitialEntities(EcsWorld world, Entity player);
}
