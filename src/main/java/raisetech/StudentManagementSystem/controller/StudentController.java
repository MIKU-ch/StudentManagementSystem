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
  private final StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  //学生一覧（受講コース情報含む）を取得して、一覧画面（studentList）を返す
  @GetMapping
  public String listStudents(Model model) {
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList();
    List<Student> students = service.searchStudentList();
    model.addAttribute("studentList",
        converter.convertToStudentDetailList(students, studentsCourses));
    return "studentList";
  }

  //新規学生登録フォームを表示する
  @GetMapping("/form")
  public String showRegistrationForm(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    // coursesプロパティはフォーム上で courses[0] として参照されるため、初期状態で1件分追加（null回避）
    List<StudentsCourses> courses = new ArrayList<>();
    courses.add(new StudentsCourses());
    studentDetail.setCourses(courses);
    model.addAttribute("studentDetail", studentDetail);
    return "registerStudent";
  }

  //新規学生を登録する
  @PostMapping
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    service.registerStudent(studentDetail);
    System.out.println(
        studentDetail.getStudent().getName() + "さんが新規受講生として登録されました。");
    return "redirect:/students";
  }

  //指定されたIDの学生の更新フォームを表示する
  @GetMapping("/{id}/form")
  public String showUpdateForm(@PathVariable int id, Model model) {
    StudentDetail studentDetail = service.getStudentDetailById(id);
    // コース情報が空の場合、1件分追加してnull回避
    if (studentDetail.getCourses() == null || studentDetail.getCourses().isEmpty()) {
      List<StudentsCourses> courses = new ArrayList<>();
      courses.add(new StudentsCourses());
      studentDetail.setCourses(courses);
    }
    model.addAttribute("studentDetail", studentDetail);
    return "updateStudent";
  }

  //指定された学生の情報を更新する
  @PostMapping("/{id}")
  public String updateStudent(@PathVariable int id, @ModelAttribute StudentDetail studentDetail,
      BindingResult result) {
    if (result.hasErrors()) {
      return "updateStudent";
    }
    service.updateStudent(studentDetail);
    System.out.println(studentDetail.getStudent().getName() + "さんの受講生情報が更新されました。");
    return "redirect:/students";
  }
}
