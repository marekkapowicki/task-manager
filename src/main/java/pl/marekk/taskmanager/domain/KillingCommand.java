package pl.marekk.taskmanager.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.util.UUID;

@FunctionalInterface
public interface KillingCommand {
  static KillingCommand killAll() {
    return new KillAll();
  }

  static KillingCommand killByPid(UUID pid) {
    Preconditions.checkArgument(pid != null, "pid can not be null");
    return new KillByPidCommand(pid);
  }

  static KillingCommand killByPriority(TaskPriority priority) {
    Preconditions.checkArgument(priority != null, "priority can not be null");
    return new KillByPriorityCommand(priority);
  }

  boolean applicableFor(Task task);

  class KillAll implements KillingCommand {

    @Override
    public boolean applicableFor(Task task) {
      return true;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this).toString();
    }
  }

  class KillByPidCommand implements KillingCommand {

    private final UUID pid;

    private KillByPidCommand(UUID pid) {
      this.pid = pid;
    }

    @Override
    public boolean applicableFor(Task task) {
      return pid.equals(task.pid());
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this).add("pid", pid).toString();
    }
  }

  class KillByPriorityCommand implements KillingCommand {
    private final TaskPriority taskPriority;

    private KillByPriorityCommand(TaskPriority taskPriority) {
      this.taskPriority = taskPriority;
    }

    @Override
    public boolean applicableFor(Task task) {
      return taskPriority.equals(task.priority());
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this).add("taskPriority", taskPriority).toString();
    }
  }
}
