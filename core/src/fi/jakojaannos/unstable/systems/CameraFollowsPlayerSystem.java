package fi.jakojaannos.unstable.systems;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class CameraFollowsPlayerSystem implements EcsSystem<CameraFollowsPlayerSystem.Input> {


    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        resources.playerPosition()
                .ifPresent(position -> {
                    final var newX = MathUtils.clamp(
                            position.x,
                            resources.camera.bounds.min.x,
                            resources.camera.bounds.max.x
                    );
                    resources.camera.setPosition(new Vector3(newX, position.y, 0.0f));
                });
    }

    public record Input(

    ) {
    }
}
