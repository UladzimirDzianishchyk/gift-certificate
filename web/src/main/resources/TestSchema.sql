create table IF NOT EXISTS gift_certificate
(
    id               int auto_increment not null primary key,
    name             varchar(100) unique,
    description      varchar(100),
    price            numeric NOT NULL DEFAULT 0,
    duration         int not null DEFAULT 2,
    create_date      timestamp not null DEFAULT NOW(),
    last_update_date timestamp not null DEFAULT NOW()
);