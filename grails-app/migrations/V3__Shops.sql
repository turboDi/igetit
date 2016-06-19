CREATE TABLE shop (
  id bigint NOT NULL,
  name character varying(255),
  source_id character varying(255),
  city_description character varying(255),
  city_place_id character varying(255),
  eshop boolean NOT NULL DEFAULT FALSE,
  deleted boolean NOT NULL DEFAULT FALSE,
  update_time timestamp without time zone
);

CREATE SEQUENCE seq_shop
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  OWNED BY shop.id;

ALTER TABLE ONLY shop ADD CONSTRAINT shop_pk PRIMARY KEY (id);

ALTER TABLE ONLY buy ADD shop_id bigint;
ALTER TABLE ONLY buy ADD city_description character varying(255);
ALTER TABLE ONLY buy ADD city_place_id character varying(255);
ALTER TABLE ONLY buy ADD CONSTRAINT fk_buy_shop FOREIGN KEY (shop_id) REFERENCES shop(id);

CREATE OR REPLACE FUNCTION upsert_shop(
  _source_id varchar,
  _name varchar,
  _city_description varchar,
  _city_place_id varchar,
  _update_time timestamp without time zone)
  RETURNS void AS
  $BODY$
  UPDATE shop SET name = _name, update_time = _update_time, deleted = FALSE WHERE source_id = _source_id;
  INSERT INTO shop (id, name, source_id, city_description, city_place_id, update_time)
      SELECT nextval('seq_shop'), _name, _source_id, _city_description, _city_place_id, _update_time
      WHERE NOT EXISTS (SELECT 1 FROM shop WHERE source_id = _source_id);
  $BODY$
LANGUAGE sql VOLATILE
COST 100;

INSERT INTO city (id, description, place_id) VALUES (nextval('seq_city'), 'Екатеринбург', 'ChIJS9tioOplwUMRIH9W99dDAtU');
INSERT INTO city (id, description, place_id) VALUES (nextval('seq_city'), 'Казань', 'ChIJmc2sfCutXkERZYyttbl3y38');
INSERT INTO city (id, description, place_id) VALUES (nextval('seq_city'), 'Краснодар', 'ChIJszVFcWRF8EARtr7EVk95IPc');
INSERT INTO city (id, description, place_id) VALUES (nextval('seq_city'), 'Москва', 'ChIJybDUc_xKtUYRTM9XV8zWRD0');
INSERT INTO city (id, description, place_id) VALUES (nextval('seq_city'), 'Нижний Новгород', 'ChIJUTID7vnVUUERqleWhM_A0yg');
INSERT INTO city (id, description, place_id) VALUES (nextval('seq_city'), 'Новосибирск', 'ChIJl03MkOHl30IRhenT4XMGOps');
INSERT INTO city (id, description, place_id) VALUES (nextval('seq_city'), 'Ростов-на-Дону', 'ChIJ77a0w3fH40ARBE2L5FG0SII');
INSERT INTO city (id, description, place_id) VALUES (nextval('seq_city'), 'Самара', 'ChIJ03nYK-IYZkERCwM6u6nNlbo');
INSERT INTO city (id, description, place_id) VALUES (nextval('seq_city'), 'Санкт-Петербург', 'ChIJ7WVKx4w3lkYR_46Eqz9nx20');