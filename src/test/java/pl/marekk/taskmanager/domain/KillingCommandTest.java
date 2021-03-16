package pl.marekk.taskmanager.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class KillingCommandTest {

  @Test
  void applyKillAll() {
    // when
    final boolean applicable =
        KillingCommand.killAll().applicableFor(Task.newTask(TaskPriority.MEDIUM));

    // then
    assertThat(applicable).isTrue();
  }

  @Test
  void applyKillByIdToValidTask() {
    // given
    final Task task = Task.newTask(TaskPriority.MEDIUM);
    // when
    final boolean applicable = KillingCommand.killByPid(task.pid()).applicableFor(task);

    // then
    assertThat(applicable).isTrue();
  }

  @Test
  void applyKillByIdToOtherTask() {
    // given
    final Task task = Task.newTask(TaskPriority.MEDIUM);
    final UUID nonExistingPid = UUID.randomUUID();
    final boolean applicable = KillingCommand.killByPid(nonExistingPid).applicableFor(task);

    // then
    assertThat(applicable).isFalse();
  }

  @Test
  void applyKillByPriorityToProperTask() {
    // given
    final Task task = Task.newTask(TaskPriority.MEDIUM);
    final boolean applicable =
        KillingCommand.killByPriority(TaskPriority.MEDIUM).applicableFor(task);

    // then
    assertThat(applicable).isTrue();
  }

  @Test
  void applyKillByPriorityToOtherTask() {
    // given
    final Task task = Task.newTask(TaskPriority.MEDIUM);
    final UUID nonExistingPid = UUID.randomUUID();
    final boolean applicable = KillingCommand.killByPid(nonExistingPid).applicableFor(task);

    // then
    assertThat(applicable).isFalse();
  }
}
