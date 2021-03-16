package pl.marekk.taskmanager.domain;

import static pl.marekk.taskmanager.domain.DefaultTaskManager.taskManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public interface TaskManager {

  static TaskManager priorityBasedTaskManager(int maxSize) {
    BiFunction<List<ActiveTask>, Task, List<ActiveTask>> priorityBasedFunction =
        PriorityBasedStrategy::cleanupStaleElement;
    return taskManager(maxSize, priorityBasedFunction);
  }

  static TaskManager fifoTaskManager(int maxSize) {
    BiFunction<List<ActiveTask>, Task, List<ActiveTask>> tail =
        (list, elementToAdd) -> new ArrayList<>(list).subList(1, list.size());
    return taskManager(maxSize, tail);
  }

  static TaskManager fixedSizeTaskManager(int maxSize) {
    BiFunction<List<ActiveTask>, Task, List<ActiveTask>> identity =
        (list, elementToAdd) -> new ArrayList<>(list);
    return taskManager(maxSize, identity);
  }

  boolean add(Task task);

  List<ActiveTask> listAllTasks();

  List<ActiveTask> listAllTasks(ActiveTaskComparator comparator);

  void killTasks(KillingCommand killingCommand);

  class PriorityBasedStrategy {

    private PriorityBasedStrategy() {
    }

    static List<ActiveTask> cleanupStaleElement(List<ActiveTask> list, Task task) {
      final Optional<ActiveTask> oldestTaskWithTheLowestPriority =
          findTheOldestTaskWithTheLowestPriorityLowerThan(list, task);
      return oldestTaskWithTheLowestPriority
          .map(
              elementToRemove ->
                  list.stream()
                      .filter(current -> !current.equals(elementToRemove))
                      .collect(Collectors.toCollection(CopyOnWriteArrayList::new)))
          .orElseGet(() -> new CopyOnWriteArrayList<>(list));
    }

    private static Optional<ActiveTask> findTheOldestTaskWithTheLowestPriorityLowerThan(
        List<ActiveTask> list, Task task) {
      final List<TaskPriority> taskPriorities = task.priority().allLowerPrioritiesThan();
      return taskPriorities.stream()
          .map(priority -> findTheOldestTaskWithThePriority(list, priority))
          .flatMap(Optional::stream)
          .filter(Objects::nonNull)
          .findFirst();
    }

    private static Optional<ActiveTask> findTheOldestTaskWithThePriority(
        List<ActiveTask> list, TaskPriority priority) {
      return list.stream().filter(it -> it.priority().equals(priority)).findFirst();
    }
  }
}
