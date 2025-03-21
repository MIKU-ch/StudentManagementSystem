package raisetech.StudentManagementSystem.domain;

import lombok.Getter;

@Getter
public enum CourseStatusEnum {
  KARI_APPLY("仮申込"),
  HON_APPLY("本申込"),
  ENROLLING("受講中"),
  FINISHED("受講終了");

  private final String label;

  private CourseStatusEnum(String label) {
    this.label = label;
  }

  public static CourseStatusEnum fromLabel(String label) {
    for (CourseStatusEnum status : values()) {
      if (status.label.equals(label)) {
        return status;
      }
    }
    return null;
  }
}
