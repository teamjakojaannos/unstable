package fi.jakojaannos.unstable.resources;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Players {

    public final List<Entity> playerList = new ArrayList<>();

    public Optional<Vector2> getPlayerPosition() {
        if (playerList.size() == 0) {
            return Optional.empty();
        }

        return playerList.get(0)
                .getComponent(PhysicsBody.class)
                .map(PhysicsBody::getPosition);
    }

}
