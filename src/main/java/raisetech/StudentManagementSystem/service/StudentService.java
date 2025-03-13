package raisetech.StudentManagementSystem.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagementSystem.controller.converter.StudentConverter;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.domain.StudentDetail;
import raisetech.StudentManagementSystem.repository.StudentRepository;

@Service
public class StudentService {

  private final StudentRepository repository;
  private final StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  // 全ての学生情報を取得
  public List<Student> searchStudentList() {
    return repository.search();
  }

  // コースを全件取得
  public List<StudentsCourses> searchStudentsCourseList() {
    return repository.searchStudentsCourses();
  }

  // 学生一覧（受講コース情報含む）を取得する
  public List<StudentDetail> listStudentDetails() {
    List<StudentsCourses> studentsCourses = searchStudentsCourseList();
    List<Student> students = searchStudentList();
    return converter.convertToStudentDetailList(students, studentsCourses);
  }

  // 指定されたIDの学生情報とコース情報を取得する
  public StudentDetail getStudentDetailById(int id) {
    Student student = repository.findById(id);
    List<StudentsCourses> courses = repository.findCoursesByStudentId(id);

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setCourseList(courses != null ? courses : new ArrayList<>());

    return studentDetail;
  }

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    // 学生情報を登録
    repository.registerStudent(studentDetail.getStudent());
    // 登録された学生のIDを各コースにセット
    int studentId = studentDetail.getStudent().getId();
    initStudentsCourse(studentDetail, studentId);
  }

  // 受講生コース情報を登録する際の初期情報を設定する
  private void initStudentsCourse(StudentDetail studentDetail, int studentId) {
    if (studentDetail.getCourseList() != null) {
      for (StudentsCourses sc : studentDetail.getCourseList()) {
        sc.setStudentId(studentId);
        repository.registerStudentsCourses(sc);
      }
    }
  }

  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());

    if (studentDetail.getCourseList() != null) {
      for (StudentsCourses sc : studentDetail.getCourseList()) {
        sc.setStudentId(studentDetail.getStudent().getId()); // IDを設定
        if (sc.getId() == null) {
          throw new IllegalArgumentException("更新対象のコースIDが提供されていません");
        } else {
          repository.updateStudentsCourses(sc);
        }
      }
    }
  }
}
