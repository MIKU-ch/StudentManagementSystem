package raisetech.StudentManagementSystem.dto;

import lombok.Getter;
import lombok.Setter;
import raisetech.StudentManagementSystem.domain.StudentDetail;

@Setter
@Getter
public class StudentRegisterResponseDTO {

  private String message;
  private StudentDetail studentDetail;

  public StudentRegisterResponseDTO() {
  }

  public StudentRegisterResponseDTO(String message, StudentDetail studentDetail) {
    this.message = message;
    this.studentDetail = studentDetail;
  }

}
