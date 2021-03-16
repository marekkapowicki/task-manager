package pl.marekk.taskmanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AddingPriorityBasedTaskManagerTest {

  @Test
  void shouldNotAddTheLowerPriority() {
    // given
    final TaskManager taskManager = TaskManager.priorityBasedTaskManager(1);
    taskManager.add(Task.newTask(TaskPriority.HIGH));

    // when
    final Task newOne = Task.newTask(TaskPriority.LOW);
    final boolean added = taskManager.add(newOne);
    // then
    assertThat(added).isFalse();

    assertThat(taskManager.listAllTasks())
        .hasSize(1)
        .extracting(ActiveTask::pid)
        .doesNotContain(newOne.pid());
  }

  @Test
  void shouldAddTheHigherPriority() {
    // given
    final TaskManager taskManager = TaskManager.priorityBasedTaskManager(1);
    taskManager.add(Task.newTask(TaskPriority.MEDIUM));

    // when
    final Task newOne = Task.newTask(TaskPriority.HIGH);
    final boolean added = taskManager.add(newOne);
    // then
    assertThat(added).isTrue();

    assertThat(taskManager.listAllTasks())
        .hasSize(1)
        .extracting(ActiveTask::pid)
        .contains(newOne.pid());
  }

  @Test
  void shouldRemoveOnlyTheOldestElementWithLowestPriority() {
    // given
    final TaskManager taskManager = TaskManager.priorityBasedTaskManager(2);
    final Task oldestTask = Task.newTask(TaskPriority.MEDIUM);
    taskManager.add(oldestTask);

    final Task taskToRemove = Task.newTask(TaskPriority.LOW);
    taskManager.add(taskToRemove);

    // when
    final Task newOne = Task.newTask(TaskPriority.HIGH);
    final boolean added = taskManager.add(newOne);
    // then
    assertThat(added).isTrue();

    assertThat(taskManager.listAllTasks())
        .hasSize(2)
        .extracting(ActiveTask::pid)
        .doesNotContain(taskToRemove.pid())
        .contains(newOne.pid(), oldestTask.pid());
  }

  @Test
  void shouldRemoveOnlyTheFirstElementWithLowestPriority() {
    // given
    final TaskManager taskManager = TaskManager.priorityBasedTaskManager(2);
    final Task oldestTaskToRemove = Task.newTask(TaskPriority.LOW);
    taskManager.add(oldestTaskToRemove);

    final Task taskToKeep = Task.newTask(TaskPriority.LOW);
    taskManager.add(taskToKeep);

    // when
    final Task newOne = Task.newTask(TaskPriority.HIGH);
    final boolean added = taskManager.add(newOne);
    // then
    assertThat(added).isTrue();

    assertThat(taskManager.listAllTasks())
        .hasSize(2)
        .extracting(ActiveTask::pid)
        .doesNotContain(oldestTaskToRemove.pid())
        .contains(newOne.pid(), taskToKeep.pid());
  }
}
