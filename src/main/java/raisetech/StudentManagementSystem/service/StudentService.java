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

  /**
   * 学生の一覧を検索する
   *
   * @return 学生のリスト
   */
  public List<Student> searchStudentList() {
    return repository.search();
  }

  /**
   * 受講コースの一覧を検索する
   *
   * @return 受講コースのリスト
   */
  public List<StudentsCourses> searchStudentsCourseList() {
    return repository.searchStudentsCourses();
  }

  /**
   * 学生情報（受講コース情報を含む）を取得する
   *
   * @return 学生詳細情報のリスト
   */
  public List<StudentDetail> listStudentDetails() {
    List<StudentsCourses> studentsCourses = searchStudentsCourseList();
    List<Student> students = searchStudentList();
    return converter.convertToStudentDetailList(students, studentsCourses);
  }

  /**
   * 指定されたIDの学生詳細情報を取得する
   *
   * @param id 学生ID
   * @return 学生詳細情報
   * @throws CustomAppException 学生が見つからない場合に例外をスロー
   */
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

  /**
   * 新規の学生を登録する
   *
   * @param studentDetail 登録する学生の詳細情報
   */
  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    // 学生情報の登録
    repository.registerStudent(studentDetail.getStudent());

    // 学生IDを取得し、受講コース情報を登録
    int studentId = studentDetail.getStudent().getId();
    initStudentsCourse(studentDetail, studentId);
  }

  /**
   * 受講コース情報を初期化し、学生IDを設定して登録する
   *
   * @param studentDetail 学生詳細情報
   * @param studentId     学生ID
   */
  private void initStudentsCourse(StudentDetail studentDetail, int studentId) {
    if (studentDetail.getCourseList() != null) {
      for (StudentsCourses sc : studentDetail.getCourseList()) {
        sc.setStudentId(studentId);
        repository.registerStudentsCourses(sc);
      }
    }
  }

  /**
   * 学生情報を更新する
   *
   * @param studentDetail 更新する学生の詳細情報
   * @throws CustomAppException 学生が存在しない場合、またはコースIDが提供されていない場合に例外をスロー
   */
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

        // コースIDが提供されていない場合は例外をスロー
        if (sc.getId() == null) {
          throw new CustomAppException("更新対象のコースIDが提供されていません");
        } else {
          repository.updateStudentsCourses(sc);
        }
      }
    }
  }
}
