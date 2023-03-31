DROP TABLE IF EXISTS Subscription;
DROP TABLE IF EXISTS Ticket;
DROP TABLE IF EXISTS ParsedTicketData;
DROP TABLE IF EXISTS Store;
DROP TABLE IF EXISTS CustomizedCategory;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS UserTable;

CREATE TABLE IF NOT EXISTS UserTable (
    id                  SERIAL,
    role                VARCHAR         NOT NULL,
    nickname            VARCHAR(30)     NOT NULL,
    password            VARCHAR         NOT NULL,
    name                VARCHAR(50)     NOT NULL,
    email               VARCHAR(100)    NOT NULL,
    registered_at       TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT PK_User PRIMARY KEY(id),
    CONSTRAINT UNIQUE_User_nickname UNIQUE(nickname),
    CONSTRAINT UNIQUE_User_email UNIQUE(email)
);

CREATE TABLE IF NOT EXISTS Category (
    id                  SERIAL,
    name                VARCHAR(50)     NOT NULL,

    CONSTRAINT PK_Category PRIMARY KEY(id),
    CONSTRAINT UNIQUE_Category_name UNIQUE(name)
);

CREATE TABLE IF NOT EXISTS CustomizedCategory (
    user_id             INTEGER,
    category_id         INTEGER,
    max_waste_limit     DECIMAL(12, 2)  NOT NULL,

    CONSTRAINT PK_CustomizedCategory PRIMARY KEY (user_id, category_id),
    CONSTRAINT FK_CustomizedCategory_TO_UserTable
        FOREIGN KEY (user_id) REFERENCES UserTable(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT FK_CustomizedCategory_TO_Category
    FOREIGN KEY (category_id) REFERENCES Category (id)
        ON DELETE NO ACTION
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Store (
    id                  SERIAL,
    name                VARCHAR(50)     NOT NULL,

    CONSTRAINT PK_Store PRIMARY KEY (id),
    CONSTRAINT UNIQUE_Store_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS ParsedTicketData (
    id                  SERIAL,
    parsed_at           TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    supplier            VARCHAR,
    category            VARCHAR,
    subcategory         VARCHAR,
    emitted_at_date     DATE,
    emitted_at_time     VARCHAR,
    country             VARCHAR,
    language            VARCHAR,
    currency            VARCHAR,
    total_tax           DECIMAL(12, 2),
    total_amount        DECIMAL(12, 2),

    CONSTRAINT PK_ParsedTicketData PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Ticket (
    id                          SERIAL,
    name                        VARCHAR(50),
    registered_at               TIMESTAMP      NOT NULL     DEFAULT CURRENT_TIMESTAMP,
    emitted_at                  TIMESTAMP      NOT NULL,
    amount                      DECIMAL(12, 2) NOT NULL,
    picture                     BYTEA          NOT NULL,
    creator_id                  INTEGER        NOT NULL,
    custom_category_user_id     INTEGER        NOT NULL,
    custom_category_category_id INTEGER        NOT NULL,
    store_id                    INTEGER        NOT NULL,
    parsed_ticket_id            INTEGER        NOT NULL,

    CONSTRAINT PK_Ticket PRIMARY KEY (id),
    CONSTRAINT FK_Ticket_TO_UserTable
        FOREIGN KEY (creator_id) REFERENCES UserTable (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT FK_Ticket_TO_CustomizedCategory
        FOREIGN KEY (custom_category_user_id, custom_category_category_id) REFERENCES CustomizedCategory (user_id, category_id)
        ON DELETE NO ACTION
        ON UPDATE CASCADE,
    CONSTRAINT FK_Ticket_TO_Store
        FOREIGN KEY (store_id) REFERENCES Store (id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,
    CONSTRAINT FK_Ticket_TO_ParsedTicketData
        FOREIGN KEY (parsed_ticket_id) REFERENCES ParsedTicketData (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS Subscription (
    id          SERIAL,
    status      VARCHAR         NOT NULL,
    customer_id INTEGER,
    starting_at TIMESTAMP       NOT NULL    DEFAULT CURRENT_TIMESTAMP,
    ending_at   TIMESTAMP       NOT NULL,

    CONSTRAINT PK_Subscription PRIMARY KEY (id),
    CONSTRAINT FK_Subscription_TO_UserTable
        FOREIGN KEY (customer_id) REFERENCES UserTable (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


/* ************************* DATOS POR DEFECTO ************************* */
-- Datos de categorías (ver https://developers.mindee.com/docs/receipt-ocr#category)
INSERT INTO Category(name) VALUES ('accommodation');
INSERT INTO Category(name) VALUES ('food');
INSERT INTO Category(name) VALUES ('gasoline');
INSERT INTO Category(name) VALUES ('parking');
INSERT INTO Category(name) VALUES ('transport');
INSERT INTO Category(name) VALUES ('toll');
INSERT INTO Category(name) VALUES ('telecom');
INSERT INTO Category(name) VALUES ('miscellaneous');

