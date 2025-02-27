package raisetech.StudentManagementSystem.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import raisetech.StudentManagementSystem.data.Course;
import raisetech.StudentManagementSystem.data.Student;

@Getter
@Setter
public class StudentDetail {

  private Student student;
  private List<Course> courses;
}
