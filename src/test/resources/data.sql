-- 学生データの初期登録
INSERT INTO students (name, kana_name, nickname, age, email, region, remark, gender, is_deleted) VALUES
('室伏 広治', 'むろぶし こうじ', 'Koji Murobushi', 78, 'koji.muro@example.com', '川', '削除されました', 'FEMALE', 1),
('田中 真美子', 'たなか まみこ', 'まみちゃん', 25, 'tanaka@example.com', '北海道', '生まれ変わりました。', 'FEMALE', 0),
('錦織 圭', 'にしこり けい', 'Kei', 34, 'kei.nishikori@example.com', 'アメリカ', 'この学生は優秀です。', 'MALE', 0),
('池江 璃花子', 'いけえ りかこ', 'Rika', 24, 'rikako.ikee@example.com', '東京', '将来は海外へ行きたい', 'MALE', 0),
('井上 花月', 'いのうえ かづき', 'はなちゃん', 25, 'rikako.inoue@example.com', '北海道', '花嫁', 'FEMALE', 0);

-- コースステータステーブルの初期登録
INSERT INTO course_status (status) VALUES
('KARI_APPLY'),
('HON_APPLY'),
('ENROLLING'),
('FINISHED');

-- コースデータの初期登録
-- 各コースに対して、対応する student_id と course_status_id を指定
INSERT INTO students_courses (student_id, start_date_at, end_date_at, course_name, course_status_id) VALUES
(1, '2024-01-01', '2025-03-23', 'AWS', 4),
(2, '2024-02-01', '2025-03-23', 'デザイン', 4),
(3, '2024-03-01', NULL, 'WordPress', 3),
(4, '2024-04-01', NULL, 'Java', 3),
(5, '2024-05-01', NULL, 'マーケティング', 3);
