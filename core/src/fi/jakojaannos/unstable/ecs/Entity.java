package fi.jakojaannos.unstable.ecs;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Entity {
    private Archetype archetype;
    private int entityIndex;

    public Entity(final Archetype archetype, final int entityIndex) {
        this.archetype = archetype;
        this.entityIndex = entityIndex;
    }

    public static Entity.Builder builder() {
        return new Builder();
    }

    public static class Builder {
        @SuppressWarnings("rawtypes")
        private final Map<Class, Object> components = new HashMap<>();

        public <C> Builder component(final C component) {
            this.components.put(component.getClass(), component);
            return this;
        }

        public Class<?>[] componentClasses() {
            return this.components.keySet().toArray(new Class[0]);
        }

        public Stream<Object> components() {
            return this.components.values().stream();
        }
    }
}
