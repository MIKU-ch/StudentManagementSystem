package raisetech.StudentManagementSystem.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.domain.Gender;
import raisetech.StudentManagementSystem.domain.StudentDetail;

public class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void before() {
    sut = new StudentConverter();
  }

  @Test
  void 受講生のリストと受講生コース情報のリストを渡して受講生詳細のリストが作成できること() {
    // 受講生データの作成
    Student student = createStudent();

    // 受講コースデータの作成
    StudentsCourses studentsCourses = new StudentsCourses();
    studentsCourses.setId(1);
    studentsCourses.setStudentId(1);
    studentsCourses.setCourseName("新しいコース");
    studentsCourses.setStartDateAt(LocalDate.parse("2025-03-01"));
    studentsCourses.setEndDateAt(LocalDate.parse("2025-06-01"));

    List<Student> students = List.of(student);
    List<StudentsCourses> courses = List.of(studentsCourses);

    // メソッド実行
    List<StudentDetail> result = sut.convertToStudentDetailList(students, courses);

    // 検証
    assertThat(result).hasSize(1);
    StudentDetail studentDetail = result.get(0);
    assertThat(studentDetail.getStudent()).isEqualTo(student);
    assertThat(studentDetail.getCourseList()).containsExactly(studentsCourses);
  }

  @Test
  void 受講生のリストと受講生コース情報のリストを渡した時に紐づかない受講生コース情報は除外されること() {
    // 受講生データの作成（ID:1 の受講生）
    Student student = createStudent();

    // 受講生と紐づくコース情報（studentId = 1）
    StudentsCourses matchingCourse = new StudentsCourses();
    matchingCourse.setId(1);
    matchingCourse.setStudentId(1);
    matchingCourse.setCourseName("新しいコース");
    matchingCourse.setStartDateAt(LocalDate.parse("2025-03-01"));
    matchingCourse.setEndDateAt(LocalDate.parse("2025-06-01"));

    // 受講生と紐づかないコース情報（studentId = 1000）
    StudentsCourses nonMatchingCourse = new StudentsCourses();
    nonMatchingCourse.setId(1000);
    nonMatchingCourse.setStudentId(1000);
    nonMatchingCourse.setCourseName("マーケティング");
    nonMatchingCourse.setStartDateAt(LocalDate.parse("2025-03-01"));
    nonMatchingCourse.setEndDateAt(null);

    List<Student> students = List.of(student);
    List<StudentsCourses> courses = List.of(matchingCourse, nonMatchingCourse);

    // メソッド実行
    List<StudentDetail> result = sut.convertToStudentDetailList(students, courses);

    // 検証：受講生詳細は1件で、受講生と紐づくコース情報のみが含まれること
    assertThat(result).hasSize(1);
    StudentDetail studentDetail = result.get(0);
    assertThat(studentDetail.getStudent()).isEqualTo(student);
    assertThat(studentDetail.getCourseList()).containsExactly(matchingCourse);
  }

  private static Student createStudent() {
    Student student = new Student();
    student.setId(1);
    student.setName("室伏 広治");
    student.setKanaName("むろぶし こうじ");
    student.setNickname("Koji Murobushi");
    student.setAge(78);
    student.setEmail("koji.muro@example.com");
    student.setRegion("川");
    student.setGender(Gender.FEMALE);
    student.setRemark("");
    student.setIsDeleted(false);
    return student;
  }
}
