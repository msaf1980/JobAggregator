CREATE TABLE city (
  id    int NOT NULL, 
  title varchar(255) NOT NULL, 
  CONSTRAINT pk_city 
    PRIMARY KEY (id));

CREATE UNIQUE INDEX city_id 
  ON city (id);
   
CREATE INDEX city_title 
  ON city (title);

ALTER TABLE public.city
  OWNER TO test;


CREATE TABLE city_distinct (
  id    int NOT NULL, 
  title varchar(255), 
  CONSTRAINT pk_city_distinct 
    PRIMARY KEY (id));
	
CREATE UNIQUE INDEX city_distinct_id 
  ON city_distinct (id);
CREATE INDEX city_distinct_title 
  ON city_distinct (title);

ALTER TABLE public.city_distinct
  OWNER TO test;

  
CREATE TABLE education (
  id    int NOT NULL, 
  title varchar(255) NOT NULL, 
  CONSTRAINT pk_education 
    PRIMARY KEY (id));

CREATE UNIQUE INDEX education_id 
  ON education (id);
CREATE INDEX education_title 
  ON education (title);

ALTER TABLE public.education
  OWNER TO test;
  
  
  
CREATE TABLE experience (
  id    int NOT NULL, 
  title varchar(255) NOT NULL, 
  CONSTRAINT pk_experience 
    PRIMARY KEY (id));

CREATE UNIQUE INDEX experience_id 
  ON experience (id);
CREATE INDEX experience_title 
  ON experience (title);
	
ALTER TABLE public.experience
  OWNER TO test;	
	
CREATE TABLE currency (
  id    int NOT NULL, 
  title varchar(255) NOT NULL, 
  PRIMARY KEY (id));
CREATE UNIQUE INDEX currency_id 
  ON currency (id); 

ALTER TABLE public.currency
  OWNER TO test;	
	
	
CREATE TABLE workingtype (
  id    int NOT NULL, 
  title varchar(255) NOT NULL, 
  CONSTRAINT pk_working_type 
    PRIMARY KEY (id));
	
CREATE UNIQUE INDEX workingtype_id 
  ON workingtype (id);
CREATE INDEX workingtype_title 
  ON workingtype (title);
	
ALTER TABLE public.workingtype
  OWNER TO test;
  
  

CREATE TABLE schedule (
  id    int NOT NULL, 
  title varchar(255) NOT NULL, 
  CONSTRAINT pk_schedule 
    PRIMARY KEY (id));
	
CREATE UNIQUE INDEX schedule_id 
  ON schedule (id);
CREATE INDEX schedule_title 
  ON schedule (title);

ALTER TABLE public.schedule
  OWNER TO test;
  
  

CREATE TABLE job_rubric (
  id    int NOT NULL, 
  title varchar(255), 
  CONSTRAINT pk_job_rubric 
    PRIMARY KEY (id));
	
CREATE UNIQUE INDEX job_rubric_id 
  ON job_rubric (id);
CREATE INDEX job_rubric_title 
  ON job_rubric (title);
  
ALTER TABLE public.job_rubric
  OWNER TO test;
  
  
  
CREATE TABLE job_specialty (
  id    int NOT NULL, 
  jobrubric_id int4,
  title varchar(255) NOT NULL, 
  CONSTRAINT pk_job_speciality 
    PRIMARY KEY (id));
	
CREATE UNIQUE INDEX job_specialty_id 
  ON job_specialty (id);
CREATE INDEX job_specialty_title 
  ON job_specialty (title);

ALTER TABLE job_speciality ADD CONSTRAINT fk_job_rubric_specialty FOREIGN KEY (jobrubricid) REFERENCES job_rubric (id);
  
ALTER TABLE public.job_specialty
  OWNER TO test;
  
  
  
CREATE TABLE resume (
  id                 bigint NOT NULL, 
  owner_id		     bigint NOT NULL, 
  name               varchar(255) NOT NULL, 
  wanted_salary      bigint, 
  currency_id		 int,
  header             varchar(255) NOT NULL, 
  url                varchar(255), 
  sex                varchar(1), 
  has_child          bool, 
  personal_qualities varchar(16000), 
  skills             varchar(16000), 
  marital_status     varchar(20), 
  birthday           timestamp(7), 
  add_day            timestamp(7), 
  mod_day            timestamp(7), 
  workingtype_id      int, 
  schedule_id         int, 
  experience_id       int, 
  education_id        int, 
  city_id             int NOT NULL, 
  citydistinct_id             int,
  smoke bool,
  journey bool,
  driver bool,
  driver_licenses varchar(10),
  CONSTRAINT pk_resume 
    PRIMARY KEY (id));
	
CREATE UNIQUE INDEX resume_id 
  ON resume (id);

ALTER TABLE resume ADD CONSTRAINT fk_resume_city FOREIGN KEY (city_id) REFERENCES city (id);
ALTER TABLE resume ADD CONSTRAINT fk_resume_citydistinct FOREIGN KEY (citydistinct_id) REFERENCES city_distinct (id);
ALTER TABLE resume ADD CONSTRAINT fk_resume_education FOREIGN KEY (education_id) REFERENCES education (id);
ALTER TABLE resume ADD CONSTRAINT fk_resume_expirience FOREIGN KEY (experience_id) REFERENCES experience (id);
ALTER TABLE resume ADD CONSTRAINT fk_resume_workingtype FOREIGN KEY (workingtype_id) REFERENCES workingtype (id);
ALTER TABLE resume ADD CONSTRAINT fk_resume_shedule FOREIGN KEY (schedule_id) REFERENCES schedule (id);
ALTER TABLE resume ADD CONSTRAINT fk_resume_currency FOREIGN KEY (currency_id) REFERENCES currency (id);

ALTER TABLE public.resume
  OWNER TO test;

CREATE TABLE cities_reference (
  resume_id       bigint NOT NULL, 
  city_id         int NOT NULL, 
  citydistinct_id int NOT NULL);
  
CREATE INDEX cities_reference_resumeid 
  ON cities_reference (resumeid);

ALTER TABLE cities_reference ADD CONSTRAINT fk_city_reference_city FOREIGN KEY (city_id) REFERENCES city (id);  
ALTER TABLE cities_reference ADD CONSTRAINT fk_city_distinct FOREIGN KEY (citydistinct_id) REFERENCES city_distinct (id);
ALTER TABLE cities_reference ADD CONSTRAINT fk_resume_cities_ref FOREIGN KEY (resume_id) REFERENCES resume (id);

  
ALTER TABLE public.cities_reference
  OWNER TO test;

  

CREATE TABLE resume_rubrics (
  resume_id        bigint NOT NULL, 
  jobrubric_id     int NOT NULL, 
  jobspecialty_id int NOT NULL);
  
CREATE INDEX resume_rubrics_resumeid 
  ON resume_rubrics (resume_id);
CREATE INDEX resume_rubrics_jobrubricid 
  ON resume_rubrics (jobrubric_id);
CREATE INDEX resume_rubrics_jobspecialityid 
  ON resume_rubrics (jobspeciality_id);

ALTER TABLE resume_rubrics ADD CONSTRAINT fk_resume_rubrics FOREIGN KEY (resume_id) REFERENCES resume (id);
ALTER TABLE resume_rubrics ADD CONSTRAINT fk_resume_jobrubric FOREIGN KEY (jobrubric_id) REFERENCES job_rubric (id);
ALTER TABLE resume_rubrics ADD CONSTRAINT fk_resume_jobspecialty FOREIGN KEY (jobspeciality_id) REFERENCES job_speciality (id);

ALTER TABLE public.resume_rubrics
  OWNER TO test;
  
  

CREATE TABLE institution (
  resume_id      bigint NOT NULL, 
  institution   varchar(1000) NOT NULL, 
  form          varchar(25) NOT NULL, 
  faculty       varchar(255) NOT NULL, 
  specialty     varchar(4000) NOT NULL, 
  city_id        int, 
  description   varchar(4000), 
  from_day      timestamp(7), 
  to_day        timestamp(7), 
  additional    bool);
  
CREATE INDEX institutions_resumeid 
  ON institutions (resume_id);

ALTER TABLE institution ADD CONSTRAINT fk_institution_city FOREIGN KEY (city_id) REFERENCES city (id);
ALTER TABLE institution ADD CONSTRAINT fk_intitution_resume FOREIGN KEY (resume_id) REFERENCES resume (id);

ALTER TABLE public.institution
  OWNER TO test;
  
CREATE TABLE resume_jobs (
  resume_id    bigint NOT NULL, 
  position    varchar(2000) NOT NULL, 
  company     varchar(2000) NOT NULL, 
  from_day   timestamp(7) NOT NULL, 
  to_day     timestamp(7), 
  city_id      int, 
  description varchar(8000));
  
CREATE INDEX resume_jobs_resumeid 
  ON resume_jobs (resumeid);

ALTER TABLE resume_jobs ADD CONSTRAINT fk_resume_jobs FOREIGN KEY (resume_id) REFERENCES resume (id);
ALTER TABLE resume_jobs ADD CONSTRAINT fk_resume_jobs_city FOREIGN KEY (city_id) REFERENCES city (id);
  
ALTER TABLE public.resume_jobs
  OWNER TO test;
