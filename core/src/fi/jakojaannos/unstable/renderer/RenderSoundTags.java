package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import fi.jakojaannos.unstable.components.SoundTags;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

public class RenderSoundTags implements EcsSystem<RenderSoundTags.Input>, AutoCloseable {
    private final Sound locked;
    private final Random random = new Random();

    public RenderSoundTags() {
        this.locked = Gdx.audio.newSound(Gdx.files.internal("PaperTurnPage.ogg"));
    }

    @Override
    public void tick(
            SystemInput<Input> input,
            Resources resources
    ) {
        input.entities()
             .forEach(entity -> {
                 entity.locked.ifPresent(playSound(entity, SoundTags.Locked.class));
             });
    }

    private Consumer<SoundTags.Locked> playSound(Input entity, Class<SoundTags.Locked> tag) {
        return playSound(entity, tag, 0.5f, 0.65f, 0.95f, 1.05f);
    }

    private Consumer<SoundTags.Locked> playSound(
            Input entity,
            Class<SoundTags.Locked> tag,
            float volumeMin,
            float volumeMax,
            float pitchMin,
            float pitchMax
    ) {
        return ignored -> {
            final var volume = this.random.nextFloat(volumeMin, volumeMax);
            final var pitch = this.random.nextFloat(pitchMin, pitchMax);

            entity.handle.removeComponent(tag);
            this.locked.play(volume, pitch, 0.0f);
        };
    }

    @Override
    public void close() {
        this.locked.dispose();
    }

    public record Input(
            Entity handle,
            Optional<SoundTags.Locked> locked
    ) {}
}
