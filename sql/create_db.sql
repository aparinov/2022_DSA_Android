DROP TABLE IF EXISTS project_participants CASCADE;
DROP TABLE IF EXISTS applications CASCADE;
DROP TABLE IF EXISTS profiles CASCADE;
DROP TABLE IF EXISTS profile_infos CASCADE;
DROP TABLE IF EXISTS registration_forms CASCADE;
DROP TABLE IF EXISTS projects CASCADE;
DROP TABLE IF EXISTS project_tags CASCADE;
DROP TABLE IF EXISTS profile_tags CASCADE;

-- Хранит данные пользователя на момент заполнения
-- формы регистрации и ссылку для верификации.
CREATE TABLE registration_forms (
    email             TEXT PRIMARY KEY,
    first_name        TEXT NOT NULL,
    last_name         TEXT NOT NULL,
    middle_name       TEXT,
    password          TEXT NOT NULL,
    verified          BOOLEAN NOT NULL DEFAULT False,
    verification_code TEXT NOT NULL
);

-- Хранит данные созданного профиля с верифицированным
-- владельцем. В `password` храним хеш от настоящего пароля.
-- `email`, кажется, можно использовать как первичнй ключ для профиля,
-- так как они уникальные и верифицируемые.
CREATE TABLE profiles (
    email           TEXT PRIMARY KEY,
    first_name      TEXT NOT NULL,
    last_name       TEXT NOT NULL,
    middle_name     TEXT,
    password        TEXT NOT NULL
);

-- Всякая информация о пользователе. Вынес в отдельную
-- таблицу, чтобы не захломлять таблицу профилей.
-- Поле `student` показывает, студент или преподаватель пользователь.
-- Валидность `degree`, `faculty`, `program` будем проверять в коде.
-- В `bio` пользователь может написать любой текст о себе.
CREATE TABLE profile_infos (
    email         TEXT PRIMARY KEY,
    nickname      TEXT,
    student       BOOLEAN DEFAULT True,
    degree        TEXT,
    faculty       TEXT,
    program       TEXT,
    year_of_study INT,
    bio           TEXT,
    FOREIGN KEY (email) REFERENCES profiles(email)
);

-- Разделил статус самого проекта и статус набора на проект,
-- так как эти события необязательно зависят друг от друга:
-- работа над проектом не начата, а набор на него уже закончен,
-- или работа уже началась, а набор все еще идет.
-- `recruiting_status`: ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED')
-- `project_status`: ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED')
-- `application_deadline`: дедлайн для подачи заявок.
-- `planned_start_of_work`, `planned_finish_of_work` - планируемые
-- даты начала и конца работы над проектом, на логику работы никак не
-- влияют, просто информационные поля для студентов.
CREATE TABLE projects (
    id                         SERIAL PRIMARY KEY,
    author_email               TEXT NOT NULL,
    title                      TEXT NOT NULL UNIQUE,
    title_lower                TEXT NOT NULL,
    description                TEXT NOT NULL,
    max_number_of_students     INT NOT NULL,
    current_number_of_students INT NOT NULL DEFAULT 0,
    recruiting_status          TEXT NOT NULL DEFAULT 'NOT_STARTED',
    project_status             TEXT NOT NULL DEFAULT 'NOT_STARTED',
    applications_deadline      TIMESTAMP,
    planned_start_of_work      TIMESTAMP,
    planned_finish_of_work     TIMESTAMP,
    FOREIGN KEY (author_email) REFERENCES profiles(email)
);

CREATE TABLE tags (
    id  INT PRIMARY KEY,
    tag TEXT NOT NULL UNIQUE
);

CREATE TABLE project_tags (
    project_id INT NOT NULL,
    tag_id     INT NOT NULL,
    PRIMARY KEY (project_id, tag_id),
    FOREIGN KEY(project_id) REFERENCES projects(id),
    FOREIGN KEY(tag_id) REFERENCES tags(id)
);

CREATE TABLE profile_tags (
    email  TEXT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (email, tag_id),
    FOREIGN KEY(email) REFERENCES profiles(email),
    FOREIGN KEY(tag_id) REFERENCES tags(id)
);

-- status: ('CREATED', 'ACCEPTED', 'REJECTED')
-- Думаю, стоит сделать возможность подачи нескольких заявок от одного
-- студента на один проект, если предыдущую заявку отклонили.
CREATE TABLE applications (
    id            SERIAL PRIMARY KEY,
    project_id    INT NOT NULL,
    student_email TEXT NOT NULL,
    created_at    TIMESTAMP NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP NOT NULL DEFAULT now(),
    status        TEXT DEFAULT 'CREATED',
    FOREIGN KEY(project_id) REFERENCES projects(id),
    FOREIGN KEY(student_email) REFERENCES profiles(email)
);

-- Хранит инфу об участниках проектов (для тех, у кого статус заявки
-- ACCEPTED).
CREATE TABLE project_participants (
    id            SERIAL PRIMARY KEY,
    project_id    INT NOT NULL REFERENCES projects(id),
    student_email TEXT NOT NULL REFERENCES profiles(email),
    applied_at    TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (project_id, student_email),
    FOREIGN KEY(project_id) REFERENCES projects(id),
    FOREIGN KEY(student_email) REFERENCES profiles(email)
);
