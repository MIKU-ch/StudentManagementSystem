package raisetech.StudentManagementSystem.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.repository.StudentRepository;

@Service
public class StudentService {

  private final StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  // 全ての学生を取得する
  public List<Student> searchStudentList() {
    return repository.search();
  }

  // コースを全件取得する
  public List<StudentsCourses> searchStudentsCourseList() {
    return repository.searchStudentsCourses();
  }

  // 絞り込みをする。年齢が30代の人のみを抽出する。
  public List<Student> getStudents30s() {
    return repository.search().stream()
        .filter(student -> student.getAge() >= 30 && student.getAge() < 40)
        .collect(Collectors.toList());
  }

  // Javaコースを受講している学生のみを取得する
  public List<Student> getStudentsInJavaCourse() {
    // students_courses から courseName が "Java" の studentId を取得
    List<String> javaStudentIds = repository.searchStudentsCourses().stream()
        .filter(course -> "Java".equals(course.getCourseName()))  // Javaコースに絞り込み
        .map(StudentsCourses::getStudentId)  // studentId を取得
        .collect(Collectors.toList());

    // students から該当する studentId の学生を抽出
    return repository.search().stream()
        .filter(student -> javaStudentIds.contains(student.getId()))  // studentId が一致する学生をフィルタ
        .collect(Collectors.toList());
  }
}
