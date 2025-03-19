INSERT INTO students ( name, kana_name, nickname, age, email, region, remark, gender, is_deleted) VALUES
('室伏 広治', 'むろぶし こうじ', 'Koji Murobushi', 78, 'koji.muro@example.com', '川', '削除されました', 'FEMALE', 1),
('田中 真美子', 'たなか まみこ', 'まみちゃん', 25, 'tanaka@example.com', '北海道', '生まれ変わりました。', 'FEMALE', 0),
('錦織 圭', 'にしこり けい', 'Kei', 34, 'kei.nishikori@example.com', 'アメリカ', 'この学生は優秀です。', 'MALE', 0),
('池江 璃花子', 'いけえ りかこ', 'Rika', 24, 'rikako.ikee@example.com', '東京', '将来は海外へ行きたい', 'MALE', 0),
('井上 花月', 'いのうえ かづき', 'はなちゃん', 25, 'rikako.inoue@example.com', '北海道', '花嫁', 'FEMALE', 0);

INSERT INTO students_courses (start_date_at, end_date_at, course_name) VALUES
('2025-03-01', '2025-06-01', '新しいコース'),
('2025-03-01', NULL, 'マーケティング'),
('2024-03-01', NULL, 'WordPress'),
('2024-04-01', NULL, 'マーケティング'),
('2025-02-01', NULL, 'Java');
