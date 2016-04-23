ALTER TABLE buy ALTER COLUMN name TYPE TEXT;
ALTER TABLE buy ADD name_fts TSVECTOR;

UPDATE buy SET name_fts = to_tsvector('russian', name);

CREATE OR REPLACE FUNCTION set_name_fts()
  RETURNS TRIGGER
AS $$
BEGIN
  NEW.name_fts := to_tsvector('russian', NEW.name);

  RETURN NEW;
END $$ LANGUAGE plpgsql;

CREATE TRIGGER set_name_fts_trigger
BEFORE INSERT OR UPDATE ON buy
FOR EACH ROW
EXECUTE PROCEDURE set_name_fts();

CREATE INDEX idx_name_fts ON buy USING gin(name_fts);