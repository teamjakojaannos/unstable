package fi.jakojaannos.unstable.resources;

import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.ecs.Entity;

public class Entities {
    private final EcsWorld world;

    public Entities(final EcsWorld world) {
        this.world = world;
    }

    public void spawn(final Entity.Builder entity) {
        this.world.queueSpawn(entity);
    }
}
