package raisetech.StudentManagementSystem.controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import raisetech.StudentManagementSystem.controller.converter.StudentConverter;
import raisetech.StudentManagementSystem.data.StudentsCourses;
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


  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail(); // StudentDetailのインスタンスを作成
    studentDetail.setStudentsCourses(Arrays.asList(new StudentsCourses())); // コースの初期化
    model.addAttribute("studentDetail", studentDetail); // モデルに追加
    return "registerStudent";
  }


  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }

    // 受講生とコース情報を保存する
    service.saveStudentDetail(studentDetail);

    System.out.println(
        studentDetail.getStudent().getName() + "さんが新規受講生として登録されました。");
    return "redirect:/studentsCourseList";
  }
}
