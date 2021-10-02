package fi.jakojaannos.unstable.ecs;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public interface EcsWorld {
    <TInput> SystemInput<TInput> createInput(
            Class<TInput> clazz,
            Constructor<TInput> constructor,
            SystemInput.Component[] components
    );

    Stream<Archetype> getMatchingArchetypes(SystemInput.Component... components);

    Entity spawn(Entity.Builder entity);

    class Impl implements EcsWorld {
        public List<Archetype> archetypes = new ArrayList<>();

        @Override
        public <TInput> SystemInput<TInput> createInput(
                final Class<TInput> clazz,
                final Constructor<TInput> constructor,
                final SystemInput.Component[] components
        ) {
            return new SystemInput.Impl<>(this, clazz, constructor, components);
        }

        @Override
        public Stream<Archetype> getMatchingArchetypes(final SystemInput.Component... components) {
            return this.archetypes.stream()
                                  .filter(archetype -> archetype.matches(components));
        }

        @Override
        public Entity spawn(final Entity.Builder entity) {
            return this.archetypes.stream()
                                  .filter(archetype -> archetype.matches(Arrays.stream(entity.componentClasses())
                                                                               .map(clazz -> new SystemInput.Component(clazz, clazz, SystemInput.Component.Type.Required))
                                                                               .toArray(SystemInput.Component[]::new))
                                  )
                                  .findAny()
                                  .orElseGet(() -> createArchetype(entity.componentClasses()))
                                  .spawn(entity);
        }

        private Archetype createArchetype(Class<?>[] components) {
            final var archetype = new Archetype(components);
            this.archetypes.add(archetype);

            return archetype;
        }
    }
}
