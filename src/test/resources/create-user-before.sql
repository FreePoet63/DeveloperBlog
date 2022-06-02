delete from usr_role;
delete from usr;

insert into usr(id, active, password, username) values
(1, true, '$2a$08$kEclxmrf7kim1FmRox1AROxCfBIvmckWcA/dsmy0H5sV5cXaZHUta', 'Natasha'),
(2, true, '"$2a$08$kEclxmrf7kim1FmRox1AROxCfBIvmckWcA/dsmy0H5sV5cXaZHUta"', 'Olesya');

insert into usr_role(user_id, roles) values
(1, 'USER'), (1, 'ADMIN'),
(2, 'USER') ;