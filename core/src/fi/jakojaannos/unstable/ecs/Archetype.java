package fi.jakojaannos.unstable.ecs;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Archetype {
    @SuppressWarnings("rawtypes")
    private Map<Class, List<Object>> storages;
    private List<Entity> entityHandles;

    public Archetype(final Class<?>[] components) {
        this.storages = Arrays.stream(components)
                              .collect(Collectors.toUnmodifiableMap(clazz -> clazz,
                                                                    clazz -> new ArrayList<>()));
        this.entityHandles = new ArrayList<>();
    }

    public boolean matches(final SystemInput.Component... components) {
        return Arrays
                .stream(components)
                .filter(component -> !component.isOptional())
                // With the optional components filtered out, if the component is required, we must have the key
                // available. If not required, the component is excluded and must NOT be present.
                .allMatch(component -> component.isRequired() == this.storages.containsKey(component.clazz()));
    }

    public int entityCount() {
        return this.entityHandles.size();
    }

    public Object[] componentsOfType(final Class<?> clazz, final boolean isOptional) {
        if (!this.storages.containsKey(clazz)) {
            if (isOptional) {
                return IntStream.range(0, this.entityCount())
                                .mapToObj((ignored) -> Optional.empty())
                                .toArray();
            }

            throw new IllegalStateException(String.format("Component of type %s not present on archetype %s",
                                                          clazz.getSimpleName(),
                                                          Arrays.toString(this.storages.keySet().toArray())));
        }

        return isOptional
                ? this.storages.get(clazz).stream().map(Optional::of).toArray()
                : this.storages.get(clazz).toArray();
    }

    public Entity spawn(final Entity.Builder builder) {
        final var entityIndex = this.entityCount();
        final var entity = new Entity(this, entityIndex);
        this.entityHandles.add(entity);

        builder.components()
               .forEach(component -> this.storages.get(component.getClass()).add(component));

        return entity;
    }
}
