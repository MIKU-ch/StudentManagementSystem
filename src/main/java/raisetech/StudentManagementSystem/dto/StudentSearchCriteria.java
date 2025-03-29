package raisetech.StudentManagementSystem.dto;

import lombok.Getter;
import lombok.Setter;
import raisetech.StudentManagementSystem.domain.CourseStatus;
import raisetech.StudentManagementSystem.domain.Gender;

@Getter
@Setter
public class StudentSearchCriteria {

  private Integer studentId;
  private String name;
  private String kanaName;
  private String nickname;
  private String email;
  private String region;
  private Integer age;
  private Gender gender;
  private String remark;
  private Boolean isDeleted;
  private Integer courseId;
  private Integer courseStudentId;
  private String courseName;
  private CourseStatus status;

  // すべてのフィールドが null かどうかを判定するヘルパーメソッド
  public boolean isEmpty() {
    return studentId == null &&
        name == null &&
        kanaName == null &&
        nickname == null &&
        email == null &&
        region == null &&
        age == null &&
        gender == null &&
        remark == null &&
        isDeleted == null &&
        courseId == null &&
        courseStudentId == null &&
        courseName == null &&
        status == null;
  }
}
