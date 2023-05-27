package mimsoft.io.config

const val USER =
    """create table users
(
    id         bigserial primary key,
    phone      text not null,
    first_name text not null,
    last_name  text not null,
    image      text not null,
    birth_day  timestamp(6),
    created    timestamp(6),
    updated    timestamp(6),
    deleted    bool default false
)"""

const val BRANCH =
    """create table branch
(
    id        bigserial
        primary key,
    name_uz   text not null,
    name_ru   text not null,
    name_eng  text not null,
    longitude double precision not null,
    latitude  double precision not null,
    address   text not null,
    created   timestamp(6),
    updated   timestamp(6),
    deleted   boolean default false
)"""

const val CATEGORY =
    """CREATE TABLE category
(
    id       bigserial primary key ,
    name_uz  text not null,
    name_ru  text not null,
    name_eng text not null,
    image    text not null,
    deleted  bool default false,
    created  timestamp(6),
    updated  timestamp(6)
)"""

const val EXTRA =
    """CREATE TABLE extra
(
    id              BIGSERIAL PRIMARY KEY,
    name_uz         TEXT not null,
    name_ru         TEXT not null,
    name_eng        TEXT not null,
    price           DOUBLE PRECISION,
    description_uz  TEXT not null,
    description_ru  TEXT not null,
    description_eng TEXT not null,
    deleted         BOOLEAN DEFAULT false,
    created         TIMESTAMP(6),
    updated         TIMESTAMP(6)
)"""

const val LABEL =
    """CREATE TABLE label
(
    id         BIGSERIAL PRIMARY KEY,
    menu_id    BIGINT not null ,
    name_uz    TEXT not null ,
    name_ru    TEXT not null,
    name_eng   TEXT not null,
    text_color TEXT not null,
    bg_color   TEXT not null,
    icon       TEXT not null,
    deleted    BOOLEAN DEFAULT false,
    created    TIMESTAMP(6),
    updated    TIMESTAMP(6)
)"""

const val MENU =
    """CREATE TABLE menu
(
    id       BIGSERIAL PRIMARY KEY,
    name_uz  TEXT not null ,
    name_ru  TEXT not null ,
    name_eng TEXT not null ,
    deleted  BOOLEAN DEFAULT false,
    created  TIMESTAMP(6),
    updated  TIMESTAMP(6)
);"""

const val OPTION =
    """CREATE TABLE option (
    id        bigserial PRIMARY KEY,
    name_uz   text not null,
    name_ru   text not null,
    name_eng  text not null,
    description_uz text not null,
    description_ru text not null,
    description_eng text not null,
    image     text not null,
    price     double precision not null,
    created   timestamp(6),
    updated   timestamp(6),
    deleted   boolean DEFAULT false
);"""

const val PRODUCT =
    """CREATE TABLE product (
    id        bigserial PRIMARY KEY,
    menu_id   bigint not null,
    name_uz   text not null,
    name_ru   text not null,
    name_eng  text not null,
    description_uz text not null,
    description_ru text not null,
    description_eng text not null,
    image     text not null,
    price     double precision not null,
    created   timestamp(6),
    updated   timestamp(6),
    deleted   boolean DEFAULT false
);"""

const val RESTAURANT =
    """CREATE TABLE restaurant
(
    id       bigserial PRIMARY KEY,
    name_uz  text not null,
    name_ru  text not null,
    name_eng text not null,
    logo     text not null,
    domain   text not null,
    created  timestamp(6),
    updated  timestamp(6),
    deleted  boolean DEFAULT false
);"""


const val TABLE =
    """CREATE TABLE table
(
    id       bigserial PRIMARY KEY,
    name     text not null,
    roomId   bigint not null,
    qr       text not null,
    restaurant_id    text not null
);"""

const val ADDRESS =
    """CREATE TABLE address(
        id bigserial PRIMARY KEY,
        type text,
        name text,
        details text,
        description text,
        latitude decimal,
        longitude decimal,
        created timestamp,
        updated timestamp,
        deleted boolean
    );
);"""





