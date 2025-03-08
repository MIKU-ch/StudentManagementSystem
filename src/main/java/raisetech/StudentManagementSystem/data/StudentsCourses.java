package raisetech.StudentManagementSystem.data;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentsCourses {

  private Integer id;
  private Integer studentId;
  private String courseName;
  private LocalDate startDateAt;
  private LocalDate endDateAt;
}
