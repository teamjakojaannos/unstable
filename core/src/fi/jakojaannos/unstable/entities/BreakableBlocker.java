package fi.jakojaannos.unstable.entities;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.PhysicsBody;
import fi.jakojaannos.unstable.ecs.Component;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.renderer.TextRenderer;
import fi.jakojaannos.unstable.resources.Interactable;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.List;
import java.util.function.Function;

public class BreakableBlocker implements Component<BreakableBlocker> {
    public static boolean state = false;
    public Type type;
    public boolean broken;
    public boolean idestructibbelbe;

    public BreakableBlocker(Type type) {
        this.type = type;
        this.broken = false;
        this.idestructibbelbe = false;
    }

    public BreakableBlocker(Type type, boolean broken, boolean foobar) {
        this.type = type;
        this.broken = broken;
        this.idestructibbelbe = foobar;
    }

    @Override
    public BreakableBlocker cloneComponent() {
        return new BreakableBlocker(this.type, this.broken, this.idestructibbelbe);
    }

    public static Entity.Builder create(Vector2 position, Type type, Function<Resources.Inventory, Boolean> condition) {
        return Entity.builder()
                     .component(new BreakableBlocker(type, false, type == Type.Invisible))
                     .component(new PhysicsBody(position, 1.0f, 1.0f))
                     .component(new Interactable(new Interactable.Action() {
                         @Override
                         public boolean condition(Entity self, Resources resources) {
                             return condition.apply(resources.playerInventory);
                         }

                         @Override
                         public void onExecuteFailed(Entity self, Resources resources) {
                             if (!resources.isInteractOnCooldown() && !state) {
                                 resources.setDialogueText(List.of(
                                         List.of(new TextRenderer.TextOnScreen("I need to get past this somehow"))
                                 ));
                                 state = true;
                                 resources.setInteractCooldown();
                             }
                         }

                         @Override
                         public boolean execute(Entity s, Resources r) {
                             s.getComponent(BreakableBlocker.class).ifPresent(bb -> bb.broken = true);
                             s.removeComponent(Interactable.class);
                             return true;
                         }
                     }));
    }

    public enum Type {
        Vase,
        Invisible,
    }
}
