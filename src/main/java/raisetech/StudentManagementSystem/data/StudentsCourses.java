package raisetech.StudentManagementSystem.data;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentsCourses {

  private int id;
  private int studentId;
  private Course course;
  private LocalDate startDateAt;
  private LocalDate endDateAt;
  private String courseName;
}
