package pl.marekk.taskmanager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.List;
import org.junit.jupiter.api.Test;

class AddingFixedSizeTaskManagerTest {

  @Test
  void doesNotWorkForMinusValueAsMaxSize() {
    // expect
    assertThatIllegalArgumentException().isThrownBy(() -> TaskManager.fixedSizeTaskManager(-3));
  }

  @Test
  void doesNotWorkFor0AsMaxSize() {
    // expect
    assertThatIllegalArgumentException().isThrownBy(() -> TaskManager.fixedSizeTaskManager(0));
  }

  @Test
  void shouldNotAddTheNullTask() {
    // given
    final TaskManager taskManager = TaskManager.fixedSizeTaskManager(1);
    // expect
    assertThatIllegalArgumentException().isThrownBy(() -> taskManager.add(null));
  }

  @Test
  void doesNotExceedTheCapacity() {
    // given
    final int maxSize = 3;
    final TaskManager taskManager = TaskManager.fixedSizeTaskManager(maxSize);

    taskManager.add((Task.newTask(TaskPriority.LOW)));
    taskManager.add((Task.newTask(TaskPriority.LOW)));
    taskManager.add((Task.newTask(TaskPriority.LOW)));

    // when
    final Task newOne = Task.newTask(TaskPriority.LOW);
    taskManager.add(newOne);
    final List<ActiveTask> activeTasks = taskManager.listAllTasks();

    // then
    assertThat(activeTasks)
        .hasSize(maxSize)
        .extracting(ActiveTask::pid)
        .doesNotContain(newOne.pid());
  }
}
