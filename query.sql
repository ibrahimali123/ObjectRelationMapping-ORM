-- ----------------------------------------------------------
-- AUTHOR: IBRAHIM ALI MOHAMED EBEIDO
-- ENTITY DESIGNER DDL SCRIPT FOR SQL SERVER 2005, 2008, 2012
-- DATE CREATED: Thu Apr 14 22:48:44 EET 2016
-- ----------------------------------------------------------


-- ----------------------------------------------------------
-- Creating Database
-- ----------------------------------------------------------
CREATE DATABASE IF NOT EXISTS hema;

-- ----------------------------------------------------------
-- using database
-- ----------------------------------------------------------
USE hema;

-- ----------------------------------------------------------
-- Creating tables
-- ----------------------------------------------------------

-- Creating table Employee_Project
CREATE TABLE IF NOT EXISTS Employee_Project (
   ID int(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
   employee_id int DEFAULT 0,
   project_id int DEFAULT 0
);

-- Creating table Project
CREATE TABLE IF NOT EXISTS Project (
   ID int(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR(100) NULL,
   budget int DEFAULT 0,
   department_id int DEFAULT 0
);

-- Creating table Department
CREATE TABLE IF NOT EXISTS Department (
   ID int(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR(100) NULL,
   location VARCHAR(100) NULL,
   budget int DEFAULT 0
);

-- Creating table Person
CREATE TABLE IF NOT EXISTS Person (
   ID int(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR(100) NULL,
   SSN VARCHAR(100) NULL,
   type VARCHAR(100) NULL,
   salary int DEFAULT 0,
   department_id int DEFAULT 0
);

-- ----------------------------------------------------------
-- Creating all FOREIGN KEY constraints 
-- ----------------------------------------------------------

-- Creating foreign key on [employee_id] in table Employee_Project
ALTER TABLE Employee_Project
ADD CONSTRAINT FK_personemployee_project
	FOREIGN KEY (employee_id)
	REFERENCES Person (ID)
	ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Creating foreign key on [project_id] in table Employee_Project
ALTER TABLE Employee_Project
ADD CONSTRAINT FK_projectemployee_project
	FOREIGN KEY (project_id)
	REFERENCES Project (ID)
	ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Creating foreign key on [department_id] in table Project
ALTER TABLE Project
ADD CONSTRAINT FK_departmentproject
	FOREIGN KEY (department_id)
	REFERENCES Department (ID)
	ON DELETE NO ACTION ON UPDATE NO ACTION;


-- Creating foreign key on [department_id] in table Person
ALTER TABLE Person
ADD CONSTRAINT FK_departmentperson
	FOREIGN KEY (department_id)
	REFERENCES Department (ID)
	ON DELETE NO ACTION ON UPDATE NO ACTION;


