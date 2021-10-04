package fi.jakojaannos.unstable.acts.act1;

import fi.jakojaannos.unstable.acts.Act;
import fi.jakojaannos.unstable.level.Room;

public class Act1 implements Act {
    public static final Room MANOR_ENTRY = ManorEntranceRoom.create();
    public static final Room SOME_ROOM = SomeRoom.create();

    public static final Room NURSE_HALLWAY = ManorNurseHallwayRoom.create();
    public static final Room HAMMER_ROOM = HammerRoom.create();

    public static final Room SOME_ROOM_2 = SomeRoom2.create();
    public static final Room SMALL_BEDROOM = SmallBedroom.create();

    @Override
    public Room defaultRoom() {
        return MANOR_ENTRY;
    }
}
