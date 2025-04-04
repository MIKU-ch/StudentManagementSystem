package raisetech.StudentManagementSystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.domain.StudentDetail;
import raisetech.StudentManagementSystem.dto.MessageResponseDTO;
import raisetech.StudentManagementSystem.dto.StudentDetailResponseDTO;
import raisetech.StudentManagementSystem.dto.StudentListResponseDTO;
import raisetech.StudentManagementSystem.dto.StudentRegisterResponseDTO;
import raisetech.StudentManagementSystem.dto.StudentSearchCriteria;
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

  @Operation(
      summary = "一覧検索",
      description = "指定された条件で受講生情報（受講コース情報含む）をDB側で絞り込み検索する"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "条件に一致する学生情報のリストが返される")
  })
  @GetMapping
  public ResponseEntity<StudentListResponseDTO> listStudents(
      @ModelAttribute StudentSearchCriteria criteria) {
    List<StudentDetail> students;
    // 全てのパラメータが null または空の場合、モックされている listStudentDetails() を呼ぶ
    if (criteria.isEmpty()) {
      students = service.listStudentDetails();
    } else {
      students = service.searchStudentDetails(criteria);
    }

    StudentListResponseDTO response = new StudentListResponseDTO(students);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "受講生登録", description = "新規の受講生を登録する")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "登録成功。メッセージと登録された学生情報が返される"),
      @ApiResponse(responseCode = "400", description = "入力エラー")
  })
  @PostMapping
  public ResponseEntity<StudentRegisterResponseDTO> registerStudent(
      @Valid @RequestBody StudentDetail studentDetail) {
    service.registerStudent(studentDetail);
    String message = studentDetail.getStudent().getName() + "さんが新規受講生として登録されました。";
    StudentRegisterResponseDTO response = new StudentRegisterResponseDTO(message, studentDetail);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "学生情報取得", description = "指定されたIDの学生情報を取得する")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "指定された学生の詳細情報が返される"),
      @ApiResponse(responseCode = "400", description = "入力されたIDが無効")
  })
  @GetMapping("/{id}")
  public ResponseEntity<StudentDetailResponseDTO> getStudentDetail(@PathVariable @Min(1) int id) {
    StudentDetail studentDetail = service.getStudentDetailById(id);
    StudentDetailResponseDTO response = new StudentDetailResponseDTO(studentDetail);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "学生基本情報更新", description = "指定されたIDの学生基本情報を更新する")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "更新成功。更新完了メッセージが返される"),
      @ApiResponse(responseCode = "400", description = "入力エラー")
  })
  @PutMapping("/{id}")
  public ResponseEntity<MessageResponseDTO> updateStudentBasicInfo(
      @PathVariable int id,
      @RequestBody @Valid Student student) {
    student.setId(id);
    service.updateStudentBasicInfo(student);
    MessageResponseDTO response = new MessageResponseDTO("学生基本情報を更新しました");
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "受講コース情報更新", description = "指定された学生の特定の受講コース情報を更新する")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "更新成功。更新完了メッセージが返される"),
      @ApiResponse(responseCode = "400", description = "入力エラー"),
      @ApiResponse(responseCode = "500", description = "サーバーエラー")
  })
  @PutMapping("/{studentId}/courses/{courseId}")
  public ResponseEntity<MessageResponseDTO> updateStudentCourse(
      @PathVariable int studentId,
      @PathVariable int courseId,
      @RequestBody @Valid StudentsCourses course) {

    course.setId(courseId);
    course.setStudentId(studentId);

    try {
      service.updateStudentCourse(studentId, course);
      MessageResponseDTO response = new MessageResponseDTO("受講コース情報を更新しました");
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      MessageResponseDTO response = new MessageResponseDTO("更新に失敗しました: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  @Operation(summary = "受講生にコースを追加", description = "指定された受講生に新しいコースを追加する")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "コース追加成功"),
      @ApiResponse(responseCode = "400", description = "入力エラー")
  })
  @PostMapping("/{id}/courses")
  public ResponseEntity<MessageResponseDTO> addCourseForStudent(
      @PathVariable @Min(1) int id,
      @Valid @RequestBody StudentsCourses sc) {
    if (sc.getCourseStatusId() == null) {
      sc.setCourseStatusId(1);
    }
    service.addCourseForStudent(id, sc);
    MessageResponseDTO response = new MessageResponseDTO("新しいコースが追加されました。");
    return ResponseEntity.ok(response);
  }
}
