package raisetech.StudentManagementSystem.controller;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import raisetech.StudentManagementSystem.controller.converter.StudentConverter;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.domain.StudentDetail;
import raisetech.StudentManagementSystem.service.StudentService;


@Controller
@Validated
public class StudentController {

  private final StudentService service;
  private final StudentConverter converter;

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

  @GetMapping("/student/{id}")
  public String getStudent(@PathVariable int id, Model model) {
    StudentDetail studentDetail = service.searchStudentById(id);
    model.addAttribute("studentDetail", studentDetail); // モデルに追加
    return "updateStudent";
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

  @PostMapping("/updateStudent")
  public String updateStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      return "updateStudent";
    }

    try {
      // 受講生とコース情報を保存する
      service.updateStudent(studentDetail);
      System.out.println(
          studentDetail.getStudent().getName() + "さんの受講生情報が更新されました。");
    } catch (Exception e) {
      System.out.println("受講生の更新に失敗しました: " + e.getMessage());
      model.addAttribute("errorMessage", "受講生の更新に失敗しました。リストページへ戻ります。");
      return "redirect:/studentsCourseList";
    }

    return "redirect:/studentsCourseList"; // 成功時のリダイレクト
  }
}
