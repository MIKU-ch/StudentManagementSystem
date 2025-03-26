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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.domain.CourseStatus;
import raisetech.StudentManagementSystem.domain.Gender;
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

  @Operation(summary = "一覧検索", description = "指定された条件で受講生情報（受講コース情報含む）をDB側で絞り込み検索する")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "条件に一致する学生情報のリストが返される")
  })
  @GetMapping
  public ResponseEntity<Map<String, Object>> listStudents(
      @RequestParam(required = false) Integer studentId,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String kanaName,
      @RequestParam(required = false) String nickname,
      @RequestParam(required = false) String email,
      @RequestParam(required = false) String region,
      @RequestParam(required = false) Integer age,
      @RequestParam(required = false) Gender gender,
      @RequestParam(required = false) String remark,
      @RequestParam(required = false) Boolean isDeleted,
      @RequestParam(required = false) Integer courseId,
      @RequestParam(required = false) Integer courseStudentId,
      @RequestParam(required = false) String courseName,
      @RequestParam(required = false) CourseStatus status
  ) {
    Map<String, Object> params = new HashMap<>();
    params.put("studentId", studentId);
    params.put("name", name);
    params.put("kanaName", kanaName);
    params.put("nickname", nickname);
    params.put("email", email);
    params.put("region", region);
    params.put("age", age);
    params.put("gender", gender);
    params.put("remark", remark);
    params.put("isDeleted", isDeleted);
    params.put("courseId", courseId);
    params.put("courseStudentId", courseStudentId);
    params.put("courseName", courseName);
    params.put("status", status);

    List<StudentDetail> students;
    // 全てのパラメータがnullの場合、モックされている listStudentDetails() を呼ぶ
    if (params.values().stream().allMatch(v -> v == null)) {
      students = service.listStudentDetails();
    } else {
      students = service.searchStudentDetails(params);
    }

    Map<String, Object> response = new HashMap<>();
    response.put("students", students);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "受講生登録", description = "新規の受講生を登録する")
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

  @Operation(summary = "学生情報取得", description = "指定されたIDの学生情報を取得する")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "指定された学生の詳細情報が返される"),
      @ApiResponse(responseCode = "400", description = "入力されたIDが無効")
  })
  @GetMapping("/{id}")
  public ResponseEntity<Map<String, Object>> getStudentDetail(@PathVariable @Min(1) int id) {
    StudentDetail studentDetail = service.getStudentDetailById(id);
    Map<String, Object> response = new HashMap<>();
    response.put("studentDetail", studentDetail);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "学生基本情報更新", description = "指定されたIDの学生基本情報を更新する")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "更新成功。更新完了メッセージが返される"),
      @ApiResponse(responseCode = "400", description = "入力エラー")
  })
  @PutMapping("/{id}")
  public ResponseEntity<Map<String, Object>> updateStudentBasicInfo(
      @PathVariable int id,
      @RequestBody @Valid Student student) {
    // URLのIDを受け取って、リクエストボディの学生情報にセットする
    student.setId(id);
    service.updateStudentBasicInfo(student);
    Map<String, Object> response = new HashMap<>();
    response.put("message", "学生基本情報を更新しました");
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "受講コース情報更新", description = "指定された学生の特定の受講コース情報を更新する")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "更新成功。更新完了メッセージが返される"),
      @ApiResponse(responseCode = "400", description = "入力エラー"),
      @ApiResponse(responseCode = "500", description = "サーバーエラー")
  })
  @PutMapping("/{studentId}/courses/{courseId}")
  public ResponseEntity<Map<String, Object>> updateStudentCourse(
      @PathVariable int studentId,
      @PathVariable int courseId,
      @RequestBody @Valid StudentsCourses course) {

    // URLのcourseIdとstudentIdを設定。BodyにはIDは含めない。
    course.setId(courseId);
    course.setStudentId(studentId);
    // ここでは、リクエストボディで courseStatusId が渡されている前提

    Map<String, Object> response = new HashMap<>();

    try {
      service.updateStudentCourse(studentId, course);
      response.put("message", "受講コース情報を更新しました");
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      response.put("message", "更新に失敗しました: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  @Operation(summary = "受講生にコースを追加", description = "指定された受講生に新しいコースを追加する")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "コース追加成功"),
      @ApiResponse(responseCode = "400", description = "入力エラー")
  })
  @PostMapping("/{id}/courses")
  public ResponseEntity<Map<String, Object>> addCourseForStudent(
      @PathVariable @Min(1) int id,
      @Valid @RequestBody StudentsCourses sc) {
    // デフォルトで courseStatusId を設定（nullの場合、仮申込：KARI_APPLY に対応するID、ここでは1と仮定）
    if (sc.getCourseStatusId() == null) {
      sc.setCourseStatusId(1);
    }
    service.addCourseForStudent(id, sc);
    Map<String, Object> response = new HashMap<>();
    response.put("message", "新しいコースが追加されました。");
    return ResponseEntity.ok(response);
  }
}
