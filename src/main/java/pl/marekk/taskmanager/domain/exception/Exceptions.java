package pl.marekk.taskmanager.domain.exception;

public class Exceptions {

  private Exceptions() {
  }

  public static RuntimeException illegalState(String message) {
    return new IllegalStateException(message);
  }

  public static RuntimeException illegalArgument(String message) {
    return new IllegalArgumentException(message);
  }
}
