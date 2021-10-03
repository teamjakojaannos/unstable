package fi.jakojaannos.unstable.acts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;
import fi.jakojaannos.unstable.resources.TimerHandle;

import java.util.Random;

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
    private TimerHandle lightningTimer;
    private boolean boom;
    private float intensity;

    public SetCafeUniforms() {
        lightning = Gdx.audio.newSound(Gdx.files.internal("kerho_ukkonen.ogg"));
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

        if (!resources.timers.isActiveAndValid(lightningTimer)) {
            final var lightningDelayMin = 6.0f;
            final var lightningDelayMax = 12.0f;
            final var lightningTime = this.random.nextFloat(lightningDelayMin, lightningDelayMax);

            this.lightningTimer = resources.timers.set(lightningTime, true, () -> {
                final var volume = this.random.nextFloat(0.25f, 1.0f);
                final var pitch = this.random.nextFloat(0.25f, 1.0f);
                lightning.play(volume, pitch, 0.0f);

                setLightingTimer(resources, 0);
            });
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
