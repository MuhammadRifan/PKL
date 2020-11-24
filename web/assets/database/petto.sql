-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Nov 24, 2020 at 01:41 PM
-- Server version: 5.7.24
-- PHP Version: 7.2.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `petto`
--

-- --------------------------------------------------------

--
-- Table structure for table `artikel`
--

CREATE TABLE `artikel` (
  `id_artikel` int(11) NOT NULL,
  `penulis` varchar(50) NOT NULL,
  `id_pengguna` int(11) NOT NULL,
  `judul` varchar(50) NOT NULL,
  `deskripsi` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `artikel`
--

INSERT INTO `artikel` (`id_artikel`, `penulis`, `id_pengguna`, `judul`, `deskripsi`) VALUES
(1, 'Soto', 2, 'Cara ternak lele', 'Cara ternak yang efisien dan terbukti ampuh'),
(2, 'Soto', 2, 'Cara mengkawinkan burung perkutut', 'Cara mengawinkan burung dengan cepat');

-- --------------------------------------------------------

--
-- Table structure for table `fasilitas`
--

CREATE TABLE `fasilitas` (
  `id_fasilitas` int(11) NOT NULL,
  `nama_fasilitas` varchar(50) NOT NULL,
  `jenis_fasilitas` varchar(50) NOT NULL,
  `alamat_fasilitas` text NOT NULL,
  `deskripsi_fasilitas` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `fasilitas`
--

INSERT INTO `fasilitas` (`id_fasilitas`, `nama_fasilitas`, `jenis_fasilitas`, `alamat_fasilitas`, `deskripsi_fasilitas`) VALUES
(1, 'Vet Dr Balak', 'Health', 'Jl tumpak mangewu selatan', 'Melayani dengan cepat'),
(2, 'Toko mujahidin', 'Shop', 'Jl jair-mujair no. 95', 'Jual Pakan Burung Lengkap'),
(3, 'Vet Dr Sonario', 'Health', 'Jl tino agung', 'Buka 24 jam'),
(4, 'Toko Cak Soleh', 'Shop', 'Jl jair-mujair no. 96', 'Jual Peralatan Kucing Anjing'),
(5, 'Happy Pet', 'Animal Care', 'Jl Jenggolo no 22', 'Menyediakan segala kebutuhan kucing dan anjing'),
(6, 'My Precious Pet', 'Animal Care', 'Perum Pondok Agung Blok B6 - 11', 'Menyediakan kebutuhan untuk peliharaan kecil seperti marmut, siput, kancut');

-- --------------------------------------------------------

--
-- Table structure for table `komunitas`
--

CREATE TABLE `komunitas` (
  `id_komunitas` int(11) NOT NULL,
  `nama_komunitas` varchar(50) NOT NULL,
  `foto_komunitas` varchar(50) NOT NULL,
  `kontak` varchar(50) NOT NULL,
  `deskripsi_komunitas` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `komunitas`
--

INSERT INTO `komunitas` (`id_komunitas`, `nama_komunitas`, `foto_komunitas`, `kontak`, `deskripsi_komunitas`) VALUES
(1, 'Kucing > Anjing', 'foto1.jpg', '089394', 'SUMPAH, KUCING TERBAIK, ANJING TERBURUK'),
(2, 'Anjing is the best', 'foto2.jpg', '0898345', 'ANJING IS THE BEST, VALID NO DEBAT !');

-- --------------------------------------------------------

--
-- Table structure for table `pengguna`
--

CREATE TABLE `pengguna` (
  `id_pengguna` int(11) NOT NULL,
  `nama_pengguna` varchar(50) NOT NULL,
  `email_pengguna` varchar(50) NOT NULL,
  `password_pengguna` varchar(50) NOT NULL,
  `alamat` text NOT NULL,
  `no_telp` varchar(13) NOT NULL,
  `foto` varchar(50) NOT NULL,
  `nip` varchar(25) DEFAULT NULL,
  `spesialis` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `pengguna`
--

INSERT INTO `pengguna` (`id_pengguna`, `nama_pengguna`, `email_pengguna`, `password_pengguna`, `alamat`, `no_telp`, `foto`, `nip`, `spesialis`) VALUES
(1, 'sate', 'sate@gmail.com', '12345', 'sate solo', '089', 'foto1.jpg', NULL, NULL),
(2, 'soto', 'soto@gmail.com', '12345', 'soto lamongan', '08654', 'foto2.png', '34352', 'Dokter Kucing'),
(3, 'Rifan', 'rifan@gmail.com', '121212', 'perumahan', '08775', '5fbb5cde88dee7.18628323.jpg', '696969', 'Sloth'),
(4, 'otong', 'otong@gmail.com', '12345', 'sda', '0877555655', '1.jpg', NULL, NULL),
(25, 'rifan1231', 'rifan1@gmail.com', '121212', 'perumahan', '0877', 'foto3.jpg', NULL, NULL),
(26, 'nyxatom', 'nyxatom@gmail.com', '12e45678', 'yuo', '087755565590', '5fbd095c3868b5.62958164.jpg', NULL, NULL),
(29, 'muhammadrifan', 'muhammadrifan153@gmail.com', '12345678', 'pucang', '887755565590', '5fbd0c71d232c4.48308788.jpg', NULL, NULL),
(30, 'donlot707', 'donlot707@gmail.com', '12345678', 'pucangg', '987755565590', '5fbd0cefc78a59.54374145.jpg', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `test`
--

CREATE TABLE `test` (
  `id` int(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `pass` varchar(25) NOT NULL,
  `nip` varchar(25) DEFAULT NULL,
  `is_dokter` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `test`
--

INSERT INTO `test` (`id`, `email`, `pass`, `nip`, `is_dokter`) VALUES
(1, 'roger@gmail.com', '1234512345123451234512', NULL, '0'),
(2, 'rifan@gmail.com', '12345', '975632', '1'),
(3, 'dadada@gmail.com', '1234512345123451234512345', NULL, '0'),
(4, 'rion@gmail.com', '12345', '55555', '1'),
(5, 'user@gmail.com', '12345', NULL, '0'),
(6, 'dokter@gmail.com', '12345', '89898', '1'),
(7, 'pengguna@gmail.com', '12345', NULL, '0');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `artikel`
--
ALTER TABLE `artikel`
  ADD PRIMARY KEY (`id_artikel`),
  ADD KEY `fk_penulis_pengguna` (`id_pengguna`);

--
-- Indexes for table `fasilitas`
--
ALTER TABLE `fasilitas`
  ADD PRIMARY KEY (`id_fasilitas`);

--
-- Indexes for table `komunitas`
--
ALTER TABLE `komunitas`
  ADD PRIMARY KEY (`id_komunitas`);

--
-- Indexes for table `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`id_pengguna`),
  ADD UNIQUE KEY `email_pengguna` (`email_pengguna`),
  ADD UNIQUE KEY `no_telp` (`no_telp`);

--
-- Indexes for table `test`
--
ALTER TABLE `test`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `artikel`
--
ALTER TABLE `artikel`
  MODIFY `id_artikel` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `fasilitas`
--
ALTER TABLE `fasilitas`
  MODIFY `id_fasilitas` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `komunitas`
--
ALTER TABLE `komunitas`
  MODIFY `id_komunitas` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `pengguna`
--
ALTER TABLE `pengguna`
  MODIFY `id_pengguna` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `test`
--
ALTER TABLE `test`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `artikel`
--
ALTER TABLE `artikel`
  ADD CONSTRAINT `fk_penulis_pengguna` FOREIGN KEY (`id_pengguna`) REFERENCES `pengguna` (`id_pengguna`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
