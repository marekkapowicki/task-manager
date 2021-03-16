package pl.marekk.taskmanager.domain;

import java.time.Instant;
import java.util.UUID;

public class TestActiveTaskBuilder {

  private Instant createdAt;
  private TaskPriority priority;
  private UUID pid;

  private TestActiveTaskBuilder(Instant createdAt, TaskPriority priority, UUID pid) {
    this.createdAt = createdAt;
    this.priority = priority;
    this.pid = pid;
  }

  static TestActiveTaskBuilder sample() {
    return new TestActiveTaskBuilder(Instant.now(), TaskPriority.LOW, UUID.randomUUID());
  }

  TestActiveTaskBuilder createdAt(Instant createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  TestActiveTaskBuilder priority(TaskPriority priority) {
    this.priority = priority;
    return this;
  }

  TestActiveTaskBuilder pid(UUID pid) {
    this.pid = pid;
    return this;
  }

  ActiveTask build() {
    return new ActiveTask(createdAt, new Task(pid, priority));
  }
}
