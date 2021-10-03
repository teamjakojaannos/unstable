package fi.jakojaannos.unstable.acts.act2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fi.jakojaannos.unstable.acts.Act;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.renderer.*;
import fi.jakojaannos.unstable.systems.*;

import java.util.Collection;
import java.util.List;

public class Act2 implements Act {
    public static final Room MANOR_ENTRY = ManorEntranceRoom.create();

    @Override
    public Room defaultRoom() {
        return MANOR_ENTRY;
    }

    @Override
    public Collection<EcsSystem> systems() {
        return List.of(
                new PlayerInputSystem(),
                new MorkoInputSystem(),
                new MoveCharacterSystem(),
                new PlayerLocatorSystem(),
                new CameraFollowsPlayerSystem(),
                new CollectInteractablesSystem(),
                new PlayerActionSystem()
        );
    }

    @Override
    public Collection<EcsSystem> renderSystems(SpriteBatch batch) {
        return List.of(
                new RenderTiles(batch),
                new RenderHidingSpot(batch),
                new RenderPlayer(batch),
                new RenderMorko(batch),
                new DebugRenderer(),
                new TextRenderer(batch),
                new RenderAmbience()
        );
    }
}
