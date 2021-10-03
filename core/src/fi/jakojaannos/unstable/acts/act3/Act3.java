package fi.jakojaannos.unstable.acts.act3;

import fi.jakojaannos.unstable.acts.Act;
import fi.jakojaannos.unstable.level.Room;

public class Act3 implements Act {
    public static Room MIRROR_ROOM = MirrorRoom.create();

    @Override
    public Room defaultRoom() {
        return MIRROR_ROOM;
    }
}
