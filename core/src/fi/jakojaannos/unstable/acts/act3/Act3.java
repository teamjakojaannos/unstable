package fi.jakojaannos.unstable.acts.act3;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fi.jakojaannos.unstable.acts.Act;
import fi.jakojaannos.unstable.ecs.EcsSystem;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.renderer.*;

import java.util.Collection;
import java.util.List;

public class Act3 implements Act {
    public static final Room CIGAR_ROOM = CigarRoom.create();

    public static final Room LONG_HALLWAY = null;
    public static final Room WAITING_ROOM = null;

    @Override
    public Room defaultRoom() {
        return CIGAR_ROOM;
    }

    @Override
    public Collection<EcsSystem> renderSystems(SpriteBatch batch) {
        return List.of(
                new SetCafeUniforms(),
                new RenderTiles(batch),
                new RenderHidingSpot(batch),
                new RenderPosters(batch),
                new RenderPlayer(batch),
                new RenderMorko(batch),
                new TextRenderer(batch),
                new DialogueRenderer(batch),
                new RenderSoundTags(),
                new RenderAmbience()
        );
    }
}
