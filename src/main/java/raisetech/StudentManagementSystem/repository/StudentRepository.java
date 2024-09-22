package raisetech.StudentManagementSystem.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
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
}
