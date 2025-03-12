## 概要
この変更は、学生の登録および情報取得処理の改善を目的としています。主にコントローラ、サービス、リポジトリに変更が加えられ、学生情報とコース情報の管理が効率的になりました。また、StudentDetailクラスのリファクタリングが行われました。

## 変更内容
## 1. StudentControllerの変更
### 学生登録時のレスポンスを変更
新規学生登録時に、ResponseEntity<Map<String, Object>>型のレスポンスが返されるようになり、学生名や登録詳細を含むメッセージが含まれます。

### StudentConverterを削除
以前はStudentConverterクラスを使って学生情報の変換を行っていましたが、この変換処理がStudentService内で直接処理されるようになり、StudentConverterクラスが削除されました。

### listStudentsメソッドの修正
学生情報と関連するコース情報を一度に取得する処理がStudentServiceのlistStudentDetailsメソッドに移行されました。

## 2. StudentDetailクラスの変更
### Lombokの注釈を追加
@NoArgsConstructorと@AllArgsConstructorが追加され、引数なしおよび全引数のコンストラクタが生成されるようになりました。

## 3. StudentServiceの変更
### StudentConverterの追加
StudentConverterクラスをStudentServiceにインジェクトし、学生情報とコース情報を結合してStudentDetailリストを返すlistStudentDetailsメソッドが追加されました。

### 学生情報とコース情報の取得処理の統合
学生情報の取得と関連するコース情報をまとめて取得するロジックがサービス内に追加され、学生一覧を簡単に取得できるようになりました。

## 4. StudentRepositoryの変更
### コメント修正
コース情報の更新に関するコメントがわかりやすく修正されました。

## 5. StudentConverterの変更
### 不必要な処理の削除
StudentConverter内での不要なコメントや処理が削除され、よりシンプルなコードにリファクタリングされました。

## 動作確認(登録、更新、検索ができることをポストマンで確認しました。)
![前データベース (1)](https://github.com/user-attachments/assets/32adef15-40e4-470f-9302-9936036a1d09)
![登録](https://github.com/user-attachments/assets/05c4f8ec-850a-4e0d-aca5-038a549af371)
![更新](https://github.com/user-attachments/assets/0492459c-e8d5-418a-9565-7c30a69319ab)
![検索](https://github.com/user-attachments/assets/6165b575-37a7-44f9-a311-94333e7b0001)
![後データベース](https://github.com/user-attachments/assets/eb6232b1-d406-4106-bb80-d51f23cc7a26)

