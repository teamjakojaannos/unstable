package fi.jakojaannos.unstable.resources;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Null;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.Entity;

import java.util.Optional;

public class Players {

    @Null
    private Entity player = null;

    public Optional<Entity> getPlayer() {
        return Optional.ofNullable(this.player);
    }

    /**
     *
     * @param player Player entity. Can be null if player is not present
     */
    public void setPlayer(Entity player) {
        this.player = player;
    }

    public Optional<Vector2> getPlayerPosition() {
        if (player == null) {
            return Optional.empty();
        }

        return player
                .getComponent(PhysicsBody.class)
                .map(PhysicsBody::getPosition);
    }

}
