package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;
import fi.jakojaannos.unstable.resources.TimerHandle;

import java.util.Random;

import static com.badlogic.gdx.Gdx.gl;

public class SetCafeUniforms implements EcsSystem<SetCafeUniforms.Input> {
    private static final LightningState[] LIGHTNING_SEQUENCE = new LightningState[]{
            new LightningState(true, 0.0f, 1.0f),
            new LightningState(false, 0.0f, 0.15f),
            new LightningState(true, 0.25f, 0.5f),
            new LightningState(false, 0.25f, 1.0f),
            new LightningState(true, 0.0f, 1.0f),
    };
    private final Random random = new Random();
    private final Sound lightning;
    private final Texture bgTexture;
    private final Texture bgTexture2;
    private final Texture bgTexture3;
    private TimerHandle lightningTimer;
    private boolean boom;
    private float intensity;

    public SetCafeUniforms() {
        lightning = Gdx.audio.newSound(Gdx.files.internal("kerho_ukkonen.ogg"));
        bgTexture = new Texture("läheiset kukkulat.png");
        bgTexture2 = new Texture("kaukaiset kukkulat.png");
        bgTexture3 = new Texture("taivas.png");
        bgTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.MirroredRepeat);
        bgTexture2.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.MirroredRepeat);
    }

    @Override
    public void tick(
            SystemInput<Input> input,
            Resources resources
    ) {
        final var shader = resources.activeShader;
        if (shader == null) {
            return;
        }
        shader.bind();

        final var bgSamplerLocation = shader.getUniformLocation("u_bg_texture");
        final var bgSamplerLocation2 = shader.getUniformLocation("u_bg_texture2");
        final var bgSamplerLocation3 = shader.getUniformLocation("u_bg_texture3");
        shader.setUniformi(bgSamplerLocation, 1);
        shader.setUniformi(bgSamplerLocation2, 2);
        shader.setUniformi(bgSamplerLocation3, 3);

        final var screenWidth = resources.camera.getWidth();
        final var screenHeight = resources.camera.getHeight();
        shader.setUniform2fv("u_screenSize", new float[]{screenWidth, screenHeight}, 0, 2);
        shader.setUniform2fv("u_bgSize", new float[]{bgTexture.getWidth(), bgTexture.getHeight()}, 0, 2);

        final var playerX = resources.camera.getPosition().x;
        final var playerY = resources.camera.getPosition().y;
        shader.setUniform2fv("u_playerPos", new float[]{playerX, playerY}, 0, 2);

        shader.setUniformi("u_debug_bg", 0);

        gl.glActiveTexture(GL20.GL_TEXTURE0);
        bgTexture.bind(1);
        bgTexture2.bind(2);
        bgTexture3.bind(3);
        gl.glActiveTexture(GL20.GL_TEXTURE0);

        if (resources.stormy && !resources.timers.isActiveAndValid(lightningTimer)) {
            final var lightningDelayMin = 12.0f;
            final var lightningDelayMax = 24.0f;
            final var lightningTime = this.random.nextFloat(lightningDelayMin, lightningDelayMax);

            this.lightningTimer = resources.timers.set(lightningTime, true, () -> {
                final var volume = this.random.nextFloat(0.125f, 0.5f);
                final var pitch = this.random.nextFloat(0.25f, 1.0f);
                lightning.play(volume, pitch, 0.0f);

                setLightingTimer(resources, 0);
            });
        } else {
            if (!resources.stormy) {
                resources.timers.clear(this.lightningTimer);
            }
        }

        final var litColor = new float[]{1.0f, 1.0f, 1.0f, this.intensity}; // 🔥
        final var unlitColor = new float[]{0.0f, 0.0f, 0.0f, 0.0f}; // 🚫

        shader.setUniform4fv("u_overlay_color", boom ? litColor : unlitColor, 0, 4);
    }

    private void setLightingTimer(Resources resources, int i) {
        if (i >= LIGHTNING_SEQUENCE.length) {
            this.boom = false;
            resources.spoopy = false;
            return;
        }

        final var state = LIGHTNING_SEQUENCE[i];
        this.boom = state.state;
        resources.spoopy = random.nextFloat() < state.spoopiness;
        this.intensity = random.nextFloat(0.25f, 0.95f);

        final var duration = this.random.nextFloat(state.duration);
        resources.timers.set(duration, false, () -> setLightingTimer(resources, i + 1));
    }

    public record Input() {}

    private static record LightningState(boolean state, float spoopiness, float duration) {}
}
