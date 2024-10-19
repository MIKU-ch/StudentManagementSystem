package raisetech.StudentManagementSystem.data;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentsCourses {

  private int id;
  private int studentId;
  private String courseName;
  private LocalDateTime startDateAt;
  private LocalDateTime endDateAt;
}
