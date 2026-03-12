-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mer. 11 mars 2026 à 09:59
-- Version du serveur : 9.1.0
-- Version de PHP : 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `quiz_ece`
--

-- --------------------------------------------------------

--
-- Structure de la table `difficulte`
--

DROP TABLE IF EXISTS `difficulte`;
CREATE TABLE IF NOT EXISTS `difficulte` (
  `id_difficulte` int NOT NULL AUTO_INCREMENT,
  `niveau` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id_difficulte`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `difficulte`
--

INSERT INTO `difficulte` (`id_difficulte`, `niveau`) VALUES
(1, 'Facile'),
(2, 'Moyen'),
(3, 'Difficile');

-- --------------------------------------------------------

--
-- Structure de la table `question`
--

DROP TABLE IF EXISTS `question`;
CREATE TABLE IF NOT EXISTS `question` (
  `id_question` int NOT NULL AUTO_INCREMENT,
  `libelle` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `choix1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `choix2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `choix3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `choix4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `reponse` tinyint NOT NULL,
  `quiz_id` int NOT NULL,
  `difficulte_id` int NOT NULL,
  PRIMARY KEY (`id_question`),
  KEY `quiz_id` (`quiz_id`),
  KEY `difficulte_id` (`difficulte_id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `question`
--

INSERT INTO `question` (`id_question`, `libelle`, `choix1`, `choix2`, `choix3`, `choix4`, `reponse`, `quiz_id`, `difficulte_id`) VALUES
(1, 'En quelle année a eu lieu la Révolution française ?', '1789', '1799', '1776', '1815', 1, 1, 1),
(2, 'Qui a été le premier président des États-Unis ?', 'Thomas Jefferson', 'Abraham Lincoln', 'George Washington', 'John Adams', 3, 1, 1),
(3, 'Quelle civilisation a construit les pyramides de Gizeh ?', 'Les Romains', 'Les Grecs', 'Les Égyptiens', 'Les Mayas', 3, 1, 1),
(4, 'Pendant combien d\'années a duré la Guerre de Cent Ans ?', '100 ans', '116 ans', '99 ans', '150 ans', 2, 1, 2),
(5, 'Qui a écrit \"Le Prince\" ?', 'Machiavel', 'Rousseau', 'Voltaire', 'Diderot', 1, 1, 3),
(6, 'Quelle est la capitale du Japon ?', 'Séoul', 'Pékin', 'Tokyo', 'Bangkok', 3, 2, 1),
(7, 'Quel est le plus grand désert du monde ?', 'Le Sahara', 'Le désert d\'Arabie', 'Le désert de Gobi', 'L\'Antarctique', 4, 2, 1),
(8, 'Combien de continents y a-t-il sur Terre ?', '5', '6', '7', '8', 3, 2, 1),
(9, 'Quel est le fleuve le plus long du monde ?', 'L\'Amazone', 'Le Nil', 'Le Yangtsé', 'Le Mississippi', 2, 2, 2),
(10, 'Quel est le pays le plus peuplé du monde ?', 'Les États-Unis', 'L\'Inde', 'La Chine', 'L\'Indonésie', 3, 2, 2),
(11, 'Quelle planète est surnommée \"la planète rouge\" ?', 'Vénus', 'Mars', 'Jupiter', 'Saturne', 2, 3, 1),
(12, 'Combien d\'os y a-t-il dans le corps humain adulte ?', '206', '196', '216', '226', 1, 3, 1),
(13, 'Quel gaz les plantes absorbent-elles pendant la photosynthèse ?', 'L\'oxygène', 'L\'azote', 'Le dioxyde de carbone', 'L\'hydrogène', 3, 3, 1),
(14, 'Quelle est la vitesse de la lumière dans le vide ?', '300 000 km/s', '150 000 km/s', '450 000 km/s', '600 000 km/s', 1, 3, 2),
(15, 'Qui a découvert la pénicilline ?', 'Louis Pasteur', 'Alexander Fleming', 'Marie Curie', 'Robert Koch', 2, 3, 3),
(16, 'Combien de joueurs y a-t-il dans une équipe de football ?', '10', '11', '12', '9', 2, 4, 1),
(17, 'Quel pays a remporté la Coupe du Monde de football 2018 ?', 'Allemagne', 'Brésil', 'France', 'Argentine', 3, 4, 1),
(18, 'En tennis, quel est le tournoi du Grand Chelem qui se joue sur gazon ?', 'Roland-Garros', 'Wimbledon', 'US Open', 'Open d\'Australie', 2, 4, 1),
(19, 'Quel sportif est surnommé \"The King\" ?', 'Michael Jordan', 'LeBron James', 'Roger Federer', 'Lionel Messi', 2, 4, 2),
(20, 'Combien de médailles d\'or a remporté Michael Phelps aux Jeux Olympiques ?', '23', '19', '28', '15', 1, 4, 3),
(21, 'Qui a réalisé \"Titanic\" (1997) ?', 'Steven Spielberg', 'James Cameron', 'Christopher Nolan', 'Quentin Tarantino', 2, 5, 1),
(22, 'Quel acteur joue Iron Man dans l\'univers cinématographique Marvel ?', 'Chris Evans', 'Robert Downey Jr.', 'Chris Hemsworth', 'Mark Ruffalo', 2, 5, 1),
(23, 'Quel film a remporté l\'Oscar du meilleur film en 2020 ?', 'Parasite', '1917', 'Joker', 'Once Upon a Time in Hollywood', 1, 5, 1),
(24, 'Quel est le premier long métrage d\'animation des studios Disney ?', 'Bambi', 'Pinocchio', 'Blanche-Neige et les Sept Nains', 'Fantasia', 3, 5, 2),
(25, 'Qui a réalisé \"Psychose\" (1960) ?', 'Alfred Hitchcock', 'Stanley Kubrick', 'Orson Welles', 'Francis Ford Coppola', 1, 5, 3),
(26, 'Quel est le manga le plus vendu au monde ?', 'Dragon Ball', 'One Piece', 'Naruto', 'Attack on Titan', 2, 6, 1),
(27, 'Qui est le créateur de \"Dragon Ball\" ?', 'Masashi Kishimoto', 'Akira Toriyama', 'Eiichiro Oda', 'Hajime Isayama', 2, 6, 1),
(28, 'Dans \"Naruto\", quel est le nom du village caché de Naruto ?', 'Konoha', 'Sunagakure', 'Kirigakure', 'Kumogakure', 1, 6, 1),
(29, 'Quel est le nom du héros de \"Death Note\" ?', 'L', 'Light Yagami', 'Near', 'Mello', 2, 6, 2),
(30, 'Dans \"One Piece\", quel est le fruit du démon de Luffy ?', 'Gomu Gomu no Mi', 'Mera Mera no Mi', 'Hito Hito no Mi', 'Gura Gura no Mi', 1, 6, 3),
(31, 'Quel groupe britannique est composé de John, Paul, George et Ringo ?', 'The Rolling Stones', 'The Beatles', 'Queen', 'Led Zeppelin', 2, 7, 1),
(32, 'Quel instrument de musique a 88 touches ?', 'Le violon', 'La guitare', 'Le piano', 'La harpe', 3, 7, 1),
(33, 'Quelle chanteuse est surnommée \"The Queen of Pop\" ?', 'Beyoncé', 'Madonna', 'Taylor Swift', 'Rihanna', 2, 7, 1),
(34, 'Quel groupe a sorti l\'album \"The Dark Side of the Moon\" ?', 'Led Zeppelin', 'Pink Floyd', 'The Doors', 'The Who', 2, 7, 2),
(35, 'Quel compositeur a écrit \"La Symphonie du Nouveau Monde\" ?', 'Ludwig van Beethoven', 'Wolfgang Amadeus Mozart', 'Antonín Dvořák', 'Johann Sebastian Bach', 3, 7, 3),
(36, 'Qui a écrit \"Les Misérables\" ?', 'Victor Hugo', 'Alexandre Dumas', 'Émile Zola', 'Gustave Flaubert', 1, 8, 1),
(37, 'Quel est le premier tome de la série Harry Potter ?', 'Harry Potter et la Coupe de Feu', 'Harry Potter et l\'Ordre du Phénix', 'Harry Potter à l\'école des sorciers', 'Harry Potter et la Chambre des Secrets', 3, 8, 1),
(38, 'Qui est l\'auteur de \"1984\" ?', 'George Orwell', 'Aldous Huxley', 'Ray Bradbury', 'J.R.R. Tolkien', 1, 8, 1),
(39, 'Dans quelle langue originale a été écrit \"Don Quichotte\" ?', 'Français', 'Italien', 'Espagnol', 'Portugais', 3, 8, 2),
(40, 'Qui a écrit \"À la recherche du temps perdu\" ?', 'Marcel Proust', 'Albert Camus', 'Jean-Paul Sartre', 'Franz Kafka', 1, 8, 3),
(41, 'Quelle entreprise a créé le système d\'exploitation Windows ?', 'Apple', 'Microsoft', 'Google', 'IBM', 2, 9, 1),
(42, 'Que signifie l\'acronyme \"HTML\" ?', 'Hyper Text Markup Language', 'High Tech Modern Language', 'Hyper Transfer Markup Language', 'Home Tool Markup Language', 1, 9, 1),
(43, 'Quel langage de programmation est principalement utilisé pour le développement Android ?', 'Swift', 'Kotlin', 'Python', 'C#', 2, 9, 1),
(44, 'Quelle est la dernière version de Java (décembre 2024) ?', 'Java 17', 'Java 19', 'Java 21', 'Java 23', 3, 9, 2),
(45, 'Quel algorithme de consensus est utilisé par Bitcoin ?', 'Proof of Stake', 'Proof of Work', 'Proof of Authority', 'Proof of History', 2, 9, 3),
(46, 'Quelle est la monnaie officielle du Japon ?', 'Le Won', 'Le Yuan', 'Le Yen', 'Le Dollar', 3, 2, 1),
(47, 'Quel est l\'élément chimique dont le symbole est \"O\" ?', 'Or', 'Osmium', 'Oxygène', 'Oganesson', 3, 3, 1),
(48, 'Quel pays a inventé le rugby ?', 'La France', 'L\'Angleterre', 'L\'Australie', 'La Nouvelle-Zélande', 2, 4, 2),
(49, 'Quel réalisateur a dirigé \"Inception\" et \"Interstellar\" ?', 'Christopher Nolan', 'Denis Villeneuve', 'James Cameron', 'Ridley Scott', 1, 5, 2),
(50, 'Quel manga a été créé par Hajime Isayama ?', 'One Piece', 'Naruto', 'Attack on Titan', 'My Hero Academia', 3, 6, 2),
(51, 'Quel album des Beatles est surnommé \"l\'Album Blanc\" ?', 'Abbey Road', 'Let It Be', 'The Beatles', 'Sgt. Pepper\'s Lonely Hearts Club Band', 3, 7, 2),
(52, 'Qui a écrit \"Le Petit Prince\" ?', 'Jules Verne', 'Antoine de Saint-Exupéry', 'Charles Perrault', 'Hans Christian Andersen', 2, 8, 2),
(53, 'Quelle entreprise a développé le système d\'exploitation Linux ?', 'Microsoft', 'Apple', 'IBM', 'Linus Torvalds (indépendant)', 4, 9, 2),
(54, 'Quelle bataille a marqué la fin de Napoléon Bonaparte ?', 'Waterloo', 'Austerlitz', 'Iéna', 'Wagram', 1, 1, 3),
(55, 'Quel est le plus grand lac d\'Amérique du Nord ?', 'Le lac Michigan', 'Le lac Supérieur', 'Le lac Huron', 'Le lac Ontario', 2, 2, 3),
(56, 'Quelle particule subatomique a une charge positive ?', 'Électron', 'Neutron', 'Proton', 'Neutrino', 3, 3, 3),
(57, 'Quel joueur de football a remporté le plus de Ballons d\'Or ?', 'Cristiano Ronaldo', 'Lionel Messi', 'Pelé', 'Maradona', 1, 4, 3),
(58, 'Quel film a été le premier à utiliser des images générées par ordinateur (CGI) ?', 'Star Wars (1977)', 'Tron (1982)', 'Toy Story (1995)', 'Jurassic Park (1993)', 2, 5, 3),
(59, 'Quel est le vrai nom de l\'auteur de manga \"Eiichiro Oda\" ?', 'Oda Eiichiro', 'Kishimoto Masashi', 'Toriyama Akira', 'Isayama Hajime', 1, 6, 3),
(61, 'Qui est ghiles', 'za3im', 'el baz idourare', 'chikour', 'le capotchino', 2, 1, 3);

-- --------------------------------------------------------

--
-- Structure de la table `quiz`
--

DROP TABLE IF EXISTS `quiz`;
CREATE TABLE IF NOT EXISTS `quiz` (
  `id_quiz` int NOT NULL AUTO_INCREMENT,
  `titre` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `date_creation` date NOT NULL,
  `utilisateur_id` int NOT NULL,
  `theme_id` int NOT NULL,
  PRIMARY KEY (`id_quiz`),
  KEY `utilisateur_id` (`utilisateur_id`),
  KEY `theme_id` (`theme_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `quiz`
--

INSERT INTO `quiz` (`id_quiz`, `titre`, `date_creation`, `utilisateur_id`, `theme_id`) VALUES
(1, 'Quiz Histoire', '2024-12-18', 3, 1),
(2, 'Quiz Géographie', '2024-12-18', 3, 2),
(3, 'Quiz Science', '2024-12-18', 3, 3),
(4, 'Quiz Sport', '2024-12-18', 3, 4),
(5, 'Quiz Cinéma', '2024-12-18', 3, 5),
(6, 'Quiz Manga', '2024-12-18', 3, 6),
(7, 'Quiz Musique', '2024-12-18', 3, 7),
(8, 'Quiz Littérature', '2024-12-18', 3, 8),
(9, 'Quiz Informatique', '2024-12-18', 3, 9);

-- --------------------------------------------------------

--
-- Structure de la table `resultat`
--

DROP TABLE IF EXISTS `resultat`;
CREATE TABLE IF NOT EXISTS `resultat` (
  `id_resultat` int NOT NULL AUTO_INCREMENT,
  `utilisateur_id` int NOT NULL,
  `quiz_id` int NOT NULL,
  `score` int NOT NULL,
  `date_completion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `temps_ecoule` int DEFAULT NULL,
  PRIMARY KEY (`id_resultat`),
  KEY `utilisateur_id` (`utilisateur_id`),
  KEY `quiz_id` (`quiz_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Structure de la table `role`
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE IF NOT EXISTS `role` (
  `id_role` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id_role`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `role`
--

INSERT INTO `role` (`id_role`, `nom`) VALUES
(1, 'Admin'),
(2, 'Joueur');

-- --------------------------------------------------------

--
-- Structure de la table `score`
--

DROP TABLE IF EXISTS `score`;
CREATE TABLE IF NOT EXISTS `score` (
  `id_score` int NOT NULL AUTO_INCREMENT,
  `valeur` int NOT NULL,
  `date_score` date NOT NULL,
  `utilisateur_id` int NOT NULL,
  `quiz_id` int NOT NULL,
  PRIMARY KEY (`id_score`),
  KEY `utilisateur_id` (`utilisateur_id`),
  KEY `quiz_id` (`quiz_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `score`
--

INSERT INTO `score` (`id_score`, `valeur`, `date_score`, `utilisateur_id`, `quiz_id`) VALUES
(2, 8, '2024-12-04', 3, 1);

-- --------------------------------------------------------

--
-- Structure de la table `theme`
--

DROP TABLE IF EXISTS `theme`;
CREATE TABLE IF NOT EXISTS `theme` (
  `id_theme` int NOT NULL AUTO_INCREMENT,
  `nom_theme` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id_theme`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `theme`
--

INSERT INTO `theme` (`id_theme`, `nom_theme`) VALUES
(1, 'Histoire'),
(2, 'Géographie'),
(3, 'Science'),
(4, 'Sport'),
(5, 'Cinéma'),
(6, 'Manga'),
(7, 'Musique'),
(8, 'Littérature'),
(9, 'Informatique');

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
CREATE TABLE IF NOT EXISTS `utilisateur` (
  `id_user` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `email` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `email` (`email`),
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id_user`, `nom`, `email`, `password`, `role_id`) VALUES
(3, 'Admin', 'admin@mail.com', '12345', 1),
(4, 'luca', 'lucas.dupont@example.com', '12345', 2),
(5, 'ghiles', 'ghiles@mail.com', '12345', 2);

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `question`
--
ALTER TABLE `question`
  ADD CONSTRAINT `question_ibfk_1` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`id_quiz`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `question_ibfk_2` FOREIGN KEY (`difficulte_id`) REFERENCES `difficulte` (`id_difficulte`) ON UPDATE CASCADE;

--
-- Contraintes pour la table `quiz`
--
ALTER TABLE `quiz`
  ADD CONSTRAINT `quiz_ibfk_1` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `quiz_ibfk_2` FOREIGN KEY (`theme_id`) REFERENCES `theme` (`id_theme`) ON UPDATE CASCADE;

--
-- Contraintes pour la table `score`
--
ALTER TABLE `score`
  ADD CONSTRAINT `score_ibfk_1` FOREIGN KEY (`utilisateur_id`) REFERENCES `utilisateur` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `score_ibfk_2` FOREIGN KEY (`quiz_id`) REFERENCES `quiz` (`id_quiz`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD CONSTRAINT `utilisateur_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id_role`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
