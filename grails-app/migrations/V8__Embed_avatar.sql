ALTER TABLE person ADD avatar_filename VARCHAR(255);
ALTER TABLE person ADD avatar_folder_id VARCHAR(255);

UPDATE person
SET avatar_filename = i.filename, avatar_folder_id = i.folder_id
FROM image i
WHERE i.id = avatar_id;

ALTER TABLE person DROP CONSTRAINT fk_person_avatar;
DELETE FROM image WHERE id IN (SELECT avatar_id FROM person);
ALTER TABLE person DROP avatar_id;

