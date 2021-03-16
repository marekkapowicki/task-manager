package pl.marekk.taskmanager.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class TaskPriorityTest {

  @Test
  void lowerThanLowPriority() {
    // when
    final List<TaskPriority> lowerPriorities = TaskPriority.LOW.allLowerPrioritiesThan();

    // then
    assertThat(lowerPriorities).isEmpty();
  }

  @Test
  void lowerThanMediumPriority() {
    // when
    final List<TaskPriority> lowerPriorities = TaskPriority.MEDIUM.allLowerPrioritiesThan();

    // then
    assertThat(lowerPriorities).hasSize(1).contains(TaskPriority.LOW);
  }

  @Test
  void lowerThanHighPriority() {
    // when
    final List<TaskPriority> lowerPriorities = TaskPriority.HIGH.allLowerPrioritiesThan();

    // then
    assertThat(lowerPriorities).hasSize(2).containsExactly(TaskPriority.LOW, TaskPriority.MEDIUM);
  }
}
