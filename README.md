# 受講生情報更新機能の追加
このリポジトリでは、受講生の詳細情報を表示・更新する機能を追加しました。以下は、主な変更点です。
## 変更点
### 1. `StudentController` の更新
- 受講生詳細情報を表示するための新しい `GET` メソッド (`/student/{id}`) を追加。
  - 受講生の詳細情報と関連するコース情報を取得し、画面に表示。
  - コース情報が空の場合、デフォルトで1件のコース情報を追加（null回避）。
  
- 受講生情報を更新する `POST` メソッド (`/updateStudent`) を追加。
  - 入力フォームから送信された受講生情報をデータベースに更新。

### 2. `StudentRepository` の更新
- 特定の学生のコース情報を取得するメソッド `findCoursesByStudentId` を追加。
- 学生情報の更新メソッド `updateStudent` を再実装（学生情報の更新処理を担当）。
- 学生のコース情報を更新するための新しいメソッド `updateStudentsCourses` を追加。

### 3. `StudentService` の更新
- 受講生情報とそのコース情報を取得する `getStudentDetailById` メソッドを追加。
- 受講生情報とコース情報を更新する `updateStudent` メソッドを追加。

### 4. HTMLテンプレートの変更
- `studentList.html` にて、受講生名をクリック可能なリンクに変更。
  - リンクをクリックすることで、受講生の詳細画面に遷移。

- `updateStudent.html` にて、受講生情報の更新フォームを実装。
  - 学生IDをhiddenフィールドとして追加し、フォーム送信時に受講生IDも送信されるように変更。
  - コース名の選択肢をリストで表示し、選択されたコース情報をフォームに反映。

### 実行結果(id:6の鈴木健太郎さんの年齢を更新できることを確認しました)
![前](https://github.com/user-attachments/assets/ad8bdb05-edae-4794-9684-97c113421630)
![更新](https://github.com/user-attachments/assets/e8f65250-95ee-4524-9004-498cab8184b6)
![後](https://github.com/user-attachments/assets/6a5d9b6a-6c16-4de3-8c68-229a16b48798)

## 追加の処理
https://github.com/MIKU-ch/StudentManagementSystem/pull/11#issuecomment-2701002332
https://github.com/MIKU-ch/StudentManagementSystem/pull/12#issue-2903914171
