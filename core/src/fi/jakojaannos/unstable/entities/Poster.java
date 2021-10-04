package fi.jakojaannos.unstable.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.components.PlayerHudComponent;
import fi.jakojaannos.unstable.components.PosterState;
import fi.jakojaannos.unstable.components.Tags;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.renderer.TextRenderer;
import fi.jakojaannos.unstable.resources.Interactable;
import fi.jakojaannos.unstable.resources.PopUp;

import java.util.Collection;
import java.util.List;

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
        return create(position, player, type, popUp, extraAction, List.of());
    }

    public static Entity.Builder create(
            Vector2 position,
            Entity player,
            Type type,
            PopUp popUp,
            Interactable.Action extraAction,
            List<Collection<TextRenderer.TextOnScreen>> preCloseDialogue
    ) {
        final var builder = Entity
                .builder()
                .component(new PhysicsBody(position.cpy(), variantSize(type)))
                .component(new PosterState(type));

        if (popUp != null) {
            builder.component(new Interactable((self, resources) -> {
                       System.out.println("HANDLER");
                       final var isOpen = self.getComponent(PosterState.class).map(s -> s.active).orElse(false);

                       if (resources.popup == null && !isOpen) {
                           resources.popup = popUp;
                           self.getComponent(PosterState.class).ifPresent(state -> state.active = true);
                           player.addComponent(new Tags.FreezeInput());

                           return true;
                       } else {
                           if (!preCloseDialogue.isEmpty() && resources.getDialogueText() == null && self.getComponent(PosterState.class).map(state -> !state.dialogueShown).orElse(false)) {
                               resources.setDialogueText(preCloseDialogue);
                               self.getComponent(PosterState.class).map(state -> state.dialogueShown = true);
                               return true;
                           }

                           if (preCloseDialogue.isEmpty() || resources.getDialogueText() == null || resources.getDialogueText().isEmpty()) {
                               resources.popup = null;
                               self.getComponent(PosterState.class).map(state -> state.dialogueShown = false);
                               self.getComponent(PosterState.class).ifPresent(state -> state.active = false);
                               player.removeComponent(Tags.FreezeInput.class);

                               if (extraAction != null) {
                                   extraAction.execute(self, resources);
                               }

                               return true;
                           }
                           return false;
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
        PhotoWhole,
        PhotoRipped,
    }
}
