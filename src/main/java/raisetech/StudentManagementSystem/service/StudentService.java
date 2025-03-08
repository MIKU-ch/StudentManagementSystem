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
    // 学生情報を更新
    repository.updateStudent(studentDetail.getStudent());

    // コース情報の更新
    if (studentDetail.getCourses() != null) {
      for (StudentsCourses sc : studentDetail.getCourses()) {
        // もしIDが未設定なら、DBから既存のコース情報を取得してIDをセットするなどの処理を行う
        if (sc.getId() == null) {
          List<StudentsCourses> existingCourses = repository.findCoursesByStudentId(
              studentDetail.getStudent().getId());
          for (StudentsCourses existing : existingCourses) {
            if (existing.getCourseName().equals(sc.getCourseName())) {
              sc.setId(existing.getId());
              break;
            }
          }
        }

        if (sc.getId() != null) {
          repository.updateStudentsCourses(sc);
        } else {
          throw new IllegalArgumentException("更新対象のコースが見つかりません");
        }
      }
    }
  }
}
