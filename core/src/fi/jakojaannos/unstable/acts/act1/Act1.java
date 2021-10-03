package fi.jakojaannos.unstable.acts.act1;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.renderer.*;
import fi.jakojaannos.unstable.systems.*;

import java.util.Collection;
import java.util.List;

import static fi.jakojaannos.unstable.Shaders.gradientFragmentShader;
import static fi.jakojaannos.unstable.Shaders.gradientVertexShader;

public class Act1 implements fi.jakojaannos.unstable.acts.Act {
    public static Room CAFE = CafeIntroRoom.create();

    @Override
    public Room defaultRoom() {
        return CAFE;
    }

    @Override
    public Collection<EcsSystem> systems() {
        return List.of(
                new PlayerInputSystem(),
                new MoveCharacterSystem(),
                new PlayerLocatorSystem(),
                new CameraFollowsPlayerSystem(),
                new CollectInteractablesSystem(),
                new PlayerActionSystem()
        );
    }

    @Override
    public Collection<EcsSystem> renderSystems(final SpriteBatch batch) {
        return List.of(
                new SetShader(batch, gradientVertexShader, gradientFragmentShader),
                new SetCafeUniforms(),
                new RenderTiles(batch),
                new RenderHidingSpot(batch),
                new RenderPosters(batch),
                new RenderPlayer(batch),
                new RenderMorko(batch),
                new SetShader(batch, null, null),
                new DebugRenderer(),
                new TextRenderer(batch)
        );
    }
}
