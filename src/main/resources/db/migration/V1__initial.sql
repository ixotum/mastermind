CREATE TABLE ORDERS
(
    ORDER_ID INTEGER NOT NULL,
    NAME VARCHAR(128),
    CUSTOMER VARCHAR(128) NOT NULL,
    ADDRESS VARCHAR(254),
    VK VARCHAR(128) NOT NULL,
    DUE_DATE DATE NOT NULL,
    EVENT_DATE DATE NOT NULL,
    DESCRIPTION VARCHAR(1024),
    NOTES VARCHAR(1024),
    STATUS INT NOT NULL
);
CREATE TABLE SETTINGS
(
    LAST_ORDER_ID INTEGER DEFAULT 0 NOT NULL
);

CREATE TABLE ORDER_STRUCTURE_COMPONENTS
(
    ID INTEGER AUTO_INCREMENT (1) NOT NULL,
    ORDER_ID INTEGER NOT NULL,
    POSITION INTEGER NOT NULL,
    ITEM VARCHAR(32) NOT NULL,
    PRICE DECIMAL(65535,32767)
);

CREATE TABLE PAYMENT
(
    ORDER_ID INTEGER NOT NULL,
    DATE DATE NOT NULL,
    PAYMENT DECIMAL(65535,32767) NOT NULL
);

CREATE TABLE EXPENSE
(
    ID INTEGER AUTO_INCREMENT (1) NOT NULL,
    DATE DATE NOT NULL,
    TYPE VARCHAR(32) NOT NULL,
    DESCRIPTION VARCHAR(64) NOT NULL
);
CREATE UNIQUE INDEX "unique_id_INDEX_1" ON EXPENSE (ID);
