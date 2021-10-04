package fi.jakojaannos.unstable.acts.act2;

import fi.jakojaannos.unstable.acts.Act;
import fi.jakojaannos.unstable.level.Room;

public class Act2 implements Act {
    public static final Room MIRROR_ROOM = MirrorRoom.create();

    public static final Room PUZZLE_ROOM = PaintingRoom.create();

    public static final Room MIRROR_ROOM_SPOOPY = MirrorRoom.create();

    public static final Room CHASE_ROOM_C = null;

    @Override
    public Room defaultRoom() {
        return MIRROR_ROOM;
    }
}
