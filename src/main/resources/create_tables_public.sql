-- Table: public."PurchaseOrder"

-- DROP TABLE public."PurchaseOrder";

CREATE TABLE public."PurchaseOrder"
(
    purchaseordernumber integer NOT NULL,
    orderdate date NOT NULL,
    deliverynotes character varying(250) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "PurchaseOrder_pkey" PRIMARY KEY (purchaseordernumber)
)

TABLESPACE pg_default;

ALTER TABLE public."PurchaseOrder"
    OWNER to postgres;
	
-- Table: public."Item"

-- DROP TABLE public."Item";

CREATE TABLE public."Item"
(
    purchaseordernumber integer NOT NULL,
    partnumber character varying(6) COLLATE pg_catalog."default" NOT NULL,
    productname character varying(20) COLLATE pg_catalog."default" NOT NULL,
    quantity integer NOT NULL,
    usprice double precision NOT NULL,
    shipdate date,
    comment character varying(250) COLLATE pg_catalog."default",
    CONSTRAINT "Item_pkey" PRIMARY KEY (purchaseordernumber, partnumber),
    CONSTRAINT "fk_purchaseOrder" FOREIGN KEY (purchaseordernumber)
        REFERENCES public."PurchaseOrder" (purchaseordernumber) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE public."Item"
    OWNER to postgres;

-- Table: public."Address"

-- DROP TABLE public."Address";

CREATE TABLE public."Address"
(
    purchaseordernumber integer NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    street character varying(100) COLLATE pg_catalog."default" NOT NULL,
    city character varying(50) COLLATE pg_catalog."default" NOT NULL,
    state character varying(20) COLLATE pg_catalog."default" NOT NULL,
    zip character varying(5) COLLATE pg_catalog."default" NOT NULL,
    country character varying(15) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "Address_pkey" PRIMARY KEY (purchaseordernumber, name),
    CONSTRAINT "fk_purchaseOrder" FOREIGN KEY (purchaseordernumber)
        REFERENCES public."PurchaseOrder" (purchaseordernumber) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE public."Address"
    OWNER to postgres;	