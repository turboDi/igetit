CREATE TABLE buy (
  id bigint NOT NULL,
  category_id bigint,
  date_created timestamp without time zone NOT NULL,
  deleted boolean NOT NULL,
  name character varying(255),
  owner_id bigint NOT NULL,
  price_currency character varying(255),
  price_value numeric(19,2)
);

CREATE TABLE buy_image (
  buy_images_id bigint,
  image_id bigint,
  images_idx integer
);

CREATE TABLE category (
  id bigint NOT NULL,
  name character varying(255) NOT NULL
);

CREATE TABLE city (
  id bigint NOT NULL,
  description character varying(255) NOT NULL,
  place_id character varying(255) NOT NULL
);

CREATE TABLE comment (
  id bigint NOT NULL,
  author_id bigint NOT NULL,
  buy_id bigint NOT NULL,
  date_created timestamp without time zone NOT NULL,
  deleted boolean NOT NULL,
  text character varying(1000) NOT NULL
);

CREATE TABLE event (
  id bigint NOT NULL,
  buy_id bigint,
  comment character varying(255),
  date_created timestamp without time zone NOT NULL,
  effector_id bigint NOT NULL,
  initiator_id bigint NOT NULL,
  type character varying(255) NOT NULL
);

CREATE TABLE image (
  id bigint NOT NULL,
  filename character varying(255) NOT NULL,
  folder_id character varying(255) NOT NULL
);

CREATE TABLE l_like (
  id bigint NOT NULL,
  date_created timestamp without time zone NOT NULL,
  like_ref bigint NOT NULL,
  liker_id bigint NOT NULL,
  type character varying(255) NOT NULL
);

CREATE TABLE person (
  id bigint NOT NULL,
  account_expired boolean NOT NULL,
  account_locked boolean NOT NULL,
  avatar_id bigint,
  city_description character varying(255),
  city_place_id character varying(255),
  confirm_token character varying(255) NOT NULL,
  date_created timestamp without time zone NOT NULL,
  deleted boolean NOT NULL,
  email character varying(255),
  email_confirmed boolean NOT NULL,
  enabled boolean NOT NULL,
  full_name character varying(255) NOT NULL,
  last_activity timestamp without time zone NOT NULL,
  o_auth_provider character varying(255),
  password character varying(255) NOT NULL,
  password_expired boolean NOT NULL,
  username character varying(255) NOT NULL
);

CREATE TABLE person_favorite (
  id bigint NOT NULL,
  buy_id bigint NOT NULL,
  person_id bigint NOT NULL
);

CREATE TABLE person_follower (
  id bigint NOT NULL,
  date_created timestamp without time zone NOT NULL,
  deleted boolean NOT NULL,
  follower_id bigint NOT NULL,
  person_id bigint NOT NULL
);

CREATE TABLE person_role (
  role_id bigint NOT NULL,
  person_id bigint NOT NULL
);

CREATE TABLE role (
  id bigint NOT NULL,
  authority character varying(255) NOT NULL
);

CREATE SEQUENCE seq_buy
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  OWNED BY buy.id;

CREATE SEQUENCE seq_category
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  OWNED BY category.id;

CREATE SEQUENCE seq_city
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  OWNED BY city.id;

CREATE SEQUENCE seq_comment
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  OWNED BY comment.id;

CREATE SEQUENCE seq_event
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  OWNED BY event.id;

CREATE SEQUENCE seq_image
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  OWNED BY image.id;

CREATE SEQUENCE seq_l_like
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  OWNED BY l_like.id;

CREATE SEQUENCE seq_person
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  OWNED BY person.id;

CREATE SEQUENCE seq_person_favorite
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  OWNED BY person_favorite.id;

CREATE SEQUENCE seq_person_follower
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  OWNED BY person_follower.id;

CREATE SEQUENCE seq_price
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  OWNED BY price.id;

CREATE SEQUENCE seq_role
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  OWNED BY role.id;


ALTER TABLE ONLY buy
ADD CONSTRAINT buy_pk PRIMARY KEY (id);

ALTER TABLE ONLY category
ADD CONSTRAINT category_pk PRIMARY KEY (id);

ALTER TABLE ONLY city
ADD CONSTRAINT city_pk PRIMARY KEY (id);

ALTER TABLE ONLY comment
ADD CONSTRAINT comment_pk PRIMARY KEY (id);

ALTER TABLE ONLY event
ADD CONSTRAINT event_pk PRIMARY KEY (id);

ALTER TABLE ONLY image
ADD CONSTRAINT image_folder_id_key UNIQUE (folder_id);

ALTER TABLE ONLY image
ADD CONSTRAINT image_pk PRIMARY KEY (id);

ALTER TABLE ONLY l_like
ADD CONSTRAINT l_like_pk PRIMARY KEY (id);

ALTER TABLE ONLY l_like
ADD CONSTRAINT l_like_type_like_ref_liker_id_key UNIQUE (type, like_ref, liker_id);

ALTER TABLE ONLY person
ADD CONSTRAINT person_email_key UNIQUE (email);

ALTER TABLE ONLY person_favorite
ADD CONSTRAINT person_favorite_buy_id_person_id_key UNIQUE (buy_id, person_id);

ALTER TABLE ONLY person_favorite
ADD CONSTRAINT person_favorite_pk PRIMARY KEY (id);

ALTER TABLE ONLY person_follower
ADD CONSTRAINT person_follower_follower_id_person_id_key UNIQUE (follower_id, person_id);

ALTER TABLE ONLY person_follower
ADD CONSTRAINT person_follower_pk PRIMARY KEY (id);

ALTER TABLE ONLY person
ADD CONSTRAINT person_pk PRIMARY KEY (id);

ALTER TABLE ONLY person_role
ADD CONSTRAINT person_role_pk PRIMARY KEY (role_id, person_id);

ALTER TABLE ONLY person
ADD CONSTRAINT person_username_key UNIQUE (username);

ALTER TABLE ONLY price
ADD CONSTRAINT price_pk PRIMARY KEY (id);

ALTER TABLE ONLY role
ADD CONSTRAINT role_authority_key UNIQUE (authority);

ALTER TABLE ONLY role
ADD CONSTRAINT role_pk PRIMARY KEY (id);

ALTER TABLE ONLY buy
ADD CONSTRAINT fk_buy_category FOREIGN KEY (category_id) REFERENCES category(id);

ALTER TABLE ONLY buy
ADD CONSTRAINT fk_buy_owner FOREIGN KEY (owner_id) REFERENCES person(id);

ALTER TABLE ONLY comment
ADD CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES person(id);

ALTER TABLE ONLY comment
ADD CONSTRAINT fk_comment_buy FOREIGN KEY (buy_id) REFERENCES buy(id);

ALTER TABLE ONLY event
ADD CONSTRAINT fk_event_initiator FOREIGN KEY (initiator_id) REFERENCES person(id);

ALTER TABLE ONLY event
ADD CONSTRAINT fk_event_effector FOREIGN KEY (effector_id) REFERENCES person(id);

ALTER TABLE ONLY event
ADD CONSTRAINT fk_event_buy FOREIGN KEY (buy_id) REFERENCES buy(id);

ALTER TABLE ONLY person_follower
ADD CONSTRAINT fk_p_fol_person FOREIGN KEY (person_id) REFERENCES person(id);

ALTER TABLE ONLY person_follower
ADD CONSTRAINT fk_p_fol_follower FOREIGN KEY (follower_id) REFERENCES person(id);

ALTER TABLE ONLY person
ADD CONSTRAINT fk_person_avatar FOREIGN KEY (avatar_id) REFERENCES image(id);

ALTER TABLE ONLY person_favorite
ADD CONSTRAINT fk_p_fav_person FOREIGN KEY (person_id) REFERENCES person(id);

ALTER TABLE ONLY person_favorite
ADD CONSTRAINT fk_p_fav_buy FOREIGN KEY (buy_id) REFERENCES buy(id);

ALTER TABLE ONLY buy_image
ADD CONSTRAINT fk_b_i_image FOREIGN KEY (image_id) REFERENCES image(id);

ALTER TABLE ONLY person_role
ADD CONSTRAINT fk_p_r_role FOREIGN KEY (role_id) REFERENCES role(id);

ALTER TABLE ONLY person_role
ADD CONSTRAINT fk_p_r_person FOREIGN KEY (person_id) REFERENCES person(id);