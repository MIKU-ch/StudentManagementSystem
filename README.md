## 概要
Spring BootアプリケーションにThymeleafを導入し、フロントエンドを実装しました。また、既存のStudentControllerとStudentServiceをリファクタリングし、責務の分離とコードの整理を行いました。以下の変更が含まれます。

## 変更点

1. Thymeleafの導入
   - build.gradleにThymeleaf依存関係を追加しました。
   - 新しいHTMLテンプレート (studentList.html) を追加し、受講生の情報を表示します。

2. StudentControllerのリファクタリング
   - RestControllerからControllerに変更しました。
   - Modelを使って、Thymeleafテンプレートにデータを渡すようにしました。
   - @PatchMappingなどのエンドポイントを削除し、単純化しました。

3. StudentServiceのリファクタリング
   - 学生とコース情報を結合して返すメソッドを追加しました (getStudentsWithCourses)。

4. StudentRepositoryのメソッド名変更
   - より明確な命名規則に従い、searchメソッドをsearchStudentに変更しました。

5. HTMLテンプレートの追加
   - studentList.html: Thymeleafで動的に受講生情報を表示します。

## 動作確認
- ブラウザから/studentsCourseListエンドポイントにアクセスすることで、受講生の一覧が正しく表示されることを確認しました。
![スクリーンショット 2024-10-12 123059](https://github.com/user-attachments/assets/d35e0457-5706-403b-8177-fe958ec8235e)
