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

  @Select("SELECT * FROM students")
  List<Student> searchStudent();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourses();

  @Select("SELECT * FROM students WHERE id = #{id}")
  Student findById(int id);

  @Update("UPDATE students SET name = #{name}, kana_name = #{kanaName}, nickname = #{nickname}, email = #{email}, region = #{region}, age = #{age}, gender = #{gender}, remark = #{remark}, is_deleted = #{isDeleted} WHERE id = #{id}")
  void updateStudent(Student student);

  // 学生を新規登録
  @Insert("INSERT INTO students (name, kana_name, nickname, email, region, age, gender, remark, is_deleted) VALUES (#{name}, #{kanaName}, #{nickname}, #{email}, #{region}, #{age}, #{gender}, #{remark}, #{isDeleted})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void saveStudent(Student student);

  // コースを新規登録
  @Insert("INSERT INTO students_courses (student_id, course_name, start_date_at, end_date_at) VALUES (#{studentId}, #{courseName}, #{startDateAt}, #{endDateAt})")
  void saveCourse(StudentsCourses studentsCourses);
}
