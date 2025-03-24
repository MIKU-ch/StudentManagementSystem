package raisetech.StudentManagementSystem.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.domain.CourseStatus;
import raisetech.StudentManagementSystem.domain.Gender;

@MybatisTest
public class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {
    // data.sql により初期データとして 5 件登録されていることを期待
    List<Student> students = sut.search();
    assertThat(students.size()).isEqualTo(5);
  }

  @Test
  void 受講生の登録が行えること() {
    // 新規の学生データを作成（既存のデータと重複しないようにする）
    Student student = new Student();
    student.setId(null); // AUTO_INCREMENT に任せるため null
    student.setName("新規 学生");
    student.setKanaName("しんき がくせい");
    student.setNickname("NewStudent");
    student.setAge(20);
    student.setEmail("newstudent@example.com");
    student.setRegion("東京");
    student.setGender(Gender.MALE);
    student.setRemark("新規登録テスト");
    student.setIsDeleted(false);

    sut.registerStudent(student);

    List<Student> actual = sut.search();
    // 初期の5件に加え、新規登録が成功して 6 件になるはず
    assertThat(actual.size()).isEqualTo(6);

    // 登録した学生が正しく保存されているか確認
    Student inserted = sut.findById(student.getId());
    assertThat(inserted).isNotNull();
    assertThat(inserted.getName()).isEqualTo("新規 学生");
  }

  @Test
  void 学生基本情報が更新できること() {
    // 既存の学生（ID=1）を取得して更新
    Student student = sut.findById(1);
    assertThat(student).isNotNull();

    student.setName("更新された名前");
    sut.updateStudent(student); // ここは updateStudentBasicInfo が内部で呼ばれる前提

    Student updated = sut.findById(1);
    assertThat(updated.getName()).isEqualTo("更新された名前");
  }

  @Test
  void 受講コース情報が更新できること() {
    // 既存の学生（ID=1）の受講コース情報を取得
    List<StudentsCourses> courses = sut.findCoursesByStudentId(1);
    assertThat(courses).isNotEmpty();
    // 更新対象として最初のコースを取得
    StudentsCourses course = courses.get(0);
    String originalCourseName = course.getCourseName();

    course.setCourseName(originalCourseName + " Updated");

    // updateStudentsCoursesを使って更新
    sut.updateStudentsCourses(course);

    StudentsCourses updated = sut.findCourseById(course.getId());
    assertThat(updated.getCourseName()).isEqualTo(originalCourseName + " Updated");
  }

  @Test
  void コースが追加できること() {
    // 既存の学生（ID=1）の存在を確認
    Student student = sut.findById(1);
    assertThat(student).isNotNull();

    // 新規コース情報を作成
    StudentsCourses course = new StudentsCourses();
    course.setStudentId(student.getId());
    course.setCourseName("Test Course");
    course.setStartDateAt(LocalDate.of(2025, 1, 1));
    course.setEndDateAt(LocalDate.of(2025, 12, 31));
    course.setStatus(CourseStatus.valueOf("ENROLLING"));

    // コースを追加
    sut.insertStudentsCourses(course);

    // 追加後、学生に紐づくコース情報を取得し、追加されたコースが存在するか検証
    List<StudentsCourses> updatedCourses = sut.findCoursesByStudentId(student.getId());
    boolean found = updatedCourses.stream().anyMatch(c -> "Test Course".equals(c.getCourseName()));
    assertThat(found).isTrue();
  }
}
