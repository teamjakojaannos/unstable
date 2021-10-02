package fi.jakojaannos.unstable.entities;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.Hiding;
import fi.jakojaannos.unstable.components.HidingSpot;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.resources.Interactable;

public class Closet {
    public static Entity.Builder create(Vector2 position, Entity player, HidingSpot.Type type) {
        final var hidingSpot = new HidingSpot(type);
        return Entity.builder()
                     .component(new PhysicsBody(position.x, position.y, 2.5f, 3.0f))
                     .component(new Interactable(() -> {
                         if (!player.hasComponent(Hiding.class)) {
                             final var body = player.getComponent(PhysicsBody.class).orElseThrow();
                             final var oldPos = body.getPosition();
                             player.addComponent(new Hiding(oldPos.cpy()));
                             body.setPosition(hidingSpot.offset);
                             hidingSpot.occupied = true;
                         } else {
                             player.getComponent(PhysicsBody.class)
                                   .orElseThrow()
                                   .setPosition(player.getComponent(Hiding.class)
                                                      .orElseThrow().previousPosition);
                             player.removeComponent(Hiding.class);
                             hidingSpot.occupied = false;
                         }
                     }))
                     .component(hidingSpot);
    }
}
