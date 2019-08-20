create table if not exists users
(
    id       bigint auto_increment primary key,
    login    varchar(255) not null unique,
    password varchar(255) not null,
    role     varchar(31)  not null default 'user',
    name     varchar(255) null,
    email    varchar(255) null
) comment 'User table';
