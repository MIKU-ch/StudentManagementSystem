package raisetech.StudentManagementSystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagementSystem.controller.converter.StudentConverter;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.domain.StudentDetail;
import raisetech.StudentManagementSystem.exception.CustomAppException;
import raisetech.StudentManagementSystem.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void コース情報も含む受講生情報の全件検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> students = new ArrayList<>();
    List<StudentsCourses> studentsCourses = new ArrayList<>();
    when(repository.search()).thenReturn(students);
    when(repository.searchStudentsCourses()).thenReturn(studentsCourses);

    sut.listStudentDetails();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentsCourses();
    verify(converter, times(1)).convertToStudentDetailList(students, studentsCourses);
  }

  @Test
  void 学生一覧検索_リポジトリのsearchメソッドが呼び出されること() {
    List<Student> students = new ArrayList<>();
    when(repository.search()).thenReturn(students);

    List<Student> result = sut.searchStudentList();
    assertSame(students, result);
    verify(repository, times(1)).search();
  }

  @Test
  void 受講コース一覧検索_リポジトリのsearchStudentsCoursesメソッドが呼び出されること() {
    List<StudentsCourses> courses = new ArrayList<>();
    when(repository.searchStudentsCourses()).thenReturn(courses);

    List<StudentsCourses> result = sut.searchStudentsCourseList();
    assertSame(courses, result);
    verify(repository, times(1)).searchStudentsCourses();
  }

  @Test
  void 指定IDの学生詳細取得_正常系() {
    int id = 1;
    Student student = new Student();
    student.setId(id);
    student.setName("テスト太郎");

    List<StudentsCourses> courses = new ArrayList<>();
    StudentsCourses course = new StudentsCourses();
    courses.add(course);

    when(repository.findById(id)).thenReturn(student);
    when(repository.findCoursesByStudentId(id)).thenReturn(courses);

    StudentDetail result = sut.getStudentDetailById(id);
    assertNotNull(result);
    assertEquals(student, result.getStudent());
    assertEquals(courses, result.getCourseList());

    verify(repository, times(1)).findById(id);
    verify(repository, times(1)).findCoursesByStudentId(id);
  }

  @Test
  void 指定IDの学生詳細取得_学生が存在しない場合は例外がスローされること() {
    int id = 1;
    when(repository.findById(id)).thenReturn(null);

    CustomAppException exception = assertThrows(CustomAppException.class,
        () -> sut.getStudentDetailById(id));
    assertEquals("指定された受講生が見つかりませんでした。", exception.getMessage());
  }

  @Test
  void 受講生登録_コース情報が存在する場合() {
    Student student = new Student();
    student.setId(1);
    student.setName("学生A");

    // コース情報を用意
    StudentsCourses course1 = new StudentsCourses();
    StudentsCourses course2 = new StudentsCourses();
    List<StudentsCourses> courseList = Arrays.asList(course1, course2);

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setCourseList(courseList);

    // 実行
    sut.registerStudent(studentDetail);

    // 学生情報の登録が呼ばれていること
    verify(repository, times(1)).registerStudent(student);
    // 各コースに学生IDがセットされ、登録処理が呼ばれていること
    verify(repository, times(1)).registerStudentsCourses(course1);
    verify(repository, times(1)).registerStudentsCourses(course2);
    assertEquals(1, course1.getStudentId());
    assertEquals(1, course2.getStudentId());
  }

  @Test
  void 受講生登録_コース情報がnullの場合() {
    Student student = new Student();
    student.setId(2);
    student.setName("学生B");

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setCourseList(null);

    sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(student);
    // コース情報がnullの場合、コース登録は呼ばれない
    verify(repository, never()).registerStudentsCourses(any(StudentsCourses.class));
  }

  @Test
  void 学生情報更新_正常系() {
    int id = 1;
    Student student = new Student();
    student.setId(id);
    student.setName("学生A");
    when(repository.findById(id)).thenReturn(student);

    StudentsCourses course = new StudentsCourses();
    course.setId(100); // 有効なコースIDを設定
    List<StudentsCourses> courseList = Collections.singletonList(course);

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setCourseList(courseList);

    sut.updateStudent(studentDetail);

    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(1)).updateStudentsCourses(course);
    // 学生IDが各コースにセットされていること
    assertEquals(id, course.getStudentId());
  }

  @Test
  void 学生情報更新_学生が存在しない場合は例外がスローされること() {
    int id = 1;
    Student student = new Student();
    student.setId(id);
    student.setName("学生A");
    when(repository.findById(id)).thenReturn(null);

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setCourseList(null);

    CustomAppException exception = assertThrows(CustomAppException.class,
        () -> sut.updateStudent(studentDetail));
    assertEquals("更新対象の学生が存在しません", exception.getMessage());
  }

  @Test
  void 学生情報更新_コースIDがnullの場合は例外がスローされること() {
    int id = 1;
    Student student = new Student();
    student.setId(id);
    student.setName("学生A");
    when(repository.findById(id)).thenReturn(student);

    // コース情報はあるが、コースIDがnull
    StudentsCourses course = new StudentsCourses();
    List<StudentsCourses> courseList = Collections.singletonList(course);

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setCourseList(courseList);

    CustomAppException exception = assertThrows(CustomAppException.class,
        () -> sut.updateStudent(studentDetail));
    assertEquals("更新対象のコースIDが提供されていません", exception.getMessage());
  }
}
