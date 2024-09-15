-- Razvoj softver - projekat (grupa 8)
-- U nastavku ovog fajla je sav potreban kod za kreiranje baze podataka za našu aplikaciju:

CREATE DATABASE Grupa8_baza;
USE Grupa8_baza;


-- Kreiranje tabele za korisnike
CREATE TABLE korisnici (
    korisnik_id INT PRIMARY KEY AUTO_INCREMENT,
    ime VARCHAR(100) NOT NULL,
    prezime VARCHAR(100) NOT NULL,
    korisnicko_ime VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    lozinka VARCHAR(255) NOT NULL,
    datum_registracije DATE NOT NULL,
    tip_korisnika ENUM('REGULAR', 'ORGANIZATOR', 'ADMIN') NOT NULL,
    novcanik DECIMAL(10, 2) DEFAULT 1000.00
);

-- Kreiranje tabele za organizatore
CREATE TABLE organizatori (
    organizator_id INT PRIMARY KEY,
    naziv_organizacije VARCHAR(255) NOT NULL,
    kontakt_osoba VARCHAR(100) NOT NULL,
    telefon VARCHAR(20),
    adresa VARCHAR(255),
    FOREIGN KEY (organizator_id) REFERENCES korisnici(korisnik_id)
);

-- Kreiranje tabele za lokacije
CREATE TABLE lokacije (
    lokacija_id INT PRIMARY KEY AUTO_INCREMENT,
    naziv VARCHAR(255) NOT NULL,
    adresa VARCHAR(255) NOT NULL,
    grad VARCHAR(100) NOT NULL,
    kapacitet INT NOT NULL,
    slika_url VARCHAR(255)
);

-- Kreiranje tabele za sektore u lokacijama
CREATE TABLE sektori (
    sektor_id INT PRIMARY KEY AUTO_INCREMENT,
    lokacija_id INT,
    naziv_sektora VARCHAR(100) NOT NULL,
    kapacitet INT NOT NULL,
    FOREIGN KEY (lokacija_id) REFERENCES lokacije(lokacija_id)
);

-- Kreiranje tabele za događaje
CREATE TABLE dogadjaji (
    dogadjaj_id INT PRIMARY KEY AUTO_INCREMENT,
    organizator_id INT,
    naziv VARCHAR(255) NOT NULL,
    opis TEXT,
    lokacija_id INT,
    datum_vrijeme DATETIME NOT NULL,
    vrsta_dogadjaja VARCHAR(100),
    podvrsta_dogadjaja VARCHAR(100),
    slika_url VARCHAR(255),
    dodatne_informacije TEXT,
    odobreno BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (organizator_id) REFERENCES organizatori(organizator_id),
    FOREIGN KEY (lokacija_id) REFERENCES lokacije(lokacija_id)
);

-- Kreiranje tabele za karte
CREATE TABLE karte (
    karta_id INT PRIMARY KEY AUTO_INCREMENT,
    dogadjaj_id INT,
    sektor_id INT,
    cijena DECIMAL(10, 2) NOT NULL,
    mjesto VARCHAR(100),
    datum_pocetka_prodaje DATE,
    datum_zavrsetka_prodaje DATE,
    maksimalan_broj_karti_po_korisniku INT,
    uslov_otkazivanja TEXT,
    FOREIGN KEY (dogadjaj_id) REFERENCES dogadjaji(dogadjaj_id),
    FOREIGN KEY (sektor_id) REFERENCES sektori(sektor_id)
);

-- Kreiranje tabele za rezervacije
CREATE TABLE rezervacije (
    rezervacija_id INT PRIMARY KEY AUTO_INCREMENT,
    korisnik_id INT,
    karta_id INT,
    datum_rezervacije DATETIME NOT NULL,
    status ENUM('REZERVISANO', 'KUPLJENO', 'OTKAZANO') NOT NULL,
    kolicina INT NOT NULL,
    ukupna_cijena DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (korisnik_id) REFERENCES korisnici(korisnik_id),
    FOREIGN KEY (karta_id) REFERENCES karte(karta_id)
);

-- Kreiranje tabele za popuste
CREATE TABLE popusti (
    popust_id INT PRIMARY KEY AUTO_INCREMENT,
    korisnik_id INT,
    broj_kupljenih_karti INT DEFAULT 0,
    potroseni_iznos DECIMAL(10, 2) DEFAULT 0,
    ostvareni_popust DECIMAL(10, 2) DEFAULT 0,
    FOREIGN KEY (korisnik_id) REFERENCES korisnici(korisnik_id)
);

-- Ubacivanje korisnika, zatim i organizatora
INSERT INTO korisnici (ime, prezime, korisnicko_ime, email, lozinka, datum_registracije, tip_korisnika)
VALUES ('Jasmina', 'Halilovic', 'jasmina', 'jasminahalilovic@fet.ba', '123', '2024-09-11', 'REGULAR', 1000.00),
('Mladen', 'Pavlovic', 'mladen', 'mladenpavlovic@fet.ba', '111', '2024-09-10', 'ORGANIZATOR', 1000.00),
('Eldar', 'Osmanovic', 'eldar', 'eldarosmanovic@fet.ba', '111', '2024-09-12', 'ORGANIZATOR', 1000.00);

-- Ubacivanje organizatora
INSERT INTO organizatori (organizator_id, naziv_organizacije, kontakt_osoba, telefon, adresa) VALUES
(2, 'FET', 'Mladen', '0601234567', 'Ulica armije BiH'),
(3, 'CSI', 'Eldar', '061234567', 'Slatine bb.');

-- Ubacivanje lokacija 
INSERT INTO lokacije (naziv, adresa, grad, kapacitet, slika_url) VALUES
('Mejdan', 'Slatine', 'Tuzla', 1500, 'assets/slikeLokacija/slikeDogadjajastadion.png'),
('Sportska dvorana', 'Novembar 25', 'Lukavac', 330, 'assets/slikeLokacija/slikeDogadjajastadion.png'),
('Kosevo', 'Izeta Sarajlica', 'Sarajevo', 2400, 'assets/slikeLokacija/slikeDogadjajastadion.png'); 

-- Ubacivanje sektora za lokacije
INSERT INTO sektori (lokacija_id, naziv_sektora, kapacitet) VALUES
(1, 'tribine', 1000), 
(1, 'parter', 500), 
(2, 'Tribine', 100), 
(2, 'parter', 200), 
(2, 'VIP', 30), 
(3, 'Tribine', 2000),
(3, 'Parter', 300),
(3, 'VIP', 100);

-- Ubacivanje dogadjaja 
INSERT INTO dogadjaji (organizator_id, naziv, opis, lokacija_id, datum_vrijeme, vrsta_dogadjaja, podvrsta_dogadjaja, slika_url, dodatne_informacije, odobreno) VALUES
(3, 'Lepa Brena', 'Koncert Lepe Brene', 1, '2024-10-10 20:00:00', 'muzika', 'koncert', 'assets/slikeDogadjaja/brena.png', 'Koncert Lepe Brene u Mejdanu. Velika zvijezda ponovo na velikoj sceni', true),
(3, 'Aleksandra Prijovic', 'Predstava Aleksandre Prijovic', 2, '2024-10-11 21:00:00', 'kultura', 'predstava', 'assets/slikeDogadjaja/aleksandra.png', 'Predstava Aleksandre Prijovic, uzivajte u jos jednom velikom dogadjaju', true),
(3, 'Milica Pavlovic', 'Festival Milice Pavlovic', 2, '2024-10-12 20:30:00', 'muzika', 'festival', 'assets/slikeDogadjaja/boks_mec.png', 'Nakon mnogo godina imamo opet priliku da uzivamo u glasu Milice Pavlovic', true),
(3, 'Crvena jabuka', 'Nastup Crvene jabuke', 1, '2024-10-13 19:00:00', 'muzika', 'koncert', 'assets/slikeDogadjaja/jabuka.png', 'EX-YU ponovo na sceni. Ne propustite ovaj nezaboravan dogadjaj', true),
(3, 'Henny', 'Nastup Henny-a', 3, '2024-10-14 22:00:00', 'ostalo', NULL, 'assets/slikeDogadjaja/heni.png', 'Mladi izvodjac i artist koji ce vas sigurno zabaviti i ostaviti odusevljenim.', false),
(2, 'Grcka-Njemacka (kosarka)', 'Kosarkaska utakmica izmedju Grcke i Njemacke', 3, '2024-10-15 20:00:00', 'sport', 'utakmica', 'assets/slikeDogadjaja/sport.png', 'Uzivajte u atraktivnom mecu izmedju ove dvije ekipe, koje su favoriti za medalju na ovom prvenstvu', true),
(3, 'Folk_fest', 'Festival narodne muzike', 1, '2024-10-16 18:00:00', 'muzika', 'festival', 'assets/slikeDogadjaja/folk_fest.png', 'Nema izgovora, svi u Mejdan u 18:00', true),
(2, 'Skup gradskog vijeca', 'Gradsko vijece Tuzle se ponovo okuplja u Mejdanu', 1, '2024-10-17 17:05:00', 'ostalo', NULL, 'assets/slikeDogadjaja/file.png', 'Bez puno price, bit ce je na skupu. Dodjite i poslusajte.', false);

-- Ubacivanje karti 
INSERT INTO karte (dogadjaj_id, sektor_id, cijena, mjesto, datum_pocetka_prodaje, datum_zavrsetka_prodaje, maksimalan_broj_karti_po_korisniku, uslov_otkazivanja) VALUES
(1, 2, 10.00, NULL, '2024-09-11', '2024-10-09', 5, 'nema'),
(1, 1, 30.00, NULL, '2024-09-11', '2024-10-09', 5, 'nema'),
(2, 5, 10.00, NULL, '2024-09-11', '2024-10-10', 4, 'nema'),
(2, 3, 20.00, NULL, '2024-09-11', '2024-10-10', 4, 'nema'),
(2, 4, 15.00, NULL, '2024-09-11', '2024-10-10', 4, 'nema'),
(3, 3, 20.00, NULL, '2024-09-11', '2024-10-11', 5, 'nema'),
(3, 4, 15.00, NULL, '2024-09-11', '2024-10-11', 5, 'nema'),
(3, 5, 50.00, NULL, '2024-09-11', '2024-10-11', 5, 'nema'),
(4, 1, 30.00, NULL, '2024-09-12', '2024-10-12', 1, 'nema'),
(4, 2, 25.00, NULL, '2024-09-12', '2024-10-12', 1, 'nema'),
(5, 8, 100.00, NULL, '2024-09-13', '2024-10-13', 5, 'nema'),
(5, 7, 30.00, NULL, '2024-09-13', '2024-10-13', 5, 'nema'),
(5, 6, 50.00, NULL, '2024-09-13', '2024-10-13', 5, 'nema'),
(6, 8, 10.00, NULL, '2024-09-14', '2024-10-14', 5, 'nema'),
(6, 6, 15.00, NULL, '2024-09-14', '2024-10-14', 5, 'nema'),
(6, 7, 40.00, NULL, '2024-09-14', '2024-10-14', 5, 'nema'),
(7, 1, 20.00, NULL, '2024-09-14', '2024-10-15', 3, 'nema'),
(7, 2, 30.00, NULL, '2024-09-14', '2024-10-15', 3, 'nema'),
(8, 1, 30.00, NULL, '2024-09-15', '2024-10-16', 5, 'nema'),
(8, 2, 50.00, NULL, '2024-09-15', '2024-10-16', 5, 'nema');





