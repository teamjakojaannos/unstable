package fi.jakojaannos.unstable.entities;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.Hiding;
import fi.jakojaannos.unstable.components.HidingSpot;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.resources.Interactable;

public class Closet {
    public static Entity.Builder create(Vector2 position, Entity player, HidingSpot.Type type) {
        return Entity.builder()
                     .component(new PhysicsBody(position.x, position.y, 1.5f, 3.0f))
                     .component(new Interactable((self, resources) -> {
                         final var timers = resources.timers;
                         final var hidingSpot = self.getComponent(HidingSpot.class).orElseThrow();
                         if (!player.hasComponent(Hiding.class)) {
                             final var body = player.getComponent(PhysicsBody.class).orElseThrow();
                             final var oldPos = body.getPosition();

                             player.addComponent(new Hiding(oldPos.cpy(), false));

                             timers.set(1.0f, false, () -> {
                                 player.getComponent(Hiding.class).ifPresent((hiding) -> hiding.canExit = true);
                             });

                             final var closetPosition = self.getComponent(PhysicsBody.class)
                                                            .orElseThrow()
                                                            .getPosition()
                                                            .cpy();
                             body.setPosition(closetPosition.add(hidingSpot.offset));
                             hidingSpot.occupied = true;
                         } else if (player.getComponent(Hiding.class).orElseThrow().canExit) {
                             player.getComponent(PhysicsBody.class)
                                   .orElseThrow()
                                   .setPosition(player.getComponent(Hiding.class)
                                                      .orElseThrow().previousPosition);
                             player.removeComponent(Hiding.class);
                             hidingSpot.occupied = false;
                         }
                     }))
                     .component(new HidingSpot(type));
    }
}
