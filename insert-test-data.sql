-- ========================================
-- SmartRent Test Data Insert Script
-- ========================================

-- ========================================
-- DATABASE: db_locations
-- ========================================

USE db_locations;

-- Vider les tables existantes
DELETE FROM location;

-- Insérer les locations de test
INSERT INTO location (type, nom, adresse, prix_par_jour, disponible, description) VALUES
-- Appartements
('APPARTEMENT', 'Studio Montmartre', '18 Rue de la Vieuville, 75018 Paris', 50.00, 1, 'Petit studio cosy à Montmartre avec vue sur la basilique'),
('APPARTEMENT', 'Appartement 2P Marais', '42 Rue des Francs Bourgeois, 75004 Paris', 80.00, 1, 'Bel appartement 2 pièces dans le Marais, proche des transports'),
('APPARTEMENT', 'Loft moderne Bastille', '100 Rue de Charonne, 75011 Paris', 120.00, 1, 'Loft design avec hauteur sous plafond, lumineux'),

-- Maisons
('MAISON', 'Maison Bretagne 4P', 'Rue du Petit Port, 29000 Quimper', 150.00, 1, 'Charmante maison de 4 pièces en Bretagne, près de la mer'),
('MAISON', 'Villa Côte d\'Azur', 'Avenue des Pins, 06400 Cannes', 250.00, 1, 'Somptueuse villa avec piscine et vue méditerranée'),

-- Studios
('STUDIO', 'Studio Centre-Ville', '50 Boulevard Saint-Germain, 75005 Paris', 60.00, 1, 'Studio chaleureux en plein cœur de Paris'),
('STUDIO', 'Studio Aéroport CDG', '2 Rue de l\'Aviation, 95700 Roissy-en-Brie', 45.00, 0, 'Studio pratique près de l\'aéroport CDG'),

-- Voitures (type location)
('VOITURE', 'Renault Clio', 'Parking Central, Paris', 40.00, 1, 'Voiture de location compacte et économique'),
('VOITURE', 'BMW Série 5', 'Parking Prestige, Paris', 120.00, 1, 'Voiture de prestige pour déplacements professionnels'),

-- Équipements
('EQUIPEMENT', 'Vélo électrique', 'Station Vélib\', Paris', 15.00, 1, 'Vélo électrique pour se déplacer facilement'),
('EQUIPEMENT', 'Trottinette électrique', 'Station Trotty, Paris', 10.00, 1, 'Trottinette pratique et ludique');

SELECT COUNT(*) as 'Total Locations' FROM location;
SELECT * FROM location;

-- ========================================
-- DATABASE: db_reservation
-- ========================================

USE db_reservation;

-- Vider les tables existantes
DELETE FROM reservation;

-- Insérer les réservations de test
INSERT INTO reservation (location_id, client_nom, client_email, date_debut, date_fin, statut, prix_total) VALUES
-- Réservations confirmées
(1, 'Jean Martin', 'jean.martin@email.com', '2026-01-10', '2026-01-15', 'CONFIRMÉE', 250.00),
(2, 'Marie Dubois', 'marie.dubois@email.com', '2026-01-12', '2026-01-17', 'CONFIRMÉE', 400.00),

-- Réservations en attente
(3, 'Pierre Leclerc', 'pierre.leclerc@email.com', '2026-01-15', '2026-01-22', 'EN_ATTENTE', 840.00),

-- Réservations annulées
(4, 'Sophie Bernard', 'sophie.bernard@email.com', '2025-12-20', '2025-12-25', 'ANNULÉE', 750.00),

-- Plus de réservations pour avoir plus de données
(5, 'Antoine Renard', 'antoine.renard@email.com', '2026-01-20', '2026-01-27', 'EN_ATTENTE', 1500.00),
(6, 'Claire Moreau', 'claire.moreau@email.com', '2026-02-01', '2026-02-05', 'CONFIRMÉE', 240.00),
(7, 'Luc Fontaine', 'luc.fontaine@email.com', '2026-02-03', '2026-02-08', 'CONFIRMÉE', 200.00),
(8, 'Nathalie Petit', 'nathalie.petit@email.com', '2026-01-25', '2026-01-28', 'EN_ATTENTE', 360.00);

SELECT COUNT(*) as 'Total Reservations' FROM reservation;
SELECT * FROM reservation;

-- ========================================
-- Résumé des données
-- ========================================

SELECT 'LOCATIONS PAR TYPE' as '';
SELECT type, COUNT(*) as nombre FROM location GROUP BY type;

SELECT '' as '';
SELECT 'RÉSERVATIONS PAR STATUT' as '';
SELECT statut, COUNT(*) as nombre FROM reservation GROUP BY statut;
