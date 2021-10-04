package fi.jakojaannos.unstable.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import fi.jakojaannos.unstable.InputManager;
import fi.jakojaannos.unstable.UnstableGame;
import fi.jakojaannos.unstable.acts.Act;
import fi.jakojaannos.unstable.components.Tags;
import fi.jakojaannos.unstable.ecs.EcsWorld;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.level.Room;
import fi.jakojaannos.unstable.renderer.Camera;
import fi.jakojaannos.unstable.renderer.TextRenderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Resources {
    public final InputManager playerInput = new InputManager();
    public final Camera camera;
    public final BoundingBox worldBounds;
    public final TimeManager timeManager;
    public TimerHandle lightningTimer;
    public Timers timers;
    public Entities entities;
    public Players players = new Players();
    public InteractItems interactItems = new InteractItems();

    public PopUp popup;
    public ShaderProgram activeShader;
    public boolean spoopy;
    public Room nextRoom;
    public Act nextAct;
    public boolean stormy;
    public Vector2 spawnPos;
    public Entity player;
    public Attacks attacks = new Attacks();
    public Particles particles = new Particles();

    public Inventory playerInventory = new Inventory();
    public TimerHandle interactCooldown;
    public float blockerBound = Float.MAX_VALUE;
    public boolean combinationSolved;
    public int[] numbers = new int[3];
    public int enteringNumber = -1;
    public boolean inOffice;
    public TimerHandle fadeToBlack;
    public boolean endFadeToBlackStarted;
    public boolean endFadeToBlackStarted2;
    public boolean creditsAvailable;
    public boolean credits;

    private List<Collection<TextRenderer.TextOnScreen>> dialogueText;

    public Resources(
            final EcsWorld world,
            final BoundingBox worldBounds,
            final BoundingBox cameraBounds,
            final Timers timers,
            final TimeManager timeManager
    ) {
        entities = new Entities(world);
        camera = new Camera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cameraBounds);
        this.worldBounds = worldBounds;
        this.timers = timers;
        this.timeManager = timeManager;
    }

    public void reset(EcsWorld world) {
        this.popup = null;
        this.activeShader = null;
        this.spoopy = false;

        this.players = new Players();
        this.entities = new Entities(world);
        this.interactItems = new InteractItems();
    }

    public Optional<Vector2> playerPosition() {
        return this.players.getPlayerPosition();
    }

    public void resetPlayer() {
        final var componentsToRemove = List.of(
                Tags.FreezeInput.class
        );
        componentsToRemove.forEach(clazz -> this.player.removeComponent(clazz));
    }

    public List<Collection<TextRenderer.TextOnScreen>> getDialogueText() {
        return this.dialogueText;
    }

    public void setDialogueText(Collection<Collection<TextRenderer.TextOnScreen>> lists) {
        this.dialogueText = lists == null ? null : new ArrayList<>(lists);

        if (lists == null) {
            setInteractCooldown();
        }
    }

    public void setInteractCooldown() {
        timers.clear(interactCooldown);
        interactCooldown = timers.set(
                UnstableGame.Constants.INTERACT_COOLDOWN,
                false,
                () -> {}
        );
    }

    public boolean isInteractOnCooldown() {
        return timers.isActiveAndValid(interactCooldown);
    }

    public static class Inventory {
        public boolean photo;
        public boolean hammer;
    }
}
