package pl.marekk.taskmanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ListingTasksTest {

  private static Instant currentDayWithTime(int hour, int minute) {
    final LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, minute));
    return toInstant(dateTime);
  }

  private static Instant toInstant(LocalDateTime date) {
    return date.toInstant(ZoneOffset.UTC);
  }

  @Test
  void sortByDate() {
    // given
    final Instant latestDate = currentDayWithTime(12, 45);
    final ActiveTask latestTask = TestActiveTaskBuilder.sample().createdAt(latestDate).build();
    // and
    final Instant olderDate = currentDayWithTime(10, 30);
    final ActiveTask olderTask = TestActiveTaskBuilder.sample().createdAt(olderDate).build();
    final TaskManager taskManager = sampleTaskManager(latestTask, olderTask);

    // when
    final List<ActiveTask> tasks =
        taskManager.listAllTasks(ActiveTaskComparator.createdAtComparator);

    // then
    assertThat(tasks)
        .hasSize(2)
        .extracting(ActiveTask::createdAt)
        .containsExactly(olderDate, latestDate);
  }

  @Test
  void sortByPriority() {
    // given
    final TaskManager taskManager = TaskManager.fixedSizeTaskManager(3);
    taskManager.add(Task.newTask(TaskPriority.HIGH));
    taskManager.add(Task.newTask(TaskPriority.LOW));
    taskManager.add(Task.newTask(TaskPriority.MEDIUM));

    // when
    final List<ActiveTask> tasks =
        taskManager.listAllTasks(ActiveTaskComparator.priorityComparator);

    // then
    assertThat(tasks)
        .hasSize(3)
        .extracting(ActiveTask::priority)
        .containsExactly(TaskPriority.LOW, TaskPriority.MEDIUM, TaskPriority.HIGH);
  }

  @Test
  void sortByPid() {
    // given
    final UUID pid1 = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
    final ActiveTask task1 = TestActiveTaskBuilder.sample().pid(pid1).build();
    // and
    final UUID pid2 = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00e");
    final ActiveTask task2 = TestActiveTaskBuilder.sample().pid(pid2).build();
    final TaskManager taskManager = sampleTaskManager(task1, task2);

    // when
    final List<ActiveTask> tasks = taskManager.listAllTasks(ActiveTaskComparator.pidComparator);

    // then -> comparing uuid is not super useful
    assertThat(tasks).hasSize(2).extracting(ActiveTask::pid).containsExactly(pid1, pid2);
  }

  private DefaultTaskManager sampleTaskManager(ActiveTask... tasks) {
    return new DefaultTaskManager(2, (list, el) -> list, Arrays.asList(tasks));
  }
}
