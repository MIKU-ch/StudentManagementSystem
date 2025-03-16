package raisetech.StudentManagementSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
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
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.StudentManagementSystem.data.Student;
import raisetech.StudentManagementSystem.domain.Gender;
import raisetech.StudentManagementSystem.domain.StudentDetail;
import raisetech.StudentManagementSystem.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private StudentService service;

  @Test
  void 受講生詳細の一覧検索ができて空のリストが返ってくること() throws Exception {
    // モックの挙動を定義
    when(service.listStudentDetails()).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/students"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));  // 空のリストは "[]" として返される

    verify(service, times(1)).listStudentDetails();
  }

  @Test
  void 受講生登録が正常にできること() throws Exception {
    // テスト用データの作成（必須項目をすべてセット）
    Student student = new Student();
    student.setName("テスト太郎");
    student.setKanaName("テストタロウ");
    student.setAge(20);
    student.setEmail("test@example.com");
    student.setRegion("Tokyo");
    // Gender が enum の場合、適切な定数を設定
    student.setGender(Gender.MALE);

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);

    // POSTリクエストを実施
    mockMvc.perform(post("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(studentDetail)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("テスト太郎さんが新規受講生として登録されました。"))
        .andExpect(jsonPath("$.studentDetail.student.name").value("テスト太郎"));

    verify(service, times(1)).registerStudent(any(StudentDetail.class));
  }

  @Test
  void 学生情報取得ができること() throws Exception {
    int id = 1;
    Student student = new Student();
    student.setId(id);
    student.setName("テスト花子");
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);

    when(service.getStudentDetailById(id)).thenReturn(studentDetail);

    mockMvc.perform(get("/students/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.student.name").value("テスト花子"));

    verify(service, times(1)).getStudentDetailById(id);
  }

  @Test
  void 学生情報更新が正常にできること() throws Exception {
    int id = 1;
    Student student = new Student();
    // 更新用データ：必須項目をすべてセット
    student.setName("更新太郎");
    student.setKanaName("更新タロウ");
    student.setAge(25);
    student.setEmail("update@example.com");
    student.setRegion("Osaka");
    student.setGender(Gender.MALE);  // Gender が enum の場合

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);

    mockMvc.perform(put("/students/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(studentDetail)))
        .andExpect(status().isOk())
        .andExpect(content().string("更新太郎さんの受講生情報が更新されました。"));

    // コントローラー内部でリクエストパスの id をセットしているため、引数検証を実施
    verify(service, times(1)).updateStudent(argThat(detail ->
        detail.getStudent() != null &&
            detail.getStudent().getId() != null &&
            detail.getStudent().getId().intValue() == id &&
            "更新太郎".equals(detail.getStudent().getName())
    ));
  }
}
