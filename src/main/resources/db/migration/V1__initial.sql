CREATE TABLE ORDERS
(
    ORDER_ID INTEGER NOT NULL,
    NAME VARCHAR(64),
    CUSTOMER VARCHAR(128) NOT NULL,
    VK VARCHAR(128) NOT NULL,
    DUE_DATE DATE NOT NULL,
    EVENT_DATE DATE NOT NULL,
    DESCRIPTION VARCHAR(1024),
    NOTES VARCHAR(1024)
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
