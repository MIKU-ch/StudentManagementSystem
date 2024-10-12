package raisetech.StudentManagementSystem.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import raisetech.StudentManagementSystem.controller.converter.StudentConverter;
import raisetech.StudentManagementSystem.domain.StudentDetail;
import raisetech.StudentManagementSystem.service.StudentService;

@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;


  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  // 学生とコースの一覧を取得するエンドポイント
  @GetMapping("/studentsCourseList")
  public String getStudentsCourseList(Model model) {
    List<StudentDetail> studentDetails = service.getStudentsWithCourses();
    model.addAttribute("studentList", studentDetails);
    return "studentList"; // テンプレート名
  }
}
