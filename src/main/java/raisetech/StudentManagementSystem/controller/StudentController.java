package raisetech.StudentManagementSystem.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagementSystem.controller.converter.StudentConverter;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.domain.StudentDetail;
import raisetech.StudentManagementSystem.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentController {

  private final StudentService service;
  private final StudentConverter studentConverter;

  @Autowired
  public StudentController(StudentService service, StudentConverter studentConverter) {
    this.service = service;
    this.studentConverter = studentConverter;
  }

  // コースを全件表示
  @GetMapping("/studentsCourseList")
  public List<StudentDetail> getStudentsList() {
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList(); // コースリストを取得
    List<Student> students = service.searchStudentList(); // 全ての学生を取得

    // StudentConverter を使って StudentDetail のリストを取得
    return studentConverter.convertToStudentDetailList(students, studentsCourses);
  }

  // 備考（remark）を更新するエンドポイント
  @PatchMapping("/{id}/remark")
  public ResponseEntity<String> updateStudentRemark(
      @PathVariable int id,
      @RequestBody String newRemark) {
    service.updateStudentRemark(id, newRemark);
    return ResponseEntity.ok("備考欄に情報が追加されました。");
  }

  // 論理削除（isDeleted）を更新するエンドポイント
  @PatchMapping("/{id}/delete")
  public ResponseEntity<String> updateStudentDeletionStatus(
      @PathVariable int id,
      @RequestBody boolean isDeleted) {
    service.updateStudentDeletionStatus(id, isDeleted);
    return ResponseEntity.ok("論理削除が成功しました。");
  }
}
