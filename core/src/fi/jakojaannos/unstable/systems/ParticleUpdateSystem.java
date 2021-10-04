package fi.jakojaannos.unstable.systems;

import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class ParticleUpdateSystem implements EcsSystem<ParticleUpdateSystem.Input> {
    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        resources.particles.list.removeIf(particle -> particle.hasDied(resources.timeManager.currentTick()));
    }

    public record Input() {
    }
}
