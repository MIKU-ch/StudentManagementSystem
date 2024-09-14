package raisetech.StudentManagementSystem;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StudentRepository {

  // 名前に基づいて学生情報を取得する
  @Select("SELECT * FROM student WHERE name =#{name}")
  Student searchByName(String name);

  // 学生情報を登録する
  @Insert("INSERT student values(#{name},#{age})")
  void registerStudent(String name, int age);

  // 学生の年齢を更新する
  @Update("UPDATE student SET age = #{age} WHERE name = #{name}")
  void updateStudent(String name, int age);

  // 名前に基づいて学生情報を削除する
  @Delete("DELETE FROM student WHERE name = #{name}")
  void deleteByName(String name);

  // 全ての学生情報を取得する
  @Select("SELECT * FROM student")
  List<Student> findAllStudents();
}
