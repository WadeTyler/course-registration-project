-- Login: mysql --user=register_rus_user --password register_r_us
-- Create Users table
CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    created_at DATE,
    PRIMARY KEY (id)
);

COMMIT;

-- Create the Authorities Table
CREATE TABLE authorities (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(13) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO authorities (name) VALUES ('ADMIN');
INSERT INTO authorities (name) VALUES ('INSTRUCTOR');
INSERT INTO authorities (name) VALUES ('STUDENT');

COMMIT;

-- Create User Authorities Table
CREATE TABLE user_authorities (
    user_id INT NOT NULL,
    authority_id INT NOT NULL,
    PRIMARY KEY (user_id, authority_id)
);

ALTER TABLE user_authorities ADD CONSTRAINT FK_user_authorities_user_id FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE user_authorities ADD CONSTRAINT FK_user_authorities_authority_id FOREIGN KEY (authority_id) REFERENCES authorities (id);

COMMIT;

-- Create Terms
CREATE TABLE terms (
    id INT NOT NULL AUTO_INCREMENT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    registration_start DATE NOT NULL,
    registration_end DATE NOT NULL,
    created_at DATE,
    PRIMARY KEY (id)
);

COMMIT;

-- Create Prerequisites
CREATE TABLE prerequisites (
    id INT NOT NULL AUTO_INCREMENT,
    course_id INT NOT NULL,
    required_course_id INT NOT NULL,
    minimum_grade DECIMAL NOT NULL,
    created_at DATE,
    PRIMARY KEY (id)
);

COMMIT;

-- Create courses_table
CREATE TABLE courses (
    id INT NOT NULL AUTO_INCREMENT,
    department VARCHAR(255) NOT NULL,
    code INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(4000) NOT NULL,
    credits INT NOT NULL,
    created_at DATE,
    PRIMARY KEY (id)
);

-- Add FK Constraints between courses and prerequisites
ALTER TABLE prerequisites ADD CONSTRAINT FK_prerequisites_course_id FOREIGN KEY (course_id) REFERENCES courses (id);
ALTER TABLE prerequisites ADD CONSTRAINT FK_prerequisites_requried_course_id FOREIGN KEY (required_course_id) REFERENCES courses (id);

COMMIT;

-- Create course_sections Table
CREATE TABLE course_sections (
    id INT NOT NULL AUTO_INCREMENT,
    course_id INT NOT NULL,
    term_id INT NOT NULL,
    instructor_id INT NOT NULL,
    room VARCHAR(20) NOT NULL,
    capacity INT NOT NULL,
    schedule VARCHAR(255) NOT NULL,
    enrolled_count INT NOT NULL,
    created_at DATE,
    PRIMARY KEY (id)
);

-- Add FK Constraints between course_sections, and instructors, terms, and courses
ALTER TABLE course_sections ADD CONSTRAINT FK_course_sections_course_id FOREIGN KEY (course_id) REFERENCES courses (id);
ALTER TABLE course_sections ADD CONSTRAINT FK_course_sections_term_id FOREIGN KEY (term_id) REFERENCES terms (id);
ALTER TABLE course_sections ADD CONSTRAINT FK_course_sections_instructor_id FOREIGN KEY (instructor_id) REFERENCES users (id);

COMMIT;

-- Create enrollments Table
CREATE TABLE enrollments (
    student_id INT NOT NULL,
    course_section_id INT NOT NULL,
    grade decimal NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at DATE,
    PRIMARY KEY (student_id, course_section_id)
);

-- What do we want to do with status here? Just a text box?
-- Add FK Constraints between enrollments, and users, and course_sections
ALTER TABLE enrollments ADD CONSTRAINT FK_enrollments_user_id FOREIGN KEY (student_id) REFERENCES users (id);
ALTER TABLE enrollments ADD CONSTRAINT FK_enrollments_course_section_id FOREIGN KEY (course_section_id) REFERENCES course_sections (id);

COMMIT;

-- Start Dummy data
-- Insert 5 dummy users
-- Because it's dummy data all users passwords are 1q2w#E$R
INSERT INTO users (id, username, password, first_name, last_name, created_at) VALUES (1, "jbish@mbop.com", "$2a$10$wSYZ5UCFZcsUa.Qg/4fmJuwuUxsQj7kDiXRXu4CRmnW5B3eX1ZLWW", "Joseph", "Bishop", NOW());
INSERT INTO users (id, username, password, first_name, last_name, created_at) VALUES (2, "john.smith@gmail.com", "$2a$10$tO0rt0XiCybM/iC8I3M1M.PIl1KwO7voFbYUrJWrQ/rrnL7K/kxQS", "John", "Smith", NOW());
INSERT INTO users (id, username, password, first_name, last_name, created_at) VALUES (3, "jasmith@mail.com", "$2a$10$g0Jsm3s./0mtQWXAlxybu.53QYRaaW/sj.XpbnJvia5xEtoOEFUA6", "Jane", "Smith", NOW());
INSERT INTO users (id, username, password, first_name, last_name, created_at) VALUES (4, "marlow@mail.com", "$2a$10$H6ffjPvfk.7TznvBWNE/WeVBJbfZbK7UCBkYsahFUScMw7FyHZUjm", "Mark", "Arlow", NOW());
INSERT INTO users (id, username, password, first_name, last_name, created_at) VALUES (5, "jessica.lane@mail.com", "$2a$10$kdILO.X.iOus.XdW0ACCI..tnOSBGX7/qwUsB7wdTDoGD.Q6OwtrK", "Jessica", "Lane", NOW());

COMMIT;

-- Create authorities for the users
-- Joseph Bishop is a student
INSERT INTO user_authorities (user_id, authority_id) VALUES (1, 3);

-- John Smith is a student
INSERT INTO user_authorities (user_id, authority_id) VALUES (2, 3);

-- Jane Smith is an Instructor
INSERT INTO user_authorities (user_id, authority_id) VALUES (3, 2);

-- Mark Arlow is an Instructor
INSERT INTO user_authorities (user_id, authority_id) VALUES (4, 2);

-- Jessica Lane is an Administrator
INSERT INTO user_authorities (user_id, authority_id) VALUES (5, 1);

COMMIT;

-- Insert some Courses
INSERT INTO courses (id, department, code, title, description, credits, created_at)
    VALUES (1, "MATH", 141, "Calculus I", "An introduction to the world of calculus!", 3, NOW());
INSERT INTO courses (id, department, code, title, description, credits, created_at)
    VALUES (2, "CS", 050, "Introduction To Computer Science", "An introduction to the fundamentals of computer science.", 3, NOW());
INSERT INTO courses (id, department, code, title, description, credits, created_at)
    VALUES (3, "CS", 104, "Introduction to Java Programming", "An introduction to the Java programming language and the the Object Oriented Programming (OOP) model.", 3, NOW());
INSERT INTO courses (id, department, code, title, description, credits, created_at)
    VALUES (4, "PHYS", 110, "Introduction to Physics", "A broad introduction to physics topics.", 3, NOW());
INSERT INTO courses (id, department, code, title, description, credits, created_at)
    VALUES (5, "PHYS", 216, "Wave Theory with Lab", "Advanced Wave theory and hands on lab", 4, NOW());
INSERT INTO courses (id, department, code, title, description, credits, created_at)
    VALUES (6, "MATH", 105, "College Algebra", "College level Algebra concepts.", 3, NOW());

COMMIT;

-- Create prerequisites
-- Calculus Requires Algebra
INSERT INTO prerequisites (course_id, required_course_id, minimum_grade, created_at)
    VALUES(1, 6, 70.0, NOW());

-- CS 104 requires CS 50
INSERT INTO prerequisites (course_id, required_course_id, minimum_grade, created_at)
    VALUES(3, 2, 81.5, NOW());

-- CS 104 also requires Algebrea
INSERT INTO prerequisites (course_id, required_course_id, minimum_grade, created_at)
    VALUES(3, 6, 70, NOW());

-- Physics 110 requires Calc I
INSERT INTO prerequisites (course_id, required_course_id, minimum_grade, created_at)
    VALUES(4, 1, 85.0, NOW());

-- Physics 216 requires Physics 110
INSERT INTO prerequisites (course_id, required_course_id, minimum_grade, created_at)
    VALUES(5, 4, 80.0, NOW());

COMMIT;

-- Create Terms
INSERT INTO terms (id, start_date, end_date, registration_start, registration_end, created_at)
    VALUES(1, "2025-01-15", "2025-04-15", "2024-12-01", "2025-01-12", NOW());
INSERT INTO terms(id, start_date, end_date, registration_start, registration_end, created_at)
    VALUES(2, "2025-04-22", "2025-07-22", "2024-03-08", "2025-03-19", NOW());
INSERT INTO terms(id, start_date, end_date, registration_start, registration_end, created_at)
    VALUES(3, "2025-08-01", "2025-11-01", "2024-06-15", "2025-07-29", NOW());

COMMIT;

-- Create Course sections
INSERT INTO course_sections (id, course_id, term_id, instructor_id, room, capacity, schedule, enrolled_count, created_at)
    VALUES(1, 1, 1, 3, 100, 20, "Spring", 3, NOW());
INSERT INTO course_sections (id, course_id, term_id, instructor_id, room, capacity, schedule, enrolled_count, created_at)
    VALUES(2, 2, 1, 3, 103, 20, "Spring", 8, NOW());
INSERT INTO course_sections (id, course_id, term_id, instructor_id, room, capacity, schedule, enrolled_count, created_at)
    VALUES(3, 3, 2, 3, 215, 15, "Summer", 1, NOW());
INSERT INTO course_sections (id, course_id, term_id, instructor_id, room, capacity, schedule, enrolled_count, created_at)
    VALUES(4, 5, 2, 4, 314, 12, "Summer", 12, NOW());
INSERT INTO course_sections (id, course_id, term_id, instructor_id, room, capacity, schedule, enrolled_count, created_at)
    VALUES(5, 4, 3, 4, 223, 20, "Fall", 0, NOW());
INSERT INTO course_sections (id, course_id, term_id, instructor_id, room, capacity, schedule, enrolled_count, created_at)
    VALUES(6, 6, 3, 4, 186, 22, "Fall", 5, NOW());

COMMIT;

-- Create Enrollments
INSERT INTO enrollments (student_id, course_section_id, grade, status, created_at)
    VALUES (1, 1, 88.3, "Complete", NOW());
INSERT INTO enrollments (student_id, course_section_id, grade, status, created_at)
    VALUES (1, 2, 90.5, "Complete", NOW());
INSERT INTO enrollments (student_id, course_section_id, grade, status, created_at)
    VALUES (1, 3, 88.3, "In Progress", NOW());
INSERT INTO enrollments (student_id, course_section_id, grade, status, created_at)
    VALUES (1, 4, 88.3, "In Progress", NOW());
INSERT INTO enrollments (student_id, course_section_id, grade, status, created_at)
    VALUES (2, 5, 0.0, "Enrolled", NOW());
INSERT INTO enrollments (student_id, course_section_id, grade, status, created_at)
    VALUES (2, 6, 0.0, "Enrolled", NOW());

COMMIT;
