package raisetech.StudentManagementSystem.service;

import java.util.List;
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

  public void updateStudentRemark(int id, String newRemark) {
    Student student = repository.findById(id);
    if (student != null) {
      student.setRemark(newRemark);
      repository.updateStudent(student);
    } else {
      throw new RuntimeException("生徒が見つかりませんでした。");
    }
  }

  public void updateStudentDeletionStatus(int id, boolean isDeleted) {
    Student student = repository.findById(id);
    if (student != null) {
      student.setDeleted(isDeleted);
      repository.updateStudent(student);
    } else {
      throw new RuntimeException("生徒が見つかりませんでした。");
    }
  }
}
