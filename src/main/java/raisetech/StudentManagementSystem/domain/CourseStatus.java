package raisetech.StudentManagementSystem.domain;

import lombok.Getter;

@Getter
public enum CourseStatus {
  KARI_APPLY("仮申込"),
  HON_APPLY("本申込"),
  ENROLLING("受講中"),
  FINISHED("受講終了");

  private final String label;

  private CourseStatus(String label) {
    this.label = label;
  }

  public static CourseStatus fromLabel(String label) {
    for (CourseStatus status : values()) {
      if (status.label.equals(label)) {
        return status;
      }
    }
    return null;
  }
}
