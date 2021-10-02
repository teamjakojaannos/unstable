package fi.jakojaannos.unstable.ecs;

public interface EcsSystem<TInput> {
    void tick(final SystemInput<TInput> input);
}
