package raisetech.StudentManagementSystem.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;
import raisetech.StudentManagementSystem.domain.CourseStatus;

@Alias("StudentsCourses")
@Schema(description = "受講生コース情報")
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

  private LocalDate endDateAt;

  private CourseStatus status;

  private Integer courseStatusId;
}
