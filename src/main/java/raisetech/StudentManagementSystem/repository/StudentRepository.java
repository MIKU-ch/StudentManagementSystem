package raisetech.StudentManagementSystem.repository;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.domain.StudentDetail;

@Mapper
public interface StudentRepository {

  // 全ての学生情報を取得
  List<Student> search();

  // 学生情報をIDで取得
  Student findById(int id);

  // 特定の学生のコース情報を取得
  List<StudentsCourses> findCoursesByStudentId(int id);

  // 学生とコース情報を取得（INNER JOIN）
  List<StudentsCourses> searchStudentsCourses();

  // 学生情報を登録
  void registerStudent(Student student);

  // 学生のコース情報を登録
  void registerStudentsCourses(StudentsCourses sc);

  // 学生情報を更新
  void updateStudent(Student student);

  // 学生のコース情報を更新（id で特定のコースのみ更新）
  void updateStudentsCourses(StudentsCourses sc);

  // コースを追加
  void insertStudentsCourses(StudentsCourses sc);

  // 受講コースIDで受講コース情報を取得する
  StudentsCourses findCourseById(int courseId);

  // 動的に学生とコース情報を検索する（動的検索）
  List<StudentDetail> searchStudentDetails(Map<String, Object> params);
}
