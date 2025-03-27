package raisetech.StudentManagementSystem.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageResponseDTO {

  private String message;

  public MessageResponseDTO() {
  }

  public MessageResponseDTO(String message) {
    this.message = message;
  }

}
