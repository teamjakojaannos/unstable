package fi.jakojaannos.unstable.ecs;

import fi.jakojaannos.unstable.resources.Resources;

public interface EcsSystem<TInput> {
    void tick(final SystemInput<TInput> input, Resources resources);
}
