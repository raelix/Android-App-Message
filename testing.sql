-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 31, 2013 at 10:25 AM
-- Server version: 5.5.24-log
-- PHP Version: 5.3.13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `testing`
--

-- --------------------------------------------------------

--
-- Table structure for table `devices`
--

CREATE TABLE IF NOT EXISTS `devices` (
  `name` text COLLATE utf8_unicode_ci NOT NULL,
  `idkey` text COLLATE utf8_unicode_ci NOT NULL,
  `imei` text COLLATE utf8_unicode_ci NOT NULL,
  `tel` text COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `devices`
--

INSERT INTO `devices` (`name`, `idkey`, `imei`, `tel`) VALUES
('caaasdasssa', 'd', 'asdsdasd', 'asdsda'),
('qcaaasdasssa', ' ', 'asdsdasd', 'asdsda'),
('qcaaasdasssa', 'sdsss', 'asdsdasd', 'asdsda'),
('qcaaasdasssa', 'sdsddddddds', 'asdsdasd', 'asdsda'),
('qcaaasdasssa', 'sdsdddddcds', 'asdsdassdasdsad', 'asdsdsdasdsda'),
('gianmarco', 'lachiavedidefault', 'ASNisjiajiss1j21212', '32008800475'),
('Gm', 'APA91bERc0R_oWu8lq-JDJ-7WwmSpAdXNLnT3wBZe3sfvcNzkQfOWvh75eD34b0zhDXyhRMtilaJBV8ORUsVuHLi7-0YrZ4V7YemlakXHQe3GK2Lja90IuNX2I0fmTfb1Z__1gktfrx0R9CqAUM2sVHO8qHP7AMkiA', '351869053793439', '393208305976');

-- --------------------------------------------------------

--
-- Table structure for table `dispositivi`
--

CREATE TABLE IF NOT EXISTS `dispositivi` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `chiave` text NOT NULL,
  `idtelefono` text NOT NULL,
  `numtelefono` text NOT NULL,
  `user` text NOT NULL,
  `password` text NOT NULL,
  `campo` int(11) NOT NULL,
  `campo1` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Dumping data for table `dispositivi`
--

INSERT INTO `dispositivi` (`id`, `chiave`, `idtelefono`, `numtelefono`, `user`, `password`, `campo`, `campo1`) VALUES
(1, '', '', '', 'gianm', '', 0, 0),
(4, '', 'Griffin', '', 'Peter', 'cazzo', 0, 0),
(6, '', 'adhsauodhasdkibsnadiksadisa', '', 'raelix', 'mypass', 0, 0),
(7, '', 'adhsauodhasdkibsnadiksadisa', '', 'raelix', 'mypass', 0, 0),
(8, '', 'idphonenumber', '', 'raelix', 'mypass', 0, 0);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
