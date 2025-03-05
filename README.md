# 変更点
## StudentController.java
- 新しい学生を登録するための `newStudent` メソッドと `registerStudent` メソッドを追加
  - `/newStudent` で新規学生登録フォームを表示
  - `/registerStudent` で学生情報を登録
- `StudentDetail` クラスを使い、学生とコース情報を同時に登録する処理を実装

## StudentsCourses.java
- `courseName` フィールドを `StudentsCourses` クラスに追加

## StudentRepository.java
- `registerStudent` メソッドと `registerStudentsCourses` メソッドを追加
  - 学生情報とコース情報をデータベースに登録

## StudentService.java
- `registerStudent` メソッドを追加
  - `StudentDetail` を受け取って、学生情報とコース情報をそれぞれ登録

## registerStudent.html
- 学生登録フォームを作成
  - 学生情報とコース情報を一度に入力できるフォームを作成
  - コース選択や開始日、終了日の入力フィールドを追加

## studentList.html
- 学生リストに変更はありませんが、他のテンプレートと一貫性を保つように調整

# 動作確認
- **1. 学生情報とコースの登録前**
![受講生とコース](https://github.com/user-attachments/assets/f7c2eb61-991c-4532-b442-5e0922f06425)

- **2. 学生情報登録ページで必要事項を記入し登録ボタンを押す**
![登録](https://github.com/user-attachments/assets/d0b3c70d-536e-4d57-95cb-b7453b445f70)

- **3. 学生情報とコースが登録されていることを確認しました**
![登録できていることを確認しました](https://github.com/user-attachments/assets/a7db7181-9c1d-4350-ad98-23c139e02e0f)

## テーブル情報
![テーブル情報](https://github.com/user-attachments/assets/a010d029-4851-42e7-b3f8-ea198bc9697c)
