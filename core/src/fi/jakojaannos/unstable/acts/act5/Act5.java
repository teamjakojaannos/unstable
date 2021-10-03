package fi.jakojaannos.unstable.acts.act5;

import fi.jakojaannos.unstable.acts.Act;
import fi.jakojaannos.unstable.level.Room;

public class Act5 implements Act {
    public static final Room CIGAR_ROOM = CigarRoom.create();

    @Override
    public Room defaultRoom() {
        return CIGAR_ROOM;
    }
}
