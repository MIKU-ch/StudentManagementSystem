package raisetech.StudentManagementSystem.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagementSystem.data.Student;
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
    // 新規の学生データを作成（既存のデータと重複しないように名前・メールなどを変更）
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

    // さらに、登録した学生が正しく保存されているか確認
    Student inserted = sut.findById(student.getId());
    assertThat(inserted).isNotNull();
    assertThat(inserted.getName()).isEqualTo("新規 学生");
  }

  @Test
  void 受講生の更新が行えること() {
    // 既存の学生データ（ID=1 のデータ）を取得して更新
    Student student = sut.findById(1);
    assertThat(student).isNotNull();

    student.setName("更新された名前");
    sut.updateStudent(student);

    Student updated = sut.findById(1);
    assertThat(updated.getName()).isEqualTo("更新された名前");
  }
}
