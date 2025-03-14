package raisetech.StudentManagementSystem.controller;

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

/**
 * 受講生情報を管理するコントローラークラス。 学生の登録、更新、取得などのAPIを提供する。
 */
@Validated
@RestController
@RequestMapping("/students")
public class StudentController {

  private final StudentService service;

  /**
   * コンストラクタ
   *
   * @param service 学生情報を管理するサービス
   */
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * すべての受講生情報（受講コース情報含む）を取得する。
   *
   * @return 学生情報のリスト
   */
  @GetMapping
  public List<StudentDetail> listStudents() {
    return service.listStudentDetails();
  }

  /**
   * 新規の受講生を登録する。
   *
   * @param studentDetail 登録する学生の詳細情報（受講コース情報含む）
   * @return 登録結果のメッセージと学生情報
   */
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

  /**
   * 指定されたIDの学生情報を取得する。
   *
   * @param id 学生ID（1以上の整数）
   * @return 指定された学生の詳細情報
   */
  @GetMapping("/{id}")
  public ResponseEntity<StudentDetail> getStudentDetail(@PathVariable @Min(1) int id) {
    StudentDetail studentDetail = service.getStudentDetailById(id);
    return ResponseEntity.ok(studentDetail);
  }

  /**
   * 指定されたIDの学生情報を更新する。
   *
   * @param id            更新対象の学生ID（1以上の整数）
   * @param studentDetail 更新後の学生情報
   * @return 更新完了メッセージ
   */
  @PutMapping("/{id}")
  public ResponseEntity<String> updateStudent(@PathVariable @Min(1) int id,
      @Valid @RequestBody StudentDetail studentDetail) {
    studentDetail.getStudent().setId(id);
    service.updateStudent(studentDetail);
    return ResponseEntity.ok(
        studentDetail.getStudent().getName() + "さんの受講生情報が更新されました。");
  }
}
