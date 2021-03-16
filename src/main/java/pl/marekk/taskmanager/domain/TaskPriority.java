package pl.marekk.taskmanager.domain;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

enum TaskPriority {
  LOW(Integer.MIN_VALUE),
  MEDIUM(1),
  HIGH(Integer.MAX_VALUE);
  private final int priority;

  TaskPriority(int priority) {
    this.priority = priority;
  }

  List<TaskPriority> allLowerPrioritiesThan() {
    return Arrays.stream(values())
        .filter(this::higherPriorityThen)
        .sorted(Comparator.comparing(taskPriority -> taskPriority.priority))
        .collect(Collectors.toList());
  }

  private boolean higherPriorityThen(TaskPriority other) {
    return this.priority > other.priority;
  }

  static class TaskPriorityComparator implements Comparator<TaskPriority> {

    @Override
    public int compare(TaskPriority o1, TaskPriority o2) {
      return Integer.compare(o1.priority, o2.priority);
    }
  }
}
