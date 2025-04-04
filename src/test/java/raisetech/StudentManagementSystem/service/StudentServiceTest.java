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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

  @InjectMocks
  private StudentService sut;

  private Student createTestStudent(int id, String name) {
    Student student = new Student();
    student.setId(id);
    student.setName(name);
    return student;
  }

  private StudentsCourses createTestCourse(Integer id, int studentId, String courseName,
      LocalDate startDate, LocalDate endDate) {
    StudentsCourses course = new StudentsCourses();
    course.setId(id);
    course.setStudentId(studentId);
    course.setCourseName(courseName);
    course.setStartDateAt(startDate);
    course.setEndDateAt(endDate);
    return course;
  }

  @Test
  void コース情報も含む受講生情報の全件検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> students = new ArrayList<>();
    List<StudentsCourses> studentsCourses = new ArrayList<>();
    List<StudentDetail> expectedDetails = new ArrayList<>();

    when(repository.search()).thenReturn(students);
    when(repository.searchStudentsCourses()).thenReturn(studentsCourses);
    when(converter.convertToStudentDetailList(students, studentsCourses)).thenReturn(
        expectedDetails);

    List<StudentDetail> result = sut.listStudentDetails();

    assertSame(expectedDetails, result);
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
    Student student = createTestStudent(id, "テスト太郎");

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
    Student student = createTestStudent(1, "学生A");

    // コース情報を用意
    StudentsCourses course1 = new StudentsCourses();
    StudentsCourses course2 = new StudentsCourses();
    List<StudentsCourses> courseList = Arrays.asList(course1, course2);

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setCourseList(courseList);

    // 実行
    sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentsCourses(course1);
    verify(repository, times(1)).registerStudentsCourses(course2);

    assertEquals(1, course1.getStudentId());
    assertEquals(1, course2.getStudentId());
    assertNotNull(studentDetail.getCourseList());
  }

  @Test
  void 受講生登録_コース情報がnullの場合() {
    Student student = createTestStudent(2, "学生B");

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setCourseList(null);

    sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(student);
    verify(repository, never()).registerStudentsCourses(any(StudentsCourses.class));
  }

  @Test
  void 学生基本情報更新_正常系() {
    int id = 1;
    Student student = createTestStudent(id, "学生A");
    when(repository.findById(id)).thenReturn(student);

    sut.updateStudentBasicInfo(student);

    verify(repository, times(1)).updateStudent(student);
  }

  @Test
  void 受講コース更新_存在しないコースなら例外が発生すること() {
    int studentId = 1;
    StudentsCourses course = createTestCourse(100, studentId, "Java",
        LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
    when(repository.findCourseById(100)).thenReturn(null);

    CustomAppException exception = assertThrows(CustomAppException.class,
        () -> sut.updateStudentCourse(studentId, course));
    assertEquals("更新対象のコースが存在しません", exception.getMessage());
  }

  @Test
  void 受講コース更新_学生IDが一致しない場合は例外が発生する() {
    int studentId = 1;
    // コースは学生ID 2 のものとする
    StudentsCourses existingCourse = createTestCourse(100, 2, "Java",
        LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
    StudentsCourses courseToUpdate = createTestCourse(100, studentId, "Java Updated",
        LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
    when(repository.findCourseById(100)).thenReturn(existingCourse);

    CustomAppException exception = assertThrows(CustomAppException.class,
        () -> sut.updateStudentCourse(studentId, courseToUpdate));
    assertEquals("URL の学生IDと、更新対象のコースの学生IDが一致しません", exception.getMessage());
  }

  @Test
  void 受講コース更新_正常に更新できること() {
    int studentId = 1;
    StudentsCourses existingCourse = createTestCourse(100, studentId, "Java",
        LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
    StudentsCourses courseToUpdate = createTestCourse(100, studentId, "Java Updated",
        LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
    when(repository.findCourseById(100)).thenReturn(existingCourse);

    sut.updateStudentCourse(studentId, courseToUpdate);
    verify(repository, times(1)).updateStudentsCourses(courseToUpdate);
  }

  @Test
  void 受講コース追加_正常に追加できること() {
    int studentId = 1;
    Student student = createTestStudent(studentId, "学生A");
    when(repository.findById(studentId)).thenReturn(student);
    when(repository.findCoursesByStudentId(studentId)).thenReturn(Collections.emptyList());

    StudentsCourses course = new StudentsCourses();
    course.setCourseName("新規コース");
    course.setStartDateAt(LocalDate.of(2025, 4, 1));
    course.setEndDateAt(LocalDate.of(2025, 6, 30));

    sut.addCourseForStudent(studentId, course);
    verify(repository, times(1)).insertStudentsCourses(course);
  }
}
