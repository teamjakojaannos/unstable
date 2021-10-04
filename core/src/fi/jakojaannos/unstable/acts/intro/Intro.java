package fi.jakojaannos.unstable.acts.intro;

import fi.jakojaannos.unstable.level.Room;

public class Intro implements fi.jakojaannos.unstable.acts.Act {
    public static Room CAFE = CafeIntroRoom.create();

    @Override
    public Room defaultRoom() {
        return CAFE;
    }
}
