package pl.marekk.taskmanager.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.time.Instant;
import java.util.UUID;

class ActiveTask {
  private final Instant createdAt;
  private final Task task;

  ActiveTask(Instant createdAt, Task task) {
    this.createdAt = createdAt;
    this.task = task;
  }

  static ActiveTask activeTask(Task task) {
    Preconditions.checkArgument(task != null, "task can not be null");
    return new ActiveTask(Instant.now(), task);
  }

  ActiveTask processKilling(KillingCommand killingCommand) {
    Preconditions.checkArgument(killingCommand != null, "killing command can not be null");
    if (killingCommand.applicableFor(task)) {
      task.kill();
      return null;
    }
    return this;
  }

  Instant createdAt() {
    return createdAt;
  }

  TaskPriority priority() {
    return task.priority();
  }

  UUID pid() {
    return task.pid();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActiveTask that = (ActiveTask) o;
    return Objects.equal(createdAt, that.createdAt) && Objects.equal(task, that.task);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(createdAt, task);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("createdAt", createdAt)
        .add("task", task)
        .toString();
  }
}
