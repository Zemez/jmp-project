create table if not exists users
(
    id       bigint auto_increment primary key,
    login    varchar(255) not null unique,
    password varchar(255) not null,
    name     varchar(255) null,
    email    varchar(255) null
) comment 'User table';
