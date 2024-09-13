package raisetech.StudentManagementSystem;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementSystemApplication {

  @Autowired
  private StudentRepository repository;

  public static void main(String[] args) {
    SpringApplication.run(StudentManagementSystemApplication.class, args);
  }

  // 学生の基本情報を取得
  @GetMapping("/student")
  public String getStudent(@RequestParam String name) {
    Student student = repository.searchByName(name);
    if (student == null) {
      return "生徒情報がありません";
    }
    return student.getName() + " " + student.getAge() + "歳";
  }

  // 学生の基本情報を更新
  @PostMapping("/student")
  public void registerStudent(String name, int age) {
    repository.registerStudent(name, age);
  }

  // 学生の基本情報を追加
  @PatchMapping("/student")
  public void updateStudentName(String name, int age) {
    repository.updateStudent(name, age);
  }

  // 学生の基本情報を削除
  @DeleteMapping("/student")
  public void deleteStudentByName(@RequestParam String name) {
    repository.deleteByName(name);
  }

  // テーブルの全件表示
  @GetMapping("/students")
  public List<Student> getAllStudents() {
    return repository.findAllStudents();
  }
}
