package raisetech.StudentManagementSystem.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.domain.StudentDetail;
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

  // 指定されたIDの学生情報とコース情報を取得する
  public StudentDetail getStudentDetailById(int id) {
    // 学生情報を取得
    Student student = repository.findById(id);

    // 学生のコース情報を取得
    List<StudentsCourses> courses = repository.findCoursesByStudentId(id);

    // StudentDetail にセット
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setCourses(courses != null ? courses : new ArrayList<>());

    return studentDetail;
  }

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    // 学生情報を登録
    repository.registerStudent(studentDetail.getStudent());
    // 登録された学生のIDを取得し、各コース情報にセット
    int studentId = studentDetail.getStudent().getId();
    if (studentDetail.getCourses() != null) {
      for (StudentsCourses sc : studentDetail.getCourses()) {
        sc.setStudentId(studentId);
        repository.registerStudentsCourses(sc);
      }
    }
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    // 学生情報が存在するかチェック
    if (!repository.existsStudentById(studentDetail.getStudent().getId())) {
      throw new IllegalArgumentException("更新対象の学生が存在しません");
    }

    // 学生情報を更新
    repository.updateStudent(studentDetail.getStudent());

    // 学生IDを取得
    int studentId = studentDetail.getStudent().getId();

    // 既存のコース情報を更新
    if (studentDetail.getCourses() != null) {
      for (StudentsCourses sc : studentDetail.getCourses()) {
        sc.setStudentId(studentId);
        repository.updateStudentsCourses(sc);
      }
    }
  }
}
