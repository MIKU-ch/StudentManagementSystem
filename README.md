# 実装内容
1. 依存性注入のリファクタリング
- @Autowiredによるフィールドインジェクションをコンストラクタインジェクションに変更。
- パッケージ構成を変更し、サービスクラスとコントローラークラスを分割・作成。
2. 受講情報の取得
- 30代の受講情報を抽出し、リストをコントローラーで返す処理を実装。
- Javaコース受講者の受講生情報を取得し、リストをコントローラーで返す処理を実装。

# 実行結果
## テーブル情報
- studentsテーブル( id は INT AUTO_INCREMENT を使用しましたが、id:1の人を練習でDELETしたため id:2 から始まっています。)
![受講生情報テーブル](https://github.com/user-attachments/assets/ceed5cd4-6de1-4dc6-813d-0e4b68d0559f)  
- students_courses テーブル
![受講コース情報テーブル](https://github.com/user-attachments/assets/15990688-225a-4224-82b3-d3ec7657cd71)  
## コマンドでstudents全員の情報とすべてのコース情報が取れていることを確認 
- students
![students](https://github.com/user-attachments/assets/3b70d160-135e-464a-96b8-e240250ec32f) 
- students_courses 
![students_courses](https://github.com/user-attachments/assets/3902abf5-7e04-4428-a4ad-a932cbd17a74) 
- 30代の受講情報を抽出し、リストをコントローラーで返す処理 
![30代](https://github.com/user-attachments/assets/7db8116d-13db-4bd7-9ad7-bd478138bdb0) 
- Javaコース受講者の受講生情報を取得し、リストをコントローラーで返す処理 
![Java](https://github.com/user-attachments/assets/465d5ec2-411c-4df3-9c1a-1df4bafdb4e9)
