package fi.jakojaannos.unstable.resources;

import com.badlogic.gdx.math.Vector2;
import fi.jakojaannos.unstable.components.AttackTargetComponent;
import fi.jakojaannos.unstable.ecs.Entity;

import java.util.ArrayList;
import java.util.List;

public class Attacks {
    public final List<AttackTarget> attackedTargets = new ArrayList<>();
    public final List<AttackTarget> availableTargets = new ArrayList<>();

    public static record AttackTarget(
            Entity self,
            Vector2 origin,
            AttackTargetComponent target
    ) {
    }
}
