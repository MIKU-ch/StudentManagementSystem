package raisetech.StudentManagementSystem.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.domain.StudentDetail;
import raisetech.StudentManagementSystem.repository.StudentRepository;

@Service
public class StudentService {

  private final StudentRepository studentRepository;

  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  // 全学生とそのコース情報を結合して取得
  public List<StudentDetail> getStudentsWithCourses() {
    List<Student> students = studentRepository.searchStudent();
    List<StudentsCourses> courses = studentRepository.searchStudentsCourses();

    // 学生IDをキーに、StudentDetailを構築する
    Map<Integer, StudentDetail> studentDetailMap = new HashMap<>();

    // まず学生情報をセット
    for (Student student : students) {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);
      studentDetail.setStudentsCourses(new ArrayList<>()); // 空のコースリストをセット
      studentDetailMap.put(student.getId(), studentDetail);
    }

    // コース情報を対応する学生にセット
    for (StudentsCourses course : courses) {
      StudentDetail studentDetail = studentDetailMap.get(course.getStudentId());
      if (studentDetail != null) {
        studentDetail.getStudentsCourses().add(course);
      }
    }

    return new ArrayList<>(studentDetailMap.values());
  }

  // 学生の備考を更新するメソッド
  @Transactional
  public void updateStudentRemark(int id, String newRemark) {
    // 学生を取得
    Student student = studentRepository.findById(id);
    if (student != null) {
      // 備考を更新
      student.setRemark(newRemark);
      // データベースを更新
      studentRepository.updateStudent(student);
    } else {
      throw new RuntimeException("学生が見つかりません: ID = " + id);
    }
  }
}
