package raisetech.StudentManagementSystem.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.service.StudentService;

@RestController
public class StudentController {

  private final StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  // 全ての学生を表示
  @GetMapping("/students")
  public List<Student> getAllStudents() {
    return service.searchStudentList();
  }

  // コースを全件表示
  @GetMapping("/studentsCourseList")
  public List<StudentsCourses> getStudentsCourseList() {
    return service.searchStudentsCourseList();
  }

  // 30代の学生のみを表示
  @GetMapping("/students/30s")
  public List<Student> getStudentsInTheir30s() {
    return service.getStudents30s();
  }

  // Javaコースを受講している学生のみを表示
  @GetMapping("/students/java")
  public List<Student> getStudentsInJavaCourse() {
    return service.getStudentsInJavaCourse();
  }
}
