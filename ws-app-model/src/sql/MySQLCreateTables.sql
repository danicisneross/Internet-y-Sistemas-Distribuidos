-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------

DROP TABLE Response;
DROP TABLE Event;

-- -------------------------------EVENT----------------------------------------
CREATE TABLE Event (
    name VARCHAR(255) COLLATE latin1_bin NOT NULL,
    description VARCHAR(1024) COLLATE latin1_bin NOT NULL,
    creationDate DATETIME NOT NULL,
    startDate DATETIME NOT NULL,
    duration DOUBLE NOT NULL,
    eventId BIGINT NOT NULL AUTO_INCREMENT,
    cancelation BOOLEAN NOT NULL,
    numberAssistance INT NOT NULL,
    numberNoAssistance INT NOT NULL,
    CONSTRAINT EventIdPK PRIMARY KEY(eventId),
    CONSTRAINT validNumberAssistance CHECK (numberAssistance >= 0 and numberAssistance <= 10000),
    CONSTRAINT validNumberNoAssistance CHECK (numberNoAssistance >= 0 and numberNoAssistance <= 10000))
    ENGINE = InnoDB;

-- -----------------------------RESPONSE----------------------------------------
CREATE TABLE Response (
    responseId BIGINT NOT NULL AUTO_INCREMENT,
    eventId BIGINT NOT NULL,
    userEmail VARCHAR(100) COLLATE latin1_bin NOT NULL,
    dateAnswer DATETIME NOT NULL,
    assistance BOOLEAN NOT NULL,
    CONSTRAINT ResponseIdPK PRIMARY KEY(responseId),
    CONSTRAINT ResponseEventIdFK FOREIGN KEY(eventId)
        REFERENCES Event(eventId) ON DELETE CASCADE)
    ENGINE = InnoDB;