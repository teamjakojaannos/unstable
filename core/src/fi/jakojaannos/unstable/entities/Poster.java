package fi.jakojaannos.unstable.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import fi.jakojaannos.unstable.acts.Act;
import fi.jakojaannos.unstable.components.*;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.renderer.TextRenderer;
import fi.jakojaannos.unstable.resources.Interactable;
import fi.jakojaannos.unstable.resources.PopUp;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

public class Poster {
    public static Entity.Builder create(Vector2 position, Type type, PopUp popUp) {
        return create(position, type, popUp, null);
    }

    public static Entity.Builder create(
            Vector2 position,
            Type type,
            PopUp popUp,
            Interactable.Action extraAction
    ) {
        return create(position, type, popUp, extraAction, List.of());
    }

    public static Entity.Builder create(
            Vector2 position,
            Type type,
            PopUp popUp,
            Interactable.Action extraAction,
            List<Collection<TextRenderer.TextOnScreen>> preCloseDialogue
    ) {
        final var hack = popUp != null && popUp.lines().size() == 1 && popUp.lines().get(0).content() == null;

        final var builder = Entity
                .builder()
                .component(new PhysicsBody(position.cpy(), hack
                        ? new BoundingBox(new Vector3(0, 0, 0), new Vector3(2, 1, 0))
                        : variantSize(type)))
                .component(new PosterState(type));

        if (popUp != null) {
            builder.component(new Interactable((self, resources) -> {
                       if (hack && resources.combinationSolved) {
                           return false;
                       }

                       final var isOpen = self.getComponent(PosterState.class).map(s -> s.active).orElse(false);

                       if (resources.popup == null && !isOpen) {
                           resources.popup = popUp;
                           self.getComponent(PosterState.class).ifPresent(state -> state.active = true);
                           resources.player.addComponent(new Tags.FreezeInput());

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
                               resources.player.removeComponent(Tags.FreezeInput.class);

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
            case Sofa, PuzzlePaintingA, PuzzlePaintingB, PuzzlePaintingC -> new BoundingBox(new Vector3(1.0f, 0, 0),
                                                                                            new Vector3(2.5f, 1, 0));
            case Kaappi -> new BoundingBox(new Vector3(-2.5f, 0, 0),
                                           new Vector3(1.0f, 1, 0));
            case Indoordoor -> new BoundingBox(new Vector3(0, 0, 0),
                                               new Vector3(2, 1, 0));
            default -> new BoundingBox(new Vector3(0, 0, 0),
                                       new Vector3(1, 1, 0));
        };
    }

    public static Entity.Builder createDoor(
            Vector2 position,
            Room nextRoom,
            Act nextAct
    ) {
        return createDoor(position, nextRoom, nextAct, null, null);
    }

    public static Entity.Builder createDoor(
            Vector2 position,
            Room nextRoom,
            Act nextAct,
            Vector2 spawnPos,
            BiFunction<Entity, Resources, Boolean> condition
    ) {
        return createDoor(position, nextRoom, nextAct, spawnPos, condition, null);
    }

    public static Entity.Builder createDoor(
            Vector2 position,
            Room nextRoom,
            Act nextAct,
            Vector2 spawnPos,
            BiFunction<Entity, Resources, Boolean> condition,
            Interactable.Action extraAction
    ) {
        return Poster.create(position,
                             Type.Indoordoor,
                             null,
                             new Interactable.Action() {
                                 @Override
                                 public boolean condition(Entity self, Resources resources) {
                                     return condition != null ? condition.apply(self, resources) : true;
                                 }

                                 @Override
                                 public void onExecuteFailed(Entity self, Resources resources) {
                                     self.addComponent(new SoundTags.Locked());
                                 }

                                 @Override
                                 public boolean execute(Entity s, Resources r) {
                                     if (nextRoom != null) {
                                         r.nextRoom = nextRoom;
                                     }
                                     if (nextAct != null) {
                                         r.nextAct = nextAct;
                                     }
                                     if (spawnPos != null) {
                                         r.spawnPos = spawnPos;
                                     }
                                     if (extraAction != null) {
                                         extraAction.execute(s, r);
                                     }
                                     return true;
                                 }
                             });
    }

    public static Entity.Builder createSofa(
            Vector2 position
    ) {
        return Poster.create(
                position.cpy(),
                Poster.Type.Sofa,
                null,
                (s, r) -> {
                    if (r.player.hasComponent(Shitting.class)) {
                        r.player.removeComponent(Shitting.class);
                        r.player.removeComponent(Tags.FreezeInput.class);
                    } else {
                        final var handle = r.timers.set(1.0f, false, () -> {});
                        r.player.addComponent(new Shitting(handle));
                        r.player.addComponent(new Tags.FreezeInput());
                    }

                    return true;
                }
        );
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
        Hammer,
        Haarniska,
        KaappiNurin,
        Kaappi,
        PuzzlePaintingA,
        PuzzlePaintingB,
        PuzzlePaintingC,
        DOCTOR,
        StatueTable,
        MedicalReport,
        TABLE,
        NpcMIES,
        NpcMIES2,
        Lambu,
        NpcNAINE,
        NpcNAINE2,
        JalkaKynttila,
        SeinaKynttila,
        Pouta
    }
}
