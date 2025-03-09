package raisetech.StudentManagementSystem.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;

@Mapper
public interface StudentRepository {

  // 全ての学生情報を取得
  @Select("SELECT * FROM students ")
  List<Student> search();

  // 学生情報をIDで取得
  @Select("SELECT * FROM students WHERE id = #{id}")
  Student findById(int id);

  // 特定の学生のコース情報を取得
  @Select("SELECT * FROM students_courses WHERE student_id = #{id}")
  List<StudentsCourses> findCoursesByStudentId(int id);

  // 学生とコース情報を取得（INNER JOIN）
  @Select("""
        SELECT sc.student_id, s.name, sc.course_name, sc.start_date_at, sc.end_date_at
        FROM students_courses sc
        INNER JOIN students s ON s.id = sc.student_id
      """)
  List<StudentsCourses> searchStudentsCourses();

  // 学生情報を登録
  @Insert("""
          INSERT INTO students(name, kana_name, nickname, email, region, age, gender, remark, is_deleted)
          VALUES(#{name}, #{kanaName}, #{nickname}, #{email}, #{region}, #{age}, #{gender}, #{remark}, 0)
      """)
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudent(Student student);

  // 学生のコース情報を登録
  @Insert("""
          INSERT INTO students_courses(student_id, course_name, start_date_at, end_date_at)
          VALUES(#{studentId}, #{courseName}, #{startDateAt}, #{endDateAt})
      """)
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void registerStudentsCourses(StudentsCourses sc);

  // 学生情報を更新
  @Update("""
          UPDATE students
          SET name = #{name}, kana_name = #{kanaName}, nickname = #{nickname},
              email = #{email}, region = #{region}, age = #{age}, gender = #{gender},
              remark = #{remark}, is_deleted = #{isDeleted}
          WHERE id = #{id}
      """)
  void updateStudent(Student student);

  // 学生のコース情報を更新（id で特定のコースのみ更新するように修正）
  @Update("""
          UPDATE students_courses
          SET course_name = #{courseName}, start_date_at = #{startDateAt}, end_date_at = #{endDateAt}
          WHERE id = #{id}
      """)
  void updateStudentsCourses(StudentsCourses sc);

}
