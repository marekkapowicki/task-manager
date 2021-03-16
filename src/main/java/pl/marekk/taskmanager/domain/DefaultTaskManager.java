package pl.marekk.taskmanager.domain;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.marekk.taskmanager.domain.exception.Exceptions;

class DefaultTaskManager implements TaskManager {
  private static final Logger log = LoggerFactory.getLogger(DefaultTaskManager.class.getName());
  private final int predefinedMaxSize;
  private final BiFunction<List<ActiveTask>, Task, List<ActiveTask>> cleaningFunction;
  private final List<ActiveTask> container;

  DefaultTaskManager(
      int predefinedMaxSize,
      final BiFunction<List<ActiveTask>, Task, List<ActiveTask>> cleaningFunction,
      List<ActiveTask> container) {
    this.predefinedMaxSize = predefinedMaxSize;
    this.cleaningFunction = cleaningFunction;
    this.container = container;
  }

  static DefaultTaskManager taskManager(
      int maxSize, final BiFunction<List<ActiveTask>, Task, List<ActiveTask>> cleaningFunction) {
    if (maxSize <= 0) {
      throw Exceptions.illegalArgument("positive values are accepted. Given is: " + maxSize);
    }
    return new DefaultTaskManager(maxSize, cleaningFunction, new CopyOnWriteArrayList<>());
  }

  @Override
  public boolean add(Task task) {
    log.info("adding a task {}", task);
    Preconditions.checkArgument(task != null, "null task is not allowed");

    if (!freeSpaceAvailable()) {
      clearTasksAndAssignNewList(cleaningFunction.apply(container, task));
    }
    final ActiveTask taskToAdd = ActiveTask.activeTask(task);
    return add(taskToAdd);
  }

  @Override
  public List<ActiveTask> listAllTasks() {
    log.debug("listing all active processes");
    return List.copyOf(container);
  }

  @Override
  public List<ActiveTask> listAllTasks(ActiveTaskComparator comparator) {
    log.debug("listing all active processes using comparator");
    final List<ActiveTask> tasks = new ArrayList<>(container);
    tasks.sort(comparator);
    return Collections.unmodifiableList(tasks);
  }

  @Override
  public void killTasks(KillingCommand killingCommand) {
    log.debug("killing tasks using {}", killingCommand);
    final CopyOnWriteArrayList<ActiveTask> activeTasks =
        container.stream()
            .map(task -> task.processKilling(killingCommand))
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
    clearTasksAndAssignNewList(activeTasks);
  }

  private void clearTasksAndAssignNewList(List<ActiveTask> newTasks) {
    container.clear();
    container.addAll(Collections.unmodifiableList(newTasks));
  }

  private boolean add(ActiveTask taskToAdd) {
    if (freeSpaceAvailable()) {
      container.add(taskToAdd);
      container.sort(Comparator.comparing(ActiveTask::createdAt));
      return true;
    } else {
      log.debug("task was not added. capacity exceeded");
      return false;
    }
  }

  private boolean freeSpaceAvailable() {
    return container.size() < predefinedMaxSize;
  }
}
