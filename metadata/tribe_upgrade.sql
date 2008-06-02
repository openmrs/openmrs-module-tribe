-- disable auto commit
SET @currentAutoCommit = @@autocommit;
SET @@autocommit = 0;

START TRANSACTION; 

-- create the tribe person attribute type
INSERT INTO person_attribute_type 
	(name, description, format, creator, date_created) 
	VALUES ('Tribe', 'Tribe of the person', 'org.openmrs.module.tribe.Tribe', 1, NOW());

SET @tribeTypeId = LAST_INSERT_ID();

-- insert person attributes for patient tribe values
INSERT INTO person_attribute (person_id, value, person_attribute_type_id, creator, date_created)
		SELECT patient_id, tribe, @tribeTypeId, 1, now() FROM patient WHERE tribe IS NOT NULL; 

-- remove tribe column
ALTER TABLE patient DROP FOREIGN KEY belongs_to_tribe;
ALTER TABLE patient DROP COLUMN tribe;

-- commit everything and restore auto commit value
COMMIT;
SET @@autocommit = @currentAutoCommit;
