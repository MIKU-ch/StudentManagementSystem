package raisetech.StudentManagementSystem.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import raisetech.StudentManagementSystem.controller.converter.StudentConverter;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.domain.StudentDetail;
import raisetech.StudentManagementSystem.service.StudentService;

@Controller
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
  public String getStudentsList(Model model) {
    // 受講生とコースのリストを取得
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList();
    List<Student> students = service.searchStudentList();

    // StudentDetailのリストを取得し、モデルに追加
    List<StudentDetail> studentDetails = studentConverter.convertToStudentDetailList(students,
        studentsCourses);

    // StudentDisplayを使用せず、StudentDetailのリストをそのままHTMLに渡す
    model.addAttribute("studentList", studentDetails); // HTMLテンプレートに渡すリスト

    return "studentList"; // view名 (テンプレート名) を返す。たとえば "studentList.html"
  }

  @PatchMapping("/{id}/remark")
  public ResponseEntity<String> updateStudentRemark(
      @PathVariable int id,
      @RequestBody String newRemark) {
    service.updateStudentRemark(id, newRemark);
    return ResponseEntity.ok("学生の備考が更新されました。");
  }

  @PatchMapping("/{id}/delete")
  public ResponseEntity<String> updateStudentDeletionStatus(
      @PathVariable int id,
      @RequestBody boolean isDeleted) {
    service.updateStudentIsDeleted(id, isDeleted);
    return ResponseEntity.ok("受講生情報の削除が成功しました。");
  }
}
