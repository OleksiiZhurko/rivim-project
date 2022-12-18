create table if not exists roles
(
    role_id bigserial constraint roles_pk primary key,
    name    varchar not null
);

alter table roles owner to postgres;

create unique index if not exists roles_name_uindex on roles (name);

create table if not exists users
(
    user_id bigserial constraint users_pk primary key,
    login      varchar not null,
    password   varchar not null,
    email      varchar,
    first_name varchar not null,
    last_name  varchar not null,
    birthday   date    not null,
    role_id bigint not null constraint users_roles_role_id_fk references roles
);

alter table users owner to postgres;

create unique index if not exists users_email_uindex on users (email);

create unique index if not exists users_login_uindex on users (login);

