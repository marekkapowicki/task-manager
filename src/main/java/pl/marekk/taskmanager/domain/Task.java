package pl.marekk.taskmanager.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Task {

  private static final Logger log = LoggerFactory.getLogger(Task.class.getName());
  private final UUID pid;

  private final TaskPriority priority;

  Task(UUID pid, TaskPriority priority) {
    this.pid = pid;
    this.priority = priority;
  }

  static Task newTask(TaskPriority priority) {
    Preconditions.checkArgument(priority != null, "priority can not be null");
    return new Task(UUID.randomUUID(), priority);
  }

  void kill() {
    // dummy method currently
    log.info("killing the task {}", this);
  }

  UUID pid() {
    return pid;
  }

  TaskPriority priority() {
    return priority;
  }

  // The easiest way is to use Lombok - but it requires the plugin and I decided not to use it
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Task task = (Task) o;
    return Objects.equal(pid, task.pid);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(pid);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("pid", pid).add("priority", priority).toString();
  }
}
