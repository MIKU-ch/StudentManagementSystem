package raisetech.StudentManagementSystem.domain;

public enum Gender {
  MALE("男性"),
  FEMALE("女性"),
  OTHER("その他");

  private final String label;

  private Gender(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  public static Gender fromLabel(String label) {
    for (Gender gender : values()) {
      if (gender.label.equals(label)) {
        return gender;
      }
    }
    return null;
  }
}
