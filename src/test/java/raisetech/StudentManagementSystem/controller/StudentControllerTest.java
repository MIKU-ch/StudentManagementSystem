package raisetech.StudentManagementSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.data.StudentsCourses;
import raisetech.StudentManagementSystem.domain.Gender;
import raisetech.StudentManagementSystem.domain.StudentDetail;
import raisetech.StudentManagementSystem.service.StudentService;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private StudentService service;

  @Test
  void 正常系_受講生詳細一覧を検索するとレスポンスに学生詳細情報が含まれること() throws Exception {
    StudentDetail detail = new StudentDetail();
    Student student = new Student();
    student.setId(1);
    student.setName("テスト太郎");
    detail.setStudent(student);
    detail.setCourseList(Collections.emptyList());

    Map<String, Object> expectedResponse = new HashMap<>();
    expectedResponse.put("students", Collections.singletonList(detail));

    when(service.listStudentDetails()).thenReturn(Collections.singletonList(detail));

    mockMvc.perform(get("/students"))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

    verify(service, times(1)).listStudentDetails();
  }

  @Test
  void 正常系_受講生登録するとレスポンスにメッセージと学生詳細情報が含まれること()
      throws Exception {
    Student student = new Student();
    student.setName("テスト太郎");
    student.setKanaName("テストタロウ");
    student.setAge(20);
    student.setEmail("test@example.com");
    student.setRegion("Tokyo");
    student.setGender(Gender.MALE);

    StudentDetail detail = new StudentDetail();
    detail.setStudent(student);

    doNothing().when(service).registerStudent(any(StudentDetail.class));

    Map<String, Object> expectedResponse = new HashMap<>();
    expectedResponse.put("message", "テスト太郎さんが新規受講生として登録されました。");
    expectedResponse.put("studentDetail", detail);

    mockMvc.perform(post("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(detail)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("テスト太郎さんが新規受講生として登録されました。"))
        .andExpect(jsonPath("$.studentDetail.student.name").value("テスト太郎"));

    verify(service, times(1)).registerStudent(any(StudentDetail.class));
  }

  @Test
  void 正常系_学生基本情報更新時に正しいレスポンスが返ること() throws Exception {
    int id = 1;
    Student student = new Student();
    student.setId(id);
    student.setName("更新太郎");
    student.setKanaName("更新タロウ");
    student.setAge(25);
    student.setEmail("update@example.com");
    student.setRegion("Osaka");
    student.setGender(Gender.MALE);

    doNothing().when(service).updateStudentBasicInfo(any(Student.class));

    Map<String, Object> expectedResponse = new HashMap<>();
    expectedResponse.put("message", "学生基本情報を更新しました");

    mockMvc.perform(put("/students/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(student)))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

    verify(service, times(1)).updateStudentBasicInfo(argThat(s ->
        s.getId() == id &&
            "更新太郎".equals(s.getName()) &&
            "更新タロウ".equals(s.getKanaName()) &&
            s.getAge() == 25 &&
            "update@example.com".equals(s.getEmail()) &&
            "Osaka".equals(s.getRegion()) &&
            s.getGender() == Gender.MALE
    ));
  }

  @Test
  void 正常系_受講コース情報更新時に正しいJSONレスポンスが返ること() throws Exception {
    int studentId = 1;
    int courseId = 10;
    StudentsCourses course = new StudentsCourses();
    // BodyからはIDは含めず、URLのIDを設定する
    course.setCourseName("新しいコース名");
    course.setStartDateAt(LocalDate.of(2024, 4, 1));
    course.setEndDateAt(LocalDate.of(2025, 6, 30));

    doNothing().when(service).updateStudentCourse(anyInt(), any(StudentsCourses.class));

    Map<String, Object> expectedResponse = new HashMap<>();
    expectedResponse.put("message", "受講コース情報を更新しました");

    mockMvc.perform(put("/students/{studentId}/courses/{courseId}", studentId, courseId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(course)))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

    verify(service, times(1)).updateStudentCourse(eq(studentId), argThat(updatedCourse ->
        updatedCourse.getId() == courseId &&
            updatedCourse.getStudentId() == studentId &&
            "新しいコース名".equals(updatedCourse.getCourseName())
    ));
  }

  @Test
  void 正常系_受講生にコース追加時に正しいレスポンスが返ること() throws Exception {
    int studentId = 1;
    StudentsCourses course = new StudentsCourses();
    course.setCourseName("新しいコース");
    course.setStartDateAt(LocalDate.of(2024, 4, 1));
    course.setEndDateAt(LocalDate.of(2025, 6, 30));

    doNothing().when(service).addCourseForStudent(anyInt(), any(StudentsCourses.class));

    Map<String, Object> expectedResponse = new HashMap<>();
    expectedResponse.put("message", "新しいコースが追加されました。");

    mockMvc.perform(post("/students/{id}/courses", studentId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(course)))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

    verify(service, times(1)).addCourseForStudent(eq(studentId), any(StudentsCourses.class));
  }

  @Test
  void 異常系_不正なIDで受講生詳細取得時にバリデーションエラーが返ること() throws Exception {
    int invalidId = 0; // @Min(1) 違反
    mockMvc.perform(get("/students/{id}", invalidId))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("バリデーションエラー"))
        .andExpect(jsonPath("$.fieldErrors").exists());
    verify(service, times(0)).getStudentDetailById(anyInt());
  }

  @Test
  void 異常系_受講生基本情報更新時にバリデーションエラーが返ること() throws Exception {
    int id = 1;
    // 空のStudentオブジェクトでバリデーションエラーを発生させる（必須項目のエラーなど）と想定
    Student student = new Student();
    mockMvc.perform(put("/students/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(student)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("バリデーションエラー"))
        .andExpect(jsonPath("$.fieldErrors").exists());
    verify(service, times(0)).updateStudentBasicInfo(any(Student.class));
  }
}
