package raisetech.StudentManagementSystem.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import raisetech.StudentManagementSystem.controller.converter.StudentConverter;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.domain.StudentDetail;
import raisetech.StudentManagementSystem.service.StudentService;

@Controller
public class StudentController {

  private final StudentService service;
  private final StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentsCourseList")
  public String getStudentsList(Model model) {
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList();
    List<Student> students = service.searchStudentList();
    model.addAttribute("studentList",
        converter.convertToStudentDetailList(students, studentsCourses));
    return "studentList";
  }

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    // StudentDetail の courses プロパティは「courses」であるため、フォームでは courses[0] を参照する
    StudentDetail studentDetail = new StudentDetail();
    // 初期状態で1件分の StudentsCourses オブジェクトをリストに追加しておく（null回避のため）
    List<StudentsCourses> courses = new ArrayList<>();
    courses.add(new StudentsCourses());
    studentDetail.setCourses(courses);
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    service.registerStudent(studentDetail);
    System.out.println(
        studentDetail.getStudent().getName() + "さんが新規受講生として登録されました。");
    return "redirect:/studentsCourseList";
  }

  @GetMapping("/student/{id}")
  public String getStudent(@PathVariable int id, Model model) {
    // 指定されたIDの学生情報を取得
    StudentDetail studentDetail = service.getStudentDetailById(id);

    // もしコース情報が空なら、1件分の StudentsCourses オブジェクトを追加（null回避）
    if (studentDetail.getCourses() == null || studentDetail.getCourses().isEmpty()) {
      studentDetail.setCourses(new ArrayList<>());
      studentDetail.getCourses().add(new StudentsCourses());
    }

    model.addAttribute("studentDetail", studentDetail);
    return "updateStudent"; // 更新用の画面に変更
  }

  @PostMapping("/updateStudent")
  public String updateStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "updateStudent";
    }
    service.updateStudent(studentDetail); // 修正：insert ではなく update を呼び出す
    System.out.println(
        studentDetail.getStudent().getName() + "さんの受講生情報が更新されました。");
    return "redirect:/studentsCourseList";
  }

}
