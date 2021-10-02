package fi.jakojaannos.unstable.ecs;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface SystemInput<TInput> {
    Stream<TInput> entities();

    record Component(Class<?> constructorParamClass, Class<?> clazz, Type type) {
        public boolean isRequired() {
            return this.type == Type.Required;
        }

        public boolean isExcluded() {
            return this.type == Type.Excluded;
        }

        public boolean isOptional() {
            return this.type == Type.Optional;
        }

        enum Type {
            Required,
            Excluded,
            Optional,
        }
    }

    record Impl<TInput>(
            EcsWorld world,
            Class<TInput> clazz,
            Constructor<TInput> constructor,
            SystemInput.Component[] components
    ) implements SystemInput<TInput> {
        @Override
        public Stream<TInput> entities() {
            return this.world
                    .getMatchingArchetypes(this.components)
                    .flatMap(archetype -> {
                        final var storages = Arrays
                                .stream(components)
                                .map(component -> component.isExcluded()
                                        ? new Object[archetype.entityCount()] // HACK: returns entityCount() nulls
                                        : archetype.componentsOfType(component.clazz, component.isOptional()))
                                .toArray(Object[][]::new);

                        final var entityCount = archetype.entityCount();
                        return IntStream.range(0, entityCount)
                                        .mapToObj(index -> {
                                            final var constructorParams = Arrays
                                                    .stream(storages)
                                                    .map(storage -> storage[index])
                                                    .toArray();
                                            return invokeConstructor(constructorParams);
                                        });
                    });
        }

        private TInput invokeConstructor(Object[] constructorParams) {
            try {
                return this.constructor.newInstance(constructorParams);
            } catch (InstantiationException e) {
                throw new IllegalStateException(String.format("Invoking system input %s constructor threw an instantiation exception!", this.clazz.getSimpleName()));
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(String.format("System input %s constructor was not accessible", this.clazz.getSimpleName()));
            } catch (InvocationTargetException e) {
                throw new IllegalStateException(String.format("System input %s constructor threw an error: %s", this.clazz.getSimpleName(), e.getMessage()));
            }
        }
    }
}
