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
        final var optTask = currentTask();
        if (optTask.isEmpty()) {
            return;
        }

        final var task = optTask.get();

        if (firstUpdate) {
            firstUpdate = false;
            task.initialize(entity, resources);
        }

        task.doAction(entity, resources);
        if (task.isCompleted(entity, resources)) {
            this.currentTaskIndex++;
            currentTask().ifPresent(newTask -> newTask.initialize(entity, resources));
        }
    }

    public Optional<Task> currentTask() {
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

    @Override
    public String toString() {
        return String.format(
                "TaskList: tasks=%d, index=%d, first=%b",
                tasks.size(),
                currentTaskIndex,
                firstUpdate
        );
    }
}
