package pl.marekk.taskmanager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class KillingTasksTest {

  @Test
  void killInTheEmptyTasManagerDoesNotThrowAnException() {
    // given
    final TaskManager taskManager = TaskManager.fixedSizeTaskManager(1);

    // expect
    assertThatCode(() -> taskManager.killTasks(KillingCommand.killAll()))
        .doesNotThrowAnyException();
  }

  @Test
  void killAllTasks() {
    // given
    final TaskManager taskManager = TaskManager.fixedSizeTaskManager(2);

    // when
    taskManager.killTasks(KillingCommand.killAll());

    // then
    assertThat(taskManager.listAllTasks()).isEmpty();
  }

  @Test
  void killByNotExistingPidDoesNotChangeTheTasks() {
    // given
    final TaskManager taskManager = TaskManager.fixedSizeTaskManager(2);
    final Task task = Task.newTask(TaskPriority.LOW);
    taskManager.add(task);
    final Task otherTask = Task.newTask(TaskPriority.MEDIUM);
    taskManager.add(otherTask);

    // when
    taskManager.killTasks(KillingCommand.killByPid(UUID.randomUUID()));

    // then
    assertThat(taskManager.listAllTasks())
        .hasSize(2)
        .extracting(ActiveTask::pid)
        .contains(task.pid(), otherTask.pid());
  }

  @Test
  void killByNotExistingPriorityDoesNotChangeTheTasks() {
    // given
    final TaskManager taskManager = TaskManager.fixedSizeTaskManager(2);
    final Task task = Task.newTask(TaskPriority.LOW);
    taskManager.add(task);
    final Task otherTask = Task.newTask(TaskPriority.MEDIUM);
    taskManager.add(otherTask);

    // when
    taskManager.killTasks(KillingCommand.killByPriority(TaskPriority.HIGH));

    // then
    assertThat(taskManager.listAllTasks())
        .hasSize(2)
        .extracting(ActiveTask::pid)
        .contains(task.pid(), otherTask.pid());
  }

  @Test
  void killByExistingPid() {
    // given
    final TaskManager taskManager = TaskManager.fixedSizeTaskManager(2);
    final Task task = Task.newTask(TaskPriority.LOW);
    taskManager.add(task);
    final Task otherTask = Task.newTask(TaskPriority.MEDIUM);
    taskManager.add(otherTask);

    // when
    taskManager.killTasks(KillingCommand.killByPid(otherTask.pid()));

    // then
    assertThat(taskManager.listAllTasks())
        .hasSize(1)
        .extracting(ActiveTask::pid)
        .contains(task.pid())
        .doesNotContain(otherTask.pid());
  }

  @Test
  void killByExistingPriority() {
    // given
    final TaskManager taskManager = TaskManager.fixedSizeTaskManager(3);
    final Task lowTaskToRemove = Task.newTask(TaskPriority.LOW);
    taskManager.add(lowTaskToRemove);
    final Task taskToKeep = Task.newTask(TaskPriority.MEDIUM);
    taskManager.add(taskToKeep);
    final Task otherLowTaskToRemove = Task.newTask(TaskPriority.LOW);
    taskManager.add(otherLowTaskToRemove);

    // when
    taskManager.killTasks(KillingCommand.killByPriority(TaskPriority.LOW));

    // then
    assertThat(taskManager.listAllTasks())
        .hasSize(1)
        .extracting(ActiveTask::pid)
        .contains(taskToKeep.pid())
        .doesNotContain(lowTaskToRemove.pid(), otherLowTaskToRemove.pid());
  }
}
