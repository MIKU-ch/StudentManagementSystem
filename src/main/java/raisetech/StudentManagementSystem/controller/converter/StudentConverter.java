package raisetech.StudentManagementSystem.controller.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.domain.StudentDetail;

@Component
public class StudentConverter {

  /**
   * Student と StudentsCourses のリストを元に、StudentDetail のリストを作成する。
   * <p>
   * 各学生IDごとにStudentsCoursesをグループ化して、効率よく変換を行います。
   *
   * @param students        学生情報のリスト（nullの場合は空リストとみなす）
   * @param studentsCourses 受講コース情報のリスト（nullの場合は空リストとみなす）
   * @return StudentDetail のリスト
   */
  public List<StudentDetail> convertToStudentDetailList(List<Student> students,
      List<StudentsCourses> studentsCourses) {
    if (students == null) {
      students = Collections.emptyList();
    }
    if (studentsCourses == null) {
      studentsCourses = Collections.emptyList();
    }

    // 受講コース情報を学生IDでグループ化する（studentId が null でないものだけ）
    Map<Integer, List<StudentsCourses>> courseMap = studentsCourses.stream()
        .filter(sc -> sc.getStudentId() != null)
        .collect(Collectors.groupingBy(StudentsCourses::getStudentId));

    // 各学生に対応するコースリストを設定してStudentDetailを生成
    List<StudentDetail> studentDetails = new ArrayList<>();
    for (Student student : students) {
      if (student == null) {
        continue; // 学生情報が null の場合はスキップ
      }
      StudentDetail detail = new StudentDetail();
      detail.setStudent(student);
      // グループ化されたマップから学生IDに対応するコースリストを取得
      List<StudentsCourses> courseList = courseMap.getOrDefault(student.getId(), new ArrayList<>());
      detail.setCourseList(courseList);
      studentDetails.add(detail);
    }
    return studentDetails;
  }
}
