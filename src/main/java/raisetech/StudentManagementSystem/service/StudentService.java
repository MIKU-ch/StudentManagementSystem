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
import raisetech.StudentManagementSystem.exception.CustomAppException;
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

  public List<Student> searchStudentList() {
    return repository.search();
  }

  public List<StudentsCourses> searchStudentsCourseList() {
    return repository.searchStudentsCourses();
  }

  public List<StudentDetail> listStudentDetails() {
    List<StudentsCourses> studentsCourses = searchStudentsCourseList();
    List<Student> students = searchStudentList();
    return converter.convertToStudentDetailList(students, studentsCourses);
  }

  public StudentDetail getStudentDetailById(int id) {
    Student student = repository.findById(id);
    if (student == null) {
      throw new CustomAppException("指定された受講生が見つかりませんでした。");
    }

    List<StudentsCourses> courses = repository.findCoursesByStudentId(id);
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setCourseList(courses != null ? courses : new ArrayList<>());

    return studentDetail;
  }

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());
    int studentId = studentDetail.getStudent().getId();
    initStudentsCourse(studentDetail, studentId);
  }

  private void initStudentsCourse(StudentDetail studentDetail, int studentId) {
    if (studentDetail.getCourseList() != null) {
      for (StudentsCourses sc : studentDetail.getCourseList()) {
        sc.setStudentId(studentId);
        repository.registerStudentsCourses(sc);
      }
    }
  }

  public void updateStudent(StudentDetail studentDetail) {
    // 更新対象の学生が存在するかチェック
    if (repository.findById(studentDetail.getStudent().getId()) == null) {
      throw new CustomAppException("更新対象の学生が存在しません");
    }

    // 学生情報の更新
    repository.updateStudent(studentDetail.getStudent());

    // 受講コース情報の更新
    if (studentDetail.getCourseList() != null) {
      for (StudentsCourses sc : studentDetail.getCourseList()) {
        sc.setStudentId(studentDetail.getStudent().getId());
        if (sc.getId() == null) {
          throw new CustomAppException("更新対象のコースIDが提供されていません");
        } else {
          repository.updateStudentsCourses(sc);
        }
      }
    }
  }
}
