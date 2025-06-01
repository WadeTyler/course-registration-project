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
