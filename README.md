## 更新履歴
### 1. `StudentController` の変更
- `@Controller` から `@RestController` に変更し、レスポンスとして HTML ではなく JSON を返すように変更。
- `listStudents` メソッドの戻り値を `List<StudentDetail>` に変更。
- 学生登録・更新時に `@RequestBody` を使用するように変更。
- `showRegistrationForm` と `showUpdateForm` メソッドを削除し、登録・更新処理を API 化。
- 更新時のレスポンスを `ResponseEntity<String>` に変更。

### 2. `StudentService` の変更
- 学生情報更新時の処理を修正。
- コース情報の更新時に **コース名ではなくコースID** を必須とするように変更。
  - これにより、同じ名前のコースが複数存在する場合でも正確に更新できるようになりました。
  - コースIDが提供されていない場合は `IllegalArgumentException` をスローする仕様に変更。

## 実行結果(Postmanで実行結果を確認しました)
### データベース情報
![前](https://github.com/user-attachments/assets/4b3c5865-1640-440b-aab0-8bbc1ca5b55a)
- @Select (courses id = Nullでした。。。後ほど直します。)
![一覧](https://github.com/user-attachments/assets/3b8133c0-8b1c-44a9-8946-066ecbbf63f0)

- @Post(登録)
![登録](https://github.com/user-attachments/assets/fc73e22e-df6a-44f2-bdc6-8c18312f97ed)
![登録後](https://github.com/user-attachments/assets/2da00fd1-feed-4d92-95b0-60e1123c93af)

- @Post(更新 is_deleted更新できてませんでした。。。後ほど直します。)
![更新 (1)](https://github.com/user-attachments/assets/22fed614-594b-42d5-8c8e-9d12161f69e9)
![更新 (2)](https://github.com/user-attachments/assets/e3d993e0-b4cf-478d-97be-e5803d03f372)

  

