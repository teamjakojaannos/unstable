package fi.jakojaannos.unstable.level;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.resources.Resources;

public interface Room {
    TileMap createMap();

    Vector2 playerStartPosition();

    void spawnInitialEntities(EcsWorld world, Resources res, Entity player);

    int width();
}
