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

  @GetMapping("/studentsCourseList")
  public List<StudentDetail> getStudentsList() {
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList();
    List<Student> students = service.searchStudentList();
    return studentConverter.convertToStudentDetailList(students, studentsCourses);
  }

  @PatchMapping("/{id}/remarks")
  public ResponseEntity<String> updateStudentRemark(
      @PathVariable int id,
      @RequestBody String newRemark) {
    service.updateStudentRemark(id, newRemark);
    return ResponseEntity.ok("学生の備考が更新されました。");
  }

  @PatchMapping("/{id}/deletion-status")
  public ResponseEntity<String> updateStudentDeletionStatus(
      @PathVariable int id,
      @RequestBody boolean isDeleted) {
    service.updateStudentIsDeleted(id, isDeleted);
    return ResponseEntity.ok("受講生情報の削除が成功しました。");
  }
}
