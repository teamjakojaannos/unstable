package fi.jakojaannos.unstable.entities;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.PlayerHudComponent;
import fi.jakojaannos.unstable.components.PosterState;
import fi.jakojaannos.unstable.components.Tags;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.resources.Interactable;
import fi.jakojaannos.unstable.resources.PopUp;

public class Poster {
    public static Entity.Builder create(Vector2 position, Entity player, PopUp popUp) {
        return Entity.builder()
                     .component(new PhysicsBody(position.x, position.y, 1.0f, 1.0f))
                     .component(new Interactable((self, resources) -> {
                         if (resources.popup == null) {
                             resources.popup = popUp;
                             self.getComponent(PosterState.class).ifPresent(state -> state.active = true);
                             player.addComponent(new Tags.FreezeInput());
                         } else {
                             resources.popup = null;
                             self.getComponent(PosterState.class).ifPresent(state -> state.active = false);
                             player.removeComponent(Tags.FreezeInput.class);
                         }
                     }))
                     .component(new PosterState(Type.POSTER))
                     .component(PlayerHudComponent.Indicator.QUESTION);
    }

    public enum Type {
        POSTER
    }
}
