package raisetech.StudentManagementSystem.data;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import raisetech.StudentManagementSystem.domain.CourseStatusEnum;

@Getter
@Setter
public class CourseStatus {

  private Integer id;
  private Integer courseId;
  private CourseStatusEnum status;
  private LocalDate createdAt;
  private LocalDate updatedAt;
}
