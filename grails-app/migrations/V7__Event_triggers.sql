ALTER TABLE event ADD comment_id bigint;
ALTER TABLE event DROP comment;

----------------------------------------
----------Comment Triggers--------------
----------------------------------------

CREATE OR REPLACE FUNCTION create_comment_event()
  RETURNS TRIGGER
AS $$
DECLARE
BEGIN
  INSERT INTO event (id, buy_id, date_created, effector_id, initiator_id, "type", comment_id)
  SELECT nextval('seq_event'), NEW.buy_id, NEW.date_created, b.owner_id, NEW.author_id, 'comment', NEW.id
  FROM buy b WHERE b.id = NEW.buy_id;

  RETURN NEW;
END $$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION delete_comment_event()
  RETURNS TRIGGER
AS $$
DECLARE
BEGIN
  DELETE FROM event WHERE comment_id = OLD.id AND "type" = 'comment';

  RETURN OLD;
END $$ LANGUAGE plpgsql;

CREATE TRIGGER create_comment_event
AFTER INSERT ON comment
FOR EACH ROW
EXECUTE PROCEDURE create_comment_event();

CREATE TRIGGER delete_comment_event
AFTER UPDATE ON comment
FOR EACH ROW
WHEN (NEW.deleted AND NOT OLD.deleted)
EXECUTE PROCEDURE delete_comment_event();

CREATE TRIGGER update_comment_event
AFTER UPDATE ON comment
FOR EACH ROW
WHEN (NOT NEW.deleted AND OLD.deleted)
EXECUTE PROCEDURE create_comment_event();

----------------------------------------
-------------Like Triggers--------------
----------------------------------------

CREATE OR REPLACE FUNCTION create_like_event()
  RETURNS TRIGGER
AS $$
DECLARE
BEGIN
  IF NEW.type = 'comment' THEN

    INSERT INTO event (id, buy_id, date_created, effector_id, initiator_id, "type", comment_id)
    SELECT nextval('seq_event'), c.buy_id, NEW.date_created, c.author_id, NEW.liker_id, 'like', NEW.like_ref
    FROM comment c WHERE c.id = NEW.like_ref;

  ELSEIF NEW.type = 'buy' THEN

    INSERT INTO event (id, buy_id, date_created, effector_id, initiator_id, "type", comment_id)
    SELECT nextval('seq_event'), NEW.like_ref, NEW.date_created, b.owner_id, NEW.liker_id, 'like', NULL
    FROM buy b WHERE b.id = NEW.like_ref;

  END IF;

  RETURN NEW;
END $$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION delete_like_event()
  RETURNS TRIGGER
AS $$
DECLARE
BEGIN

  DELETE FROM event
  WHERE initiator_id = OLD.liker_id AND "type" = 'like' AND
        (OLD.type = 'buy' AND buy_id = OLD.like_ref OR OLD.type = 'comment' AND comment_id = OLD.like_ref);

  RETURN OLD;
END $$ LANGUAGE plpgsql;

CREATE TRIGGER create_like_event
AFTER INSERT ON l_like
FOR EACH ROW
EXECUTE PROCEDURE create_like_event();

CREATE TRIGGER delete_like_event
AFTER DELETE ON l_like
FOR EACH ROW
EXECUTE PROCEDURE delete_like_event();

----------------------------------------
----------Follower Triggers-------------
----------------------------------------

CREATE OR REPLACE FUNCTION create_person_follower_event()
  RETURNS TRIGGER
AS $$
DECLARE
BEGIN
  INSERT INTO event (id, buy_id, date_created, effector_id, initiator_id, "type", comment_id)
  VALUES (nextval('seq_event'), NULL , NEW.date_created, NEW.person_id, NEW.follower_id, 'personFollower', NULL);

  RETURN NEW;
END $$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION delete_person_follower_event()
  RETURNS TRIGGER
AS $$
DECLARE
BEGIN
  DELETE FROM event WHERE effector_id = OLD.person_id AND initiator_id = OLD.follower_id AND "type" = 'personFollower';

  RETURN OLD;
END $$ LANGUAGE plpgsql;

CREATE TRIGGER create_person_follower_event
AFTER INSERT ON person_follower
FOR EACH ROW
EXECUTE PROCEDURE create_person_follower_event();

CREATE TRIGGER delete_person_follower_event
AFTER UPDATE ON person_follower
FOR EACH ROW
WHEN (NEW.deleted AND NOT OLD.deleted)
EXECUTE PROCEDURE delete_person_follower_event();

CREATE TRIGGER update_person_follower_event
AFTER UPDATE ON person_follower
FOR EACH ROW
WHEN (NOT NEW.deleted AND OLD.deleted)
EXECUTE PROCEDURE create_person_follower_event();