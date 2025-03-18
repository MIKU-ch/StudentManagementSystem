package raisetech.StudentManagementSystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
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
import raisetech.StudentManagementSystem.exception.CustomAppException;
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
  void 正常系_受講生詳細一覧を検索すると空のリストが返ること() throws Exception {
    when(service.listStudentDetails()).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/students"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(service, times(1)).listStudentDetails();
  }
  
  @Test
  void 正常系_受講生詳細を登録すると入力データがそのままレスポンスに含まれること()
      throws Exception {
    // リクエストデータを適切に構築（入力チェックも兼ねる）
    // ※本来は、登録後にDBから最新のデータが返るが、モック化しているため、入力データがそのままレスポンスに反映される
    Student student = new Student();
    student.setName("テスト太郎");
    student.setKanaName("テストタロウ");
    student.setAge(20);
    student.setEmail("test@example.com");
    student.setRegion("Tokyo");
    student.setGender(Gender.MALE);

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);

    // サービス層はvoidメソッドのため、戻り値の設定は不要
    mockMvc.perform(post("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(studentDetail)))
        .andExpect(status().isOk())
        // 入力された名前に基づくメッセージが返ることを検証
        .andExpect(jsonPath("$.message").value("テスト太郎さんが新規受講生として登録されました。"))
        // リクエスト時のstudent情報がレスポンスにエコーされることを検証
        .andExpect(jsonPath("$.studentDetail.student.name").value("テスト太郎"));

    // バリデーションも兼ねているため、サービス層の登録処理が実行されたことを検証
    verify(service, times(1)).registerStudent(any(StudentDetail.class));
  }

  @Test
  void 正常系_受講生詳細を更新すると成功メッセージが返ること() throws Exception {
    int id = 1;
    Student student = new Student();
    student.setName("更新太郎");
    student.setKanaName("更新タロウ");
    student.setAge(25);
    student.setEmail("update@example.com");
    student.setRegion("Osaka");
    student.setGender(Gender.MALE);
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);

    // voidメソッドなので、doNothing()でモックの挙動を設定
    doNothing().when(service).updateStudent(any(StudentDetail.class));

    mockMvc.perform(put("/students/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(studentDetail)))
        .andExpect(status().isOk())
        .andExpect(content().string("更新太郎さんの受講生情報が更新されました。"));

    verify(service, times(1)).updateStudent(argThat(detail ->
        detail.getStudent() != null &&
            detail.getStudent().getId() != null &&
            detail.getStudent().getId().intValue() == id &&
            "更新太郎".equals(detail.getStudent().getName())
    ));
  }

  @Test
  void 異常系_受講生登録時にバリデーションエラーが発生するとエラーメッセージが返ること()
      throws Exception {
    // 必須項目が不足している不正なデータを作成（例：nameやkanaNameが未設定）
    Student student = new Student();
    // student.setName(null);  // ※nullや空文字の場合、@NotNullの制約でエラーとなる前提
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);

    mockMvc.perform(post("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(studentDetail)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("バリデーションエラー"))
        .andExpect(jsonPath("$.fieldErrors").exists());

    // バリデーションエラーの場合、サービス層は呼ばれない
    verify(service, times(0)).registerStudent(any(StudentDetail.class));
  }

  @Test
  void 異常系_不正なIDで受講生詳細を取得するとバリデーションエラーが発生すること()
      throws Exception {
    int invalidId = 0;  // @Min(1) に違反する値
    mockMvc.perform(get("/students/{id}", invalidId))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("バリデーションエラー"))
        .andExpect(jsonPath("$.fieldErrors").exists());

    // バリデーションエラーの場合、サービス層は呼ばれない
    verify(service, times(0)).getStudentDetailById(anyInt());
  }

  @Test
  void 異常系_受講生詳細を更新する際にバリデーションエラーが発生するとエラーメッセージが返ること()
      throws Exception {
    int id = 1;
    // 不正な更新データ（必須項目が不足）
    Student student = new Student();
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);

    mockMvc.perform(put("/students/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(studentDetail)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("バリデーションエラー"))
        .andExpect(jsonPath("$.fieldErrors").exists());

    verify(service, times(0)).updateStudent(any(StudentDetail.class));
  }

  @Test
  void 異常系_存在しない受講生IDを検索するとCustomAppExceptionが発生しエラーレスポンスが返ること()
      throws Exception {
    int id = 999; // 存在しないIDなど
    when(service.getStudentDetailById(id))
        .thenThrow(new CustomAppException("学生情報が見つかりません。"));

    mockMvc.perform(get("/students/{id}", id))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("エラーメッセージ"))
        .andExpect(jsonPath("$.message").value("学生情報が見つかりません。"));

    verify(service, times(1)).getStudentDetailById(id);
  }
}
