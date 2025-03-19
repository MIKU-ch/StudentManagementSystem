CREATE TABLE IF NOT EXISTS students (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    kana_name VARCHAR(100) NOT NULL,
    nickname VARCHAR(50),
    age INT NOT NULL,
    email VARCHAR(255) NOT NULL,
    region VARCHAR(100) NOT NULL,
    remark VARCHAR(255),
    gender VARCHAR(10) NOT NULL,
    is_deleted TINYINT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS students_courses (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    start_date_at DATE,
    end_date_at DATE,
    course_name VARCHAR(255) NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE SET NULL
);
