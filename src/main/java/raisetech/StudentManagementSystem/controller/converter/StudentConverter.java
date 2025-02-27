package raisetech.StudentManagementSystem.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagementSystem.data.Course;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.domain.StudentDetail;

@Component
public class StudentConverter {

  public List<StudentDetail> convertToStudentDetailList(List<Student> students,
      List<StudentsCourses> studentsCourses) {
    List<StudentDetail> studentDetails = new ArrayList<>();

    for (Student student : students) {
      // 各学生に対応するコースをフィルタリングし、Course のリストを作成
      List<Course> studentCourseList = studentsCourses.stream()
          .filter(course -> course.getStudentId() == student.getId())
          .map(course -> course.getCourse())  // Course オブジェクトを取得
          .collect(Collectors.toList());

      // StudentDetail を生成
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);  // 学生情報をセット
      studentDetail.setCourses(studentCourseList);  // コース情報をセット

      studentDetails.add(studentDetail);  // StudentDetail をリストに追加
    }

    return studentDetails;  // StudentDetail のリストを返す
  }
}
