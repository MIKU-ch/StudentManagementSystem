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

  // コースを全件取得する
  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourses();

  // 学生情報をIDで取得する
  @Select("SELECT * FROM students WHERE id = #{id}")
  Student findById(int id);

  // 学生情報を更新する
  @Update("UPDATE students SET name = #{name}, kana_name = #{kanaName}, nickname = #{nickname}, email = #{email}, region = #{region}, age = #{age}, gender = #{gender}, remark = #{remark}, is_deleted = #{isDeleted} WHERE id = #{id}")
  void updateStudent(Student student);
}
