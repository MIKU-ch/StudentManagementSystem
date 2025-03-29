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
import raisetech.StudentManagementSystem.dto.StudentSearchCriteria;
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
   * 学生の一覧を検索する。
   *
   * @return 学生のリスト
   */
  public List<Student> searchStudentList() {
    return repository.search();
  }

  /**
   * 受講コースの一覧を検索する。
   *
   * @return 受講コースのリスト
   */
  public List<StudentsCourses> searchStudentsCourseList() {
    return repository.searchStudentsCourses();
  }

  /**
   * 学生情報（受講コース情報含む）を取得する。 ※現状は全件取得してからJava側で結合している
   *
   * @return 学生詳細情報のリスト
   */
  public List<StudentDetail> listStudentDetails() {
    List<StudentsCourses> studentsCourses = searchStudentsCourseList();
    List<Student> students = searchStudentList();
    return converter.convertToStudentDetailList(students, studentsCourses);
  }

  /**
   * 検索条件に基づいて、DB側で動的に学生情報（受講コース情報含む）を検索する。
   *
   * @param criteria 検索条件オブジェクト（例：name, region, status など）
   * @return 条件に合致する学生詳細情報のリスト
   */
  public List<StudentDetail> searchStudentDetails(StudentSearchCriteria criteria) {
    return repository.searchStudentDetails(criteria);
  }

  /**
   * 指定されたIDの学生詳細情報を取得する。
   *
   * @param id 学生ID
   * @return 学生詳細情報
   * @throws CustomAppException 指定された学生が存在しない場合にスローされる
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
   * 新規の学生を登録する。
   *
   * @param studentDetail 登録する学生の詳細情報（受講コース情報含む）
   */
  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    // 学生情報の登録
    repository.registerStudent(studentDetail.getStudent());
    // 登録後、生成された学生IDを取得し、受講コース情報を登録する
    int studentId = studentDetail.getStudent().getId();
    initStudentsCourse(studentDetail, studentId);
  }

  /**
   * 受講コース情報を初期化し、学生IDを設定して登録する。
   *
   * @param studentDetail 学生詳細情報
   * @param studentId     学生ID
   */
  private void initStudentsCourse(StudentDetail studentDetail, int studentId) {
    if (studentDetail.getCourseList() != null) {
      for (StudentsCourses sc : studentDetail.getCourseList()) {
        sc.setStudentId(studentId);
        // デフォルトでcourse_status_idを設定（KARI_APPLYに対応するID、ここでは1と仮定）
        if (sc.getCourseStatusId() == null) {
          sc.setCourseStatusId(1);
        }
        repository.registerStudentsCourses(sc);
      }
    }
  }

  /**
   * 学生の基本情報を更新する。 指定された学生IDに対応する学生情報が存在するかチェックし、存在する場合は更新する。
   *
   * @param student 更新する学生の基本情報（IDはURLから設定される）
   * @throws CustomAppException 更新対象の学生が存在しない場合にスローされる
   */
  public void updateStudentBasicInfo(Student student) {
    if (repository.findById(student.getId()) == null) {
      throw new CustomAppException("更新対象の学生が存在しません");
    }
    repository.updateStudent(student);
  }

  /**
   * 受講コース情報を更新する。 URLから取得した学生IDと、更新対象のコースIDに基づき、対象コース情報の整合性をチェックして更新する。
   *
   * @param studentId URLから取得した学生ID
   * @param course    更新する受講コース情報（IDはリクエストボディに含めない）
   * @throws CustomAppException 更新対象のコースが存在しない、または整合性に問題がある場合にスローされる
   */
  @Transactional
  public void updateStudentCourse(int studentId, StudentsCourses course) {
    StudentsCourses existingCourse = repository.findCourseById(course.getId());
    if (existingCourse == null) {
      throw new CustomAppException("更新対象のコースが存在しません");
    }
    if (!existingCourse.getStudentId().equals(studentId)) {
      throw new CustomAppException("URL の学生IDと、更新対象のコースの学生IDが一致しません");
    }
    repository.updateStudentsCourses(course);
  }

  /**
   * 学生に新しい受講コース情報を追加する。 指定された学生IDの学生が存在するかチェックし、受講コースの追加可能件数（最大3件）や開始日・終了日の条件を検証した上で登録する。
   *
   * @param studentId 更新対象の学生ID
   * @param sc        追加する受講コース情報（IDはリクエストボディに含めない）
   * @throws CustomAppException 受講生が存在しない、またはコース追加条件に合致しない場合にスローされる
   */
  @Transactional
  public void addCourseForStudent(int studentId, StudentsCourses sc) {
    Student student = repository.findById(studentId);
    if (student == null) {
      throw new CustomAppException("指定された学生が存在しません。");
    }
    List<StudentsCourses> existingCourses = repository.findCoursesByStudentId(studentId);
    if (existingCourses.size() >= 3) {
      throw new CustomAppException("受講生は最大3つのコースしか受講できません。");
    }
    if (sc.getStartDateAt() != null) {
      for (StudentsCourses existingCourse : existingCourses) {
        if (existingCourse.getEndDateAt() == null ||
            sc.getStartDateAt().isBefore(existingCourse.getEndDateAt())) {
          throw new CustomAppException(
              "前のコースが終了していないため、新しいコースを追加できません。");
        }
      }
    }
    sc.setStudentId(studentId);
    // デフォルトの course_status_id を設定（KARI_APPLYに対応するID、ここでは1と仮定）
    if (sc.getCourseStatusId() == null) {
      sc.setCourseStatusId(1);
    }
    repository.insertStudentsCourses(sc);
  }
}
