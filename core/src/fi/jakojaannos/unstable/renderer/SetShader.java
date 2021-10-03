package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.level.TileMap;
import fi.jakojaannos.unstable.resources.Resources;

public class SetShader implements EcsSystem<SetShader.Input>, AutoCloseable {
    private final SpriteBatch spriteBatch;
    private final ShaderProgram shader;

    public SetShader(SpriteBatch spriteBatch, String vertexSource, String fragmentSource) {
        this.spriteBatch = spriteBatch;
        this.shader = vertexSource == null || fragmentSource == null
                ? null
                : new ShaderProgram(vertexSource, fragmentSource);
    }

    @Override
    public void tick(
            SystemInput<Input> input,
            Resources resources
    ) {
        spriteBatch.setShader(this.shader);
        resources.activeShader = this.shader;
    }

    @Override
    public void close() {
        this.shader.dispose();
    }

    public record Input(
            TileMap tileMap
    ) {}
}
