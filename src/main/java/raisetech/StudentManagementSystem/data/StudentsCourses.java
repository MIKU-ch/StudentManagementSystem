package raisetech.StudentManagementSystem.data;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentsCourses {

  private Integer id;
  private Integer studentId;

  @NotNull(message = "コース名は必須です")
  @Size(min = 1, max = 100, message = "コース名は1文字以上100文字以下で入力してください")
  private String courseName;

  @NotNull(message = "開始日は必須です")
  @PastOrPresent(message = "開始日は過去または今日の日付である必要があります")
  private LocalDate startDateAt;

  @FutureOrPresent(message = "終了日は今日以降の日付を指定してください")
  private LocalDate endDateAt;
}
