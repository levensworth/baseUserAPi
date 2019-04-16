create table users(
        id serial,
        firstname varchar(255) not null,
        surname varchar(255) not null,
        email varchar(255) not null,
        password varchar(255) not null,
        birthday timestamp default now(),
        primary key (id),
        unique(email)
);