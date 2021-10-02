package fi.jakojaannos.unstable;

import fi.jakojaannos.unstable.ecs.EcsWorld;

public record GameState(
        EcsWorld world
) {
    public GameState() {
        this(new EcsWorld.Impl());
    }
}
