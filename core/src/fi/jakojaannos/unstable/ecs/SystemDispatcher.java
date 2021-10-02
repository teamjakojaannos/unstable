package fi.jakojaannos.unstable.ecs;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface SystemDispatcher {
    void tick(final EcsWorld world);

    class Impl implements SystemDispatcher {
        @SuppressWarnings("rawtypes") private final List<SystemDispatchInfo> systems;

        @SuppressWarnings({"rawtypes", "unchecked"})
        public Impl(List<EcsSystem> systems) {
            this.systems = systems.stream().map(this::reflectInfoFor).toList();
        }

        @Override
        @SuppressWarnings("unchecked")
        public void tick(EcsWorld world) {
            for (final var system : this.systems) {
                final var input = system.constructInput(world);
                system.system().tick(input);
            }
        }

        @SuppressWarnings("unchecked")
        private <TInput> SystemDispatchInfo<TInput> reflectInfoFor(final EcsSystem<TInput> system) {
            final var ecsSystemType = Arrays.stream(system.getClass().getGenericInterfaces())
                                            .filter((type -> type instanceof ParameterizedType))
                                            .map(ParameterizedType.class::cast)
                                            .filter(this::typeImplementsEcsSystem)
                                            .findFirst()
                                            .orElseThrow();

            final var typeParams = Arrays.stream(ecsSystemType.getActualTypeArguments())
                                         .filter((type -> type instanceof Class))
                                         .map(Class.class::cast)
                                         .toList();
            final var inputType = typeParams.get(0);
            if (!inputType.isRecord()) {
                throw new IllegalArgumentException("System input type must be a record!");
            }

            final var componentTypes = inputType.getRecordComponents();

            final var componentInputs = Arrays
                    .stream(componentTypes)
                    .map(type -> {
                        final var clazz = type.getType();
                        if (Without.class.isAssignableFrom(clazz)
                                && type.getGenericType() instanceof ParameterizedType parameterizedType
                                && parameterizedType.getActualTypeArguments()[0] instanceof Class<?> withoutClazz
                        ) {
                            return new SystemInput.Component(clazz, withoutClazz, SystemInput.Component.Type.Excluded);
                        } else if (Optional.class.isAssignableFrom(clazz)
                                && type.getGenericType() instanceof ParameterizedType parameterizedType
                                && parameterizedType.getActualTypeArguments()[0] instanceof Class<?> optionalClazz
                        ) {
                            return new SystemInput.Component(clazz, optionalClazz, SystemInput.Component.Type.Optional);
                        } else {
                            return new SystemInput.Component(clazz, clazz, SystemInput.Component.Type.Required);
                        }
                    })
                    .toArray(SystemInput.Component[]::new);

            final var constructorParamTypes = Arrays
                    .stream(componentInputs)
                    .map(SystemInput.Component::constructorParamClass)
                    .toArray(Class[]::new);
            Constructor<TInput> constructor;
            try {
                constructor = (Constructor<TInput>) (inputType.getConstructor(constructorParamTypes));
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(String.format("Could not find canonical constructor for %s", inputType.getSimpleName()));
            }

            return new SystemDispatchInfo<>(
                    system,
                    constructor,
                    (world) -> world.createInput((Class<TInput>) inputType, constructor, componentInputs)
            );
        }

        private boolean typeImplementsEcsSystem(ParameterizedType type) {
            if (type.getRawType() instanceof Class<?> clazz) {
                return EcsSystem.class.isAssignableFrom(clazz);
            }

            return false;
        }

        record SystemDispatchInfo<TInput>(
                EcsSystem<TInput> system,
                Constructor<TInput> inputConstructor,
                Function<EcsWorld, SystemInput<TInput>> inputFactory
        ) {
            public SystemInput<TInput> constructInput(final EcsWorld world) {
                return this.inputFactory.apply(world);
            }
        }
    }
}
