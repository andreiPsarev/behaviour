CREATE TABLE Role (
    ID   INTEGER     NOT NULL PRIMARY KEY,
    Name VARCHAR(20) NOT NULL UNIQUE
);
INSERT INTO Role (ID, Name) VALUES (1, 'User');

CREATE TABLE User (
    ID                 INTEGER      NOT NULL PRIMARY KEY AUTOINCREMENT,
    RoleID             INTEGER      NOT NULL,
    FirstName          VARCHAR(255) NOT NULL,
    LastName           VARCHAR(255) NOT NULL,
    Email              VARCHAR(255) NOT NULL UNIQUE,
    Password           VARCHAR(30)  NOT NULL,
    Gender             CHAR(1),
    Birthday           TEXT,
    Address            VARCHAR(255),
    Website            VARCHAR(255),
    ResetPasswordToken CHAR(32) UNIQUE,
    FOREIGN KEY (RoleID) REFERENCES Role(ID)
);

CREATE TABLE Post (
    ID                  INTEGER      NOT NULL PRIMARY KEY AUTOINCREMENT,
    UserID              INTEGER      NOT NULL,
    Title               VARCHAR(255) NOT NULL,
    Description         TEXT         NOT NULL,
    PublicationDateTime TEXT         NOT NULL,
    FOREIGN KEY (UserID) REFERENCES User(ID)
);

CREATE TABLE Comment (
    ID                  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    ParentID            INTEGER,
    UserID              INTEGER NOT NULL,
    PostID              INTEGER NOT NULL,
    Description         TEXT    NOT NULL,
    PublicationDateTime TEXT    NOT NULL,
    FOREIGN KEY (UserID)   REFERENCES User(ID),
    FOREIGN KEY (PostID)   REFERENCES Post(ID),
    FOREIGN KEY (ParentID) REFERENCES Comment(ID)
);
