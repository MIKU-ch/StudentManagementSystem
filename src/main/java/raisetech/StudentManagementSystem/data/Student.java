package raisetech.StudentManagementSystem.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import raisetech.StudentManagementSystem.domain.Gender;

@Getter
@Setter
public class Student {

  private Integer id;

  @NotNull(message = "名前は必須です")
  @Size(min = 1, max = 50, message = "名前は1文字以上50文字以下で入力してください")
  private String name;

  @NotNull(message = "ふりがなは必須です")
  @Size(min = 1, max = 50, message = "ふりがなは1文字以上50文字以下で入力してください")
  private String kanaName;

  private String nickname;

  @NotNull(message = "メールアドレスは必須です")
  @Email(message = "正しいメールアドレスを入力してください")
  private String email;

  private String region;

  @Min(value = 0, message = "年齢は0以上である必要があります")
  private int age;

  @NotNull(message = "性別は必須です")
  private Gender gender;

  private String remark;

  private Boolean isDeleted;
}
