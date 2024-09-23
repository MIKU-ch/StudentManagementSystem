package raisetech.StudentManagementSystem.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;

@Getter
@Setter
public class StudentDetail {

  private Student student;
  private List<StudentsCourses> studentsCourses;
}
