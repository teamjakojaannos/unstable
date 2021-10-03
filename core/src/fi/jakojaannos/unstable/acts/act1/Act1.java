package fi.jakojaannos.unstable.acts.act1;

import fi.jakojaannos.unstable.level.Room;

public class Act1 implements fi.jakojaannos.unstable.acts.Act {
    public static Room CAFE = CafeIntroRoom.create();

    @Override
    public Room defaultRoom() {
        return CAFE;
    }
}
