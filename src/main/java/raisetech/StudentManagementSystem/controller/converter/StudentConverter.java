package raisetech.StudentManagementSystem.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.domain.StudentDetail;

@Component
public class StudentConverter {

  // Student と StudentsCourses のリストを元に、StudentDetail のリストを作成
  public List<StudentDetail> convertToStudentDetailList(List<Student> students,
      List<StudentsCourses> studentsCourses) {
    List<StudentDetail> studentDetails = new ArrayList<>();

    for (Student student : students) {
      // 各学生に対応するコースをフィルタリング
      List<StudentsCourses> studentCourseList = studentsCourses.stream()
          .filter(course -> course.getStudentId() == student.getId())
          .collect(Collectors.toList());

      // StudentDetail を生成
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);
      studentDetail.setCourseList(studentCourseList);

      studentDetails.add(studentDetail);
    }

    return studentDetails;
  }
}
