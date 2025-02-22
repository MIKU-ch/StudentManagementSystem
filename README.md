## 概要

Thymeleafを利用して受講生情報を一覧表示できるように変更しました。

## 変更点

### 1. `build.gradle` の変更

- Thymeleafの依存関係を追加しました。
  ```gradle
  implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
  ```

### 2. `StudentController.java` の変更

- `@RestController` から `@Controller` に変更。
- `getStudentsList` メソッドの戻り値を `String` に変更し、 `Model` に `studentList` を追加する形に修正。

### 3. `StudentsCourses.java` の変更

- `id` を `String` から `int` に変更。（正規化を学習したため、テーブル情報を適切に設計する目的で変更）
- `courseName` を `courseId` に変更。（正規化を学習したため、冗長性を減らし適切に管理するために変更）
- `startDateAt` と `endDateAt` を `LocalDateTime` から `LocalDate` に変更。（単純にわかりやすくするために変更）

### 4. `studentList.html` の追加

- 受講生情報を一覧表示するテンプレートを追加。

## 注意点

- `@RequestMapping("/students")` を追加したため、`http://localhost:8080/students/studentsCourseList`
  でアクセスできます。

## テーブル情報
![テーブル情報](https://github.com/user-attachments/assets/06154ed0-7281-4cec-8d1b-6c66b1a80cd6)
