package raisetech.StudentManagementSystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagementSystem.domain.StudentDetail;
import raisetech.StudentManagementSystem.service.StudentService;

@Validated
@RestController
@RequestMapping("/students")
@Tag(name = "Student", description = "学生の登録、更新、取得などのAPI")
public class StudentController {

  private final StudentService service;

  public StudentController(StudentService service) {
    this.service = service;
  }

  @Operation(summary = "一覧検索", description = "すべての受講生情報（受講コース情報含む）を取得する")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "学生情報のリストが返される")
  })
  @GetMapping
  public List<StudentDetail> listStudents() {
    return service.listStudentDetails();
  }

  @Operation(summary = "受講生登録", description = "新規の受講生を登録する。登録する学生の詳細情報（受講コース情報含む）を受け取り、登録結果のメッセージと学生情報を返す")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "登録成功。メッセージと登録された学生情報が返される"),
      @ApiResponse(responseCode = "400", description = "入力エラー")
  })
  @PostMapping
  public ResponseEntity<Map<String, Object>> registerStudent(
      @Valid @RequestBody StudentDetail studentDetail) {
    service.registerStudent(studentDetail);

    Map<String, Object> response = new HashMap<>();
    response.put("message",
        studentDetail.getStudent().getName() + "さんが新規受講生として登録されました。");
    response.put("studentDetail", studentDetail);

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "学生情報取得", description = "指定されたIDの学生情報を取得する。学生IDは1以上の整数である必要がある")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "指定された学生の詳細情報が返される"),
      @ApiResponse(responseCode = "400", description = "入力されたIDが無効")
  })
  @GetMapping("/{id}")
  public ResponseEntity<StudentDetail> getStudentDetail(@PathVariable @Min(1) int id) {
    StudentDetail studentDetail = service.getStudentDetailById(id);
    return ResponseEntity.ok(studentDetail);
  }

  @Operation(summary = "学生情報更新", description = "指定されたIDの学生情報を更新する。更新後の学生情報をリクエストボディで受け取る")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "更新成功。更新完了メッセージが返される"),
      @ApiResponse(responseCode = "400", description = "入力エラー")
  })
  @PutMapping("/{id}")
  public ResponseEntity<String> updateStudent(@PathVariable @Min(1) int id,
      @Valid @RequestBody StudentDetail studentDetail) {
    studentDetail.getStudent().setId(id);
    service.updateStudent(studentDetail);
    return ResponseEntity.ok(
        studentDetail.getStudent().getName() + "さんの受講生情報が更新されました。");
  }
}
