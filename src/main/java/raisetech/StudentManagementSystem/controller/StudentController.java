package raisetech.StudentManagementSystem.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.domain.StudentDetail;
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
  public List<StudentDetail> getStudentsList() {
    // コースリストを取得
    List<StudentsCourses> studentsCourses = service.searchStudentsCourseList();
    List<Student> students = service.searchStudentList(); // 全ての学生を取得

    List<StudentDetail> studentDetails = new ArrayList<>();

    // 学生リストと学生コースリストを結合して StudentDetail オブジェクトを作成
    for (int i = 0; i < students.size(); i++) {
      Student student = students.get(i);

      List<StudentsCourses> studentCourseList = new ArrayList<>();
      for (int j = 0; j < studentsCourses.size(); j++) {
        StudentsCourses course = studentsCourses.get(j);
        if (course.getStudentId().equals(student.getId())) {
          studentCourseList.add(course);
        }
      }

      // 学生情報とコース情報を StudentDetail にセット
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student); // 学生情報をセット
      studentDetail.setStudentsCourses(studentCourseList); // 学生のコース情報をセット

      studentDetails.add(studentDetail); // リストに追加
    }

    return studentDetails;
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
