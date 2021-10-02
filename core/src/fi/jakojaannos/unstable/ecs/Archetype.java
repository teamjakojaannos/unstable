package fi.jakojaannos.unstable.ecs;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Archetype {
    private final Set<Entity> removeQueue = new HashSet<>();
    private final Set<Entity> modificationQueue = new HashSet<>();
    private final List<Entity> entityHandles = new ArrayList<>();

    @SuppressWarnings("rawtypes")
    private final Map<Class, List<Component>> storages;


    public Archetype(final Class<?>[] components) {
        this.storages = Arrays.stream(components)
                              .collect(Collectors.toUnmodifiableMap(clazz -> clazz,
                                                                    clazz -> new ArrayList<>()));
    }

    public boolean matches(final boolean strictEquals, final SystemInput.Component... components) {
        if (strictEquals && components.length != this.storages.size()) {
            return false;
        }

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
        add(entity, builder.components().toArray(Component[]::new));

        return entity;
    }

    @SuppressWarnings("rawtypes")
    void add(final Entity entity, final Component[] components) {
        this.entityHandles.add(entity);

        for (final var component : components) {
            this.storages.get(component.getClass())
                         .add(component.cloneComponent());
        }
    }

    public void reapEntities() {
        this.removeQueue.forEach(this.entityHandles::remove);
    }

    public void markAsRemoved(final Entity entity) {
        if (!this.entityHandles.contains(entity)) {
            System.err.println("Warning: entity handle not present in archetype!");
        }

        this.removeQueue.add(entity);
    }

    public void markAsModified(final Entity entity) {
        this.modificationQueue.add(entity);
    }

    public List<Entity> drainModifiedEntities() {
        final var result = List.copyOf(this.modificationQueue);
        this.modificationQueue.clear();
        return result;
    }

    @SuppressWarnings("rawtypes")
    public List<Component> drainComponents(final int entityIndex) {
        final var result = this.storages.values()
                                        .stream()
                                        .map(storage -> storage.remove(entityIndex))
                                        .toList();

        this.entityHandles.get(entityIndex).setIndex(-1);
        this.entityHandles.stream()
                          .filter(entity -> entity.index() >= entityIndex)
                          .forEach(entity -> entity.setIndex(entity.index() - 1));
        this.entityHandles.remove(entityIndex);
        return result;
    }

    public boolean hasComponent(final Class<?> clazz) {
        return this.storages.containsKey(clazz);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <C extends Component> Optional<C> getComponent(Class<C> clazz, int entityIndex) {
        return this.storages.containsKey(clazz)
                ? Optional.of((C) this.storages.get(clazz).get(entityIndex))
                : Optional.empty();
    }
}
