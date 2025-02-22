package raisetech.StudentManagementSystem.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;

@Mapper
public interface StudentRepository {

  // 全ての学生情報を取得する
  @Select("SELECT * FROM students")
  List<Student> search();

  // 学生情報をIDで取得する
  @Select("SELECT * FROM students WHERE id = #{id}")
  Student findById(int id);

  // 学生のコース情報を取得（INNER JOIN）
  @Select("""
        SELECT sc.id, sc.student_id, sc.start_date_at, sc.end_date_at, sc.course_id, c.course_name
        FROM students_courses sc
        INNER JOIN courses c ON sc.course_id = c.id
      """)
  List<StudentsCourses> searchStudentsCourses();

  // 学生情報を更新する
  @Update("UPDATE students SET name = #{name}, kana_name = #{kanaName}, nickname = #{nickname}, email = #{email}, region = #{region}, age = #{age}, gender = #{gender}, remark = #{remark}, is_deleted = #{isDeleted} WHERE id = #{id}")
  void updateStudent(Student student);
}
