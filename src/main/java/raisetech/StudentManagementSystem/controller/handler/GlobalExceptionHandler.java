package raisetech.StudentManagementSystem.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import raisetech.StudentManagementSystem.exception.CustomAppException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomAppException.class)
  public ResponseEntity<String> handleCustomAppException(CustomAppException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("エラーメッセージ：\n" + ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    StringBuilder errors = new StringBuilder();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.append("エラーメッセージ: ")
          .append(error.getDefaultMessage())
          .append("\n");
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.toString());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGeneralException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("エラーメッセージ：\n" + ex.getMessage());
  }
}
