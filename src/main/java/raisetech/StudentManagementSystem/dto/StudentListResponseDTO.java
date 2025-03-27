package raisetech.StudentManagementSystem.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import raisetech.StudentManagementSystem.domain.StudentDetail;

@Setter
@Getter
public class StudentListResponseDTO {

  private List<StudentDetail> students;

  public StudentListResponseDTO() {
  }

  public StudentListResponseDTO(List<StudentDetail> students) {
    this.students = students;
  }

}
