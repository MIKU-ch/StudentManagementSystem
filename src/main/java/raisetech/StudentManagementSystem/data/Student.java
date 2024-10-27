package raisetech.StudentManagementSystem.data;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private Integer id;
  private String name;
  private String kanaName;
  private String nickname;
  private String email;
  private String region;
  private int age;
  private String gender;
  private String remark;
  private boolean isDeleted;

  private List<StudentsCourses> studentsCourses = new ArrayList<>();

  // コース追加メソッド
  public void addCourse(StudentsCourses course) {
    course.setStudentId(this.id); // studentIdフィールドを設定
    this.studentsCourses.add(course);
  }
}
