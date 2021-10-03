package fi.jakojaannos.unstable.components;

import fi.jakojaannos.unstable.components.tasks.Task;
import fi.jakojaannos.unstable.ecs.Component;
import fi.jakojaannos.unstable.ecs.Entity;
import fi.jakojaannos.unstable.resources.Resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskList implements Component<TaskList> {

    private final List<Task> tasks;
    private final boolean looping;
    private int currentTaskIndex = 0;

    private boolean firstUpdate = true;

    public TaskList(List<Task> tasks, boolean looping) {
        this.tasks = new ArrayList<>(tasks);
        this.looping = looping;
    }

    public void update(Entity entity, Resources resources) {
        if (firstUpdate) {
            firstUpdate = false;
            currentTask().ifPresent(task -> task.doAction(entity, resources));
            return;
        }

        var done = currentTask()
                .map(task -> task.isCompleted(entity, resources))
                .orElse(false);

        if (done) {
            this.currentTaskIndex++;
        }

        currentTask().ifPresent(task -> task.doAction(entity, resources));
    }

    private Optional<Task> currentTask() {
        final var index = this.looping
                ? (currentTaskIndex % this.tasks.size())
                : currentTaskIndex;

        if (index >= this.tasks.size()) {
            return Optional.empty();
        }
        return Optional.of(this.tasks.get(index));
    }

    @Override
    public TaskList cloneComponent() {
        return new TaskList(this.tasks, this.looping);
    }
}
