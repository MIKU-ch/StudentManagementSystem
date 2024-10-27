package raisetech.StudentManagementSystem.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.domain.StudentDetail;
import raisetech.StudentManagementSystem.repository.StudentRepository;

@Service
public class StudentService {

  private final StudentRepository studentRepository;

  @Autowired
  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  public StudentDetail searchStudentById(int id) {
    Student student = studentRepository.searchStudentById(id);
    List<StudentsCourses> studentsCourses = studentRepository.searchStudentsCourses(
        student.getId());
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentsCourses(studentsCourses);
    return studentDetail;
  }

  // 全学生とそのコース情報を結合して取得
  public List<StudentDetail> getStudentsWithCourses() {
    List<Student> students = studentRepository.searchStudent();
    List<StudentsCourses> courses = studentRepository.searchStudentsCoursesList();

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

  @Transactional
  public void saveStudentDetail(StudentDetail studentDetail) {
    // 学生情報を保存し、生成されたIDを取得
    studentRepository.saveStudent(studentDetail.getStudent());

    // 学生IDが生成された後に各コースにIDを設定
    for (StudentsCourses course : studentDetail.getStudentsCourses()) {
      if (studentDetail.getStudent().getId() != null) {
        // addCourseメソッドを使って自動的にstudentIdを設定
        studentDetail.getStudent().addCourse(course);
        studentRepository.saveCourse(course);
      } else {
        throw new RuntimeException("Student ID is null. The course cannot be saved.");
      }
    }
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    // まず、学生情報を保存
    studentRepository.updateStudent(studentDetail.getStudent());

    for (StudentsCourses course : studentDetail.getStudentsCourses()) {
      // addCourseメソッドを使用して、studentIdを設定
      studentDetail.getStudent().addCourse(course);

      if (course.getId() == null) {
        studentRepository.saveCourse(course);  // 新規コースを挿入
      } else {
        studentRepository.updateCourse(course);  // 既存コースを更新
      }
    }
  }

}
