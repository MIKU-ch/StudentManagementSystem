package raisetech.StudentManagementSystem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagementSystem.domain.StudentDetail;
import raisetech.StudentManagementSystem.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentController {

  private final StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  // 学生一覧（受講コース情報含む）を取得する
  @GetMapping
  public List<StudentDetail> listStudents() {
    return service.listStudentDetails();
  }

  // 新規学生を登録する
  @PostMapping
  public ResponseEntity<Map<String, Object>> registerStudent(
      @RequestBody StudentDetail studentDetail) {
    service.registerStudent(studentDetail);

    Map<String, Object> response = new HashMap<>();
    response.put("message",
        studentDetail.getStudent().getName() + "さんが新規受講生として登録されました。");
    response.put("studentDetail", studentDetail);

    return ResponseEntity.ok(response);
  }

  // 指定されたIDの学生情報を取得する
  @GetMapping("/{id}")
  public ResponseEntity<StudentDetail> getStudentDetail(@PathVariable int id) {
    StudentDetail studentDetail = service.getStudentDetailById(id);
    return ResponseEntity.ok(studentDetail);
  }

  // 指定された学生の情報を更新する。キャンセルフラグの更新もここで行います(論理削除)
  @PutMapping("/{id}")
  public ResponseEntity<String> updateStudent(@PathVariable int id,
      @RequestBody StudentDetail studentDetail) {
    studentDetail.getStudent().setId(id); // IDをセット
    service.updateStudent(studentDetail);
    return ResponseEntity.ok(
        studentDetail.getStudent().getName() + "さんの受講生情報が更新されました。");
  }
}
