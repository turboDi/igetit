INSERT INTO role (id, authority) VALUES (nextval('seq_role'), 'ROLE_OAUTH_USER');

INSERT INTO person_role (role_id, person_id)
  SELECT currval('seq_role'), id
FROM person
WHERE o_auth_provider IS NOT NULL;

UPDATE person SET password = 'N/A' WHERE o_auth_provider IS NOT NULL;