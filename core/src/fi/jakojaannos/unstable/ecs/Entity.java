package fi.jakojaannos.unstable.ecs;

import java.util.*;
import java.util.stream.Stream;

public class Entity implements Component<Entity> {
    @SuppressWarnings("rawtypes")
    private final List<Component> newComponents = new ArrayList<>();
    @SuppressWarnings("rawtypes")
    private final List<Class> removedComponents = new ArrayList<>();

    private Archetype archetype;
    private int entityIndex;

    public Entity(final Archetype archetype, final int entityIndex) {
        this.archetype = archetype;
        this.entityIndex = entityIndex;
    }

    @Override
    public String toString() {
        return String.format("Entity { entityIndex: %d, archetype: %s }", entityIndex, archetype);
    }

    public void destroy() {
        this.archetype.markAsRemoved(this);
    }

    public <C extends Component<C>> void addComponent(final C component) {
        if (this.archetype.hasComponent(component.getClass())) {
            //throw new IllegalStateException(String.format("The entity already has the component %s", component.getClass().getSimpleName()));
            return;
        }

        this.newComponents.add(component);
        this.archetype.markAsModified(this);
    }

    public <C extends Component<C>> void removeComponent(final Class<C> clazz) {
        this.removedComponents.add(clazz);
        this.archetype.markAsModified(this);
    }

    @SuppressWarnings("rawtypes")
    List<Component> drainComponents() {
        final var result = new ArrayList<>(this.archetype.drainComponents(this.entityIndex));

        result.addAll(this.newComponents);
        this.newComponents.clear();
        return result;
    }

    @SuppressWarnings("rawtypes")
    List<Class> drainRemovedComponents() {
        final var result = List.copyOf(this.removedComponents);
        this.removedComponents.clear();
        return result;
    }

    void moveToArchetype(
            final Archetype archetype,
            @SuppressWarnings("rawtypes") final Component[] components
    ) {
        this.archetype = archetype;
        this.entityIndex = archetype.entityCount();
        this.archetype.add(this, components);
    }

    int index() {
        return this.entityIndex;
    }

    void setIndex(final int index) {
        this.entityIndex = index;
    }

    public boolean hasComponent(Class<?> clazz) {
        return this.archetype.hasComponent(clazz);
    }

    @SuppressWarnings("rawtypes")
    public <C extends Component> Optional<C> getComponent(Class<C> clazz) {
        return this.archetype.getComponent(clazz, this.entityIndex);
    }

    @Override
    public Entity cloneComponent() {
        return this;
    }

    public Class<Component>[] componentClasses() {
        return (Class<Component>[]) this.archetype.componentClasses();
    }

    public static Entity.Builder builder() {
        return new Builder();
    }

    @SuppressWarnings("rawtypes")
    public static class Builder {
        @SuppressWarnings("rawtypes")
        private final Map<Class, Component> components = new HashMap<>();

        public <C extends Component> Builder component(final C component) {
            this.components.put(component.getClass(), component);
            return this;
        }

        public Class<?>[] componentClasses() {
            return Stream.concat(this.components.keySet().stream(), Stream.of(Entity.class)).toArray(Class[]::new);
        }

        public Stream<Component> components() {
            return this.components.values().stream();
        }
    }
}
