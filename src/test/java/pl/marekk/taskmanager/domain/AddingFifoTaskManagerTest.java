package pl.marekk.taskmanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class AddingFifoTaskManagerTest {

  @Test
  void shouldRemoveTheOldestElement() {

    // given
    final TaskManager taskManager = TaskManager.fifoTaskManager(1);
    final Task oldTask = Task.newTask(TaskPriority.LOW);
    taskManager.add(oldTask);
    // when
    final Task newOne = Task.newTask(TaskPriority.HIGH);
    final boolean added = taskManager.add(newOne);

    // then
    assertThat(added).isTrue();
    final List<ActiveTask> activeTasks = taskManager.listAllTasks();

    assertThat(activeTasks)
        .extracting(ActiveTask::pid)
        .doesNotContain(oldTask.pid())
        .contains(newOne.pid());
  }
}
