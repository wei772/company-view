CREATE TABLE Kasutaja (
  kasutajaID SERIAL,
  kasutajaTyypID INTEGER,
  kontoStaatusID INTEGER,
  aadress TEXT,
  eesnimi CHAR(255),
  email CHAR(255),
  konto_nimi CHAR(64),
  matrikli_kood CHAR(32),
  organisatsiooni_nimi CHAR(255),
  parool CHAR(64),
  perekonnanimi CHAR(255),
  tel_nr CHAR(255)
);

CREATE TABLE KontoStaatus (
  kontoStaatusID SERIAL,
  nimetus CHAR(255)
);

CREATE TABLE KasutajaTyyp (
  kasutajaTyypID SERIAL,
  nimetus CHAR(255)
);