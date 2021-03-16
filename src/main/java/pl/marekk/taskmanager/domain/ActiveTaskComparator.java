package pl.marekk.taskmanager.domain;

import java.util.Comparator;
import pl.marekk.taskmanager.domain.TaskPriority.TaskPriorityComparator;

@FunctionalInterface
interface ActiveTaskComparator extends Comparator<ActiveTask> {
  ActiveTaskComparator createdAtComparator = (t1, t2) -> t1.createdAt().compareTo(t2.createdAt());
  ActiveTaskComparator priorityComparator =
      (t1, t2) -> new TaskPriorityComparator().compare(t1.priority(), t2.priority());
  ActiveTaskComparator pidComparator = (t1, t2) -> t1.pid().compareTo(t2.pid());
}
