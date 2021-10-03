package fi.jakojaannos.unstable.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.ecs.SystemInput;
import fi.jakojaannos.unstable.resources.Resources;

public class RenderAmbience implements EcsSystem<RenderAmbience.Input>, AutoCloseable {
    private final Music ambience;

    public RenderAmbience() {
        this.ambience = Gdx.audio.newMusic(Gdx.files.internal("Ambience1.ogg"));
        this.ambience.setLooping(true);
        this.ambience.play();
        this.ambience.setVolume(0.1f);
    }

    @Override
    public void tick(
            SystemInput<Input> input,
            Resources resources
    ) {
    }

    @Override
    public void close() {
        this.ambience.dispose();
    }


    public record Input() {}
}
