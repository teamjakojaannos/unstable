package fi.jakojaannos.unstable.acts.end;

import fi.jakojaannos.unstable.acts.Act;
import fi.jakojaannos.unstable.level.Room;

public class TheEnd implements Act {
    public static final Room THE_OFFICE = TheOffice.create(true);
    public static final Room THE_OFFICE_UNSPOOPED = TheOfficeUnspooped.create(false);

    @Override
    public Room defaultRoom() {
        return THE_OFFICE;
    }
}
