package raisetech.StudentManagementSystem.controller.handler;

import jakarta.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;
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
  public ResponseEntity<Map<String, String>> handleCustomAppException(CustomAppException ex) {
    Map<String, String> errorResponse = new LinkedHashMap<>();
    errorResponse.put("error", "エラーメッセージ");
    errorResponse.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, Object> errorResponse = new LinkedHashMap<>();
    errorResponse.put("error", "バリデーションエラー");
    Map<String, String> fieldErrors = new LinkedHashMap<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      fieldErrors.put(error.getField(), error.getDefaultMessage());
    }
    errorResponse.put("fieldErrors", fieldErrors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(errorResponse);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, Object>> handleConstraintViolationException(
      ConstraintViolationException ex) {
    Map<String, Object> errorResponse = new LinkedHashMap<>();
    errorResponse.put("error", "バリデーションエラー");
    Map<String, String> fieldErrors = new LinkedHashMap<>();
    ex.getConstraintViolations().forEach(violation -> {
      fieldErrors.put(violation.getPropertyPath().toString(), violation.getMessage());
    });
    errorResponse.put("fieldErrors", fieldErrors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
    Map<String, String> errorResponse = new LinkedHashMap<>();
    errorResponse.put("error", "予期しないエラー");
    errorResponse.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(errorResponse);
  }
}
