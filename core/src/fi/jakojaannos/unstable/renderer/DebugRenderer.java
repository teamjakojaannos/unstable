package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import fi.jakojaannos.unstable.UnstableGame;
import fi.jakojaannos.unstable.components.MorkoAi;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.Optional;

public class DebugRenderer implements EcsSystem<DebugRenderer.Input>, AutoCloseable {

    private final ShapeRenderer renderer;

    public DebugRenderer() {
        this.renderer = new ShapeRenderer();
    }

    @Override
    public void tick(SystemInput<Input> input, Resources resources) {
        if (!UnstableGame.Constants.Debug.DEBUG_RENDERER_ENABLED) return;

        input.entities().forEach(entity -> {
            final var body = entity.body;
            final var bounds = body.boundsPositionCorrected();
            final var min = bounds.min;
            final var width = bounds.getWidth();
            final var height = bounds.getHeight();

            renderer.setProjectionMatrix(resources.camera.getCombinedMatrix());

            renderer.begin(ShapeRenderer.ShapeType.Line);
            renderer.setColor(1.0f, 0.0f, 0.0f, 1.0f);
            renderer.rect(min.x, min.y, width, height);

            /*
            entity.morkoAi
                    .flatMap(MorkoAi::getTargetPos)
                    .ifPresent(target -> {
                        renderer.setColor(0.0f, 0.0f, 1.0f, 1.0f);
                        final var size = 1.0f;
                        renderer.rect(target.x - size / 2.0f, target.y - size / 2.0f, size, size);
                    });

            entity.morkoAi
                    .flatMap(MorkoAi::getAttackTargetPos)
                    .ifPresent(target -> {
                        renderer.setColor(0.0f, 1.0f, 0.0f, 1.0f);
                        final var size = 0.5f;
                        renderer.rect(target.x - size / 2.0f, target.y - size / 2.0f, size, size);
                    });
            */

            renderer.end();
        });
    }

    @Override
    public void close() {
        this.renderer.dispose();
    }

    public record Input(
            PhysicsBody body,
            Optional<MorkoAi> morkoAi
    ) {
    }
}
