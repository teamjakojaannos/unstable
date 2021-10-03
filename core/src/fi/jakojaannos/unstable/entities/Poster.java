package fi.jakojaannos.unstable.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.PlayerHudComponent;
import fi.jakojaannos.unstable.components.PosterState;
import fi.jakojaannos.unstable.components.Tags;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.resources.Interactable;
import fi.jakojaannos.unstable.resources.PopUp;

public class Poster {
    public static Entity.Builder create(Vector2 position, Entity player, Type type, PopUp popUp) {
        return create(position, player, type, popUp, null);
    }

    public static Entity.Builder create(
            Vector2 position,
            Entity player,
            Type type,
            PopUp popUp,
            Interactable.Action extraAction
    ) {
        final var builder = Entity
                .builder()
                .component(new PhysicsBody(position.cpy(), variantSize(type)))
                .component(new PosterState(type));

        if (popUp != null) {
            builder.component(new Interactable((self, resources) -> {
                       if (resources.popup == null) {
                           resources.popup = popUp;
                           self.getComponent(PosterState.class).ifPresent(state -> state.active = true);
                           player.addComponent(new Tags.FreezeInput());
                       } else {
                           resources.popup = null;
                           self.getComponent(PosterState.class).ifPresent(state -> state.active = false);
                           player.removeComponent(Tags.FreezeInput.class);

                           extraAction.execute(self, resources);
                       }
                   }))
                   .component(PlayerHudComponent.Indicator.QUESTION);
        } else if (extraAction != null) {
            builder.component(new Interactable(extraAction))
                   .component(PlayerHudComponent.Indicator.QUESTION);
        }

        return builder;
    }

    private static BoundingBox variantSize(Type type) {
        return switch (type) {
            case Sofa -> new BoundingBox(new Vector3(0.5f, 0, 0),
                                         new Vector3(3.5f, 1, 0));
            default -> new BoundingBox(new Vector3(0, 0, 0),
                                       new Vector3(1, 1, 0));
        };
    }

    // DO NOT REORDER: rendering relies on ordinals
    public enum Type {
        POSTER,
        PAINTING,
        WINDOW,
        CAFE_COUNTER,
        NEWSPAPER_ABOUT_TO_FALL,
        Indoordoor,
        Furnace,
        Sofa,
    }
}
