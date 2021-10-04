package fi.jakojaannos.unstable.acts.act2;

import fi.jakojaannos.unstable.acts.Act;
import fi.jakojaannos.unstable.level.Room;

public class Act2 implements Act {
    public static final Room MANOR_ENTRY = ManorEntranceRoom.create();
    public static final Room SMALL_BEDROOM = SmallBedroom.create();
    public static final Room MIRROR_ROOM = MirrorRoom.create();

    @Override
    public Room defaultRoom() {
        return MANOR_ENTRY;
    }
}
