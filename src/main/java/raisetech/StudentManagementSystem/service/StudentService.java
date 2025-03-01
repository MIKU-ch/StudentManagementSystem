package raisetech.StudentManagementSystem.service;

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
}
