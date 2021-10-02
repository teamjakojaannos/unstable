package fi.jakojaannos.unstable.resources;

public record TimerHandle(long id, float duration, Timers.Action action, boolean looping) {}
