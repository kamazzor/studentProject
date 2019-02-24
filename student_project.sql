DROP TABLE IF EXISTS jc_student_child;
DROP TABLE IF EXISTS jc_student_order;
DROP TABLE IF EXISTS jc_register_office;
DROP TABLE IF EXISTS jc_passport_office;
DROP TABLE IF EXISTS jc_country_struct;
DROP TABLE IF EXISTS jc_street;

--    Таблица улиц
CREATE TABLE jc_street(
	street_code integer not null,           -- Код улицы
	street_name varchar(300),               -- Название улицы
	PRIMARY KEY (street_code)
);
--    Таблица населенных пунктов
CREATE TABLE jc_country_struct(
	area_id char(12) not null,              -- Код населенного пункта типа ОКАТО
	area_name varchar(200),                 -- Название населенного пункта
	PRIMARY KEY (area_id)
);
--    Таблица органов, выдающих паспорт
CREATE TABLE jc_passport_office(
	p_office_id integer not null,           -- ID органа, выдавшего паспорт
	p_office_area_id char(12) not null,     -- ID населенного пункта (ОКАТО), выдавшего паспорт
	p_office_name varchar(200),             -- Название органа, выдавшего паспорт
	PRIMARY KEY (p_office_id),
	FOREIGN KEY (p_office_area_id) REFERENCES jc_country_struct(area_id) ON DELETE RESTRICT
);
--    Таблица ЗАГСов
CREATE TABLE jc_register_office(
	r_office_id integer not null,           -- ID ЗАГСа
	r_office_area_id char(12) not null,     -- ID населенного пункта (ОКАТО), где находится ЗАГС
	r_office_name varchar(200),             -- Название ЗАГСа
	PRIMARY KEY (r_office_id),
	FOREIGN KEY (r_office_area_id) REFERENCES jc_country_struct(area_id) ON DELETE RESTRICT
);

--    Таблица студенческих заявок
CREATE TABLE jc_student_order(
    student_order_id SERIAL,
    student_order_date timestamp not null,
    student_order_status int,

--    Данные мужа
    h_sur_name varchar(100) not null,
    h_given_name varchar(100) not null,
    h_patronymic varchar(100) not null,
    h_date_of_birth date not null,
    h_passport_seria varchar(10) not null,
    h_passport_number varchar(10) not null,
    h_passport_date date not null,
    h_passport_office_id integer not null,
    h_post_index varchar(10),
    h_street_code integer not null,
    h_building varchar(10) not null,
    h_extension varchar(10),
    h_apartment varchar(10),

--    Данные жены
    w_sur_name varchar(100) not null,
    w_given_name varchar(100) not null,
    w_patronymic varchar(100) not null,
    w_date_of_birth date not null,
    w_passport_seria varchar(10) not null,
    w_passport_number varchar(10) not null,
    w_passport_date date not null,
    w_passport_office_id integer not null,
    w_post_index varchar(10),
    w_street_code integer not null,
    w_building varchar(10) not null,
    w_extension varchar(10),
    w_apartment varchar(10),

--    Данные о браке
    certificate_id varchar(20) not null,
    register_office_id integer not null,
    marriage_date date not null,

    PRIMARY KEY (student_order_id),
    FOREIGN KEY (h_street_code) REFERENCES jc_street(street_code) ON DELETE RESTRICT,
    FOREIGN KEY (w_street_code) REFERENCES jc_street(street_code) ON DELETE RESTRICT,
    FOREIGN KEY (register_office_id) REFERENCES jc_register_office(r_office_id) ON DELETE RESTRICT

--    To be continued
);

--    Таблица детей
CREATE TABLE jc_student_child (

--    Данные ребенка
    student_child_id SERIAL,            -- ID записи ребенка
    student_order_id integer not null,  -- ID студенческого заявления
    c_sur_name varchar(100) not null,
    c_given_name varchar(100) not null,
    c_patronymic varchar(100) not null,
    c_date_of_birth date not null,
    c_certificate_number varchar(10) not null,
    c_certificate_date date not null,
    c_register_office_id integer not null,
    c_post_index varchar(10),
    c_street_code integer not null,
    c_building varchar(10) not null,
    c_extension varchar(10),
    c_apartment varchar(10),

    PRIMARY KEY (student_child_id),
    FOREIGN KEY (c_street_code) REFERENCES jc_street(street_code) ON DELETE RESTRICT,
    FOREIGN KEY (c_register_office_id) REFERENCES jc_register_office(r_office_id) ON DELETE RESTRICT

);