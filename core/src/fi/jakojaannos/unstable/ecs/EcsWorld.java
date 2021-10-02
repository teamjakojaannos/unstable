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

    void reapEntities();

    void spawnEntities();

    void commitComponentModifications();

    void queueSpawn(final Entity.Builder entity);

    class Impl implements EcsWorld {
        public List<Entity.Builder> spawnQueue = new ArrayList<>();

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
            return findOrCreateArchetype(entity.componentClasses())
                    .spawn(entity);
        }

        @Override
        public void reapEntities() {
            this.archetypes.forEach(Archetype::reapEntities);
        }

        @Override
        public void spawnEntities() {
            this.spawnQueue.forEach(this::spawn);
            this.spawnQueue.clear();
        }

        @Override
        public void commitComponentModifications() {
            this.archetypes
                    .stream()
                    .flatMap(archetype -> archetype.drainModifiedEntities().stream())
                    .forEach(entity -> {
                        final var removedComponents = entity.drainRemovedComponents();
                        final var components = entity.drainComponents()
                                                     .stream()
                                                     .filter(component -> !removedComponents.contains(component.getClass()));
                        final var componentClasses = components
                                .map(Object::getClass)
                                .toArray(Class[]::new);
                        final var newArchetype = findOrCreateArchetype(componentClasses);
                        entity.moveToArchetype(newArchetype, components.toArray(Component[]::new));
                    });
        }

        @Override
        public void queueSpawn(final Entity.Builder entity) {
            this.spawnQueue.add(entity);
        }

        private Archetype findOrCreateArchetype(Class<?>[] components) {
            return this.archetypes.stream()
                                  .filter(archetype -> archetype.matches(Arrays.stream(components)
                                                                               .map(clazz -> new SystemInput.Component(clazz, clazz,
                                                                                                                       SystemInput.Component.Type.Required))
                                                                               .toArray(SystemInput.Component[]::new)))
                                  .findAny()
                                  .orElseGet(() -> createArchetype(components));
        }

        private Archetype createArchetype(Class<?>[] components) {
            final var archetype = new Archetype(components);
            this.archetypes.add(archetype);

            return archetype;
        }
    }
}
