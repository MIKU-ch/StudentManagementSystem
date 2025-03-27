package raisetech.StudentManagementSystem.dto;

import lombok.Getter;
import lombok.Setter;
import raisetech.StudentManagementSystem.domain.StudentDetail;

@Setter
@Getter
public class StudentDetailResponseDTO {

  private StudentDetail studentDetail;

  public StudentDetailResponseDTO() {
  }

  public StudentDetailResponseDTO(StudentDetail studentDetail) {
    this.studentDetail = studentDetail;
  }

}
