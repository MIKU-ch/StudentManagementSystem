package raisetech.StudentManagementSystem.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Student {

  private Integer id;
  private String name;
  private String kanaName;
  private String nickname;
  private String email;
  private String region;
  private int age;
  private String gender;
  private String remark;
  private boolean isDeleted;
}
