-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jan 02, 2021 at 09:49 AM
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
-- Table structure for table `animalcare`
--

CREATE TABLE `animalcare` (
  `id` int(11) NOT NULL,
  `owner` varchar(50) DEFAULT NULL,
  `nama` varchar(64) NOT NULL,
  `address` varchar(256) NOT NULL,
  `city` varchar(256) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `picture` varchar(256) DEFAULT NULL,
  `hari_buka1` int(1) DEFAULT NULL,
  `hari_buka2` int(1) DEFAULT NULL,
  `hari_buka3` int(1) DEFAULT NULL,
  `hari_buka4` int(1) DEFAULT NULL,
  `hari_buka5` int(1) DEFAULT NULL,
  `hari_buka6` int(1) DEFAULT NULL,
  `hari_buka7` int(1) DEFAULT NULL,
  `jam_buka` varchar(8) NOT NULL,
  `jam_tutup` varchar(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `animalcare`
--

INSERT INTO `animalcare` (`id`, `owner`, `nama`, `address`, `city`, `phone`, `picture`, `hari_buka1`, `hari_buka2`, `hari_buka3`, `hari_buka4`, `hari_buka5`, `hari_buka6`, `hari_buka7`, `jam_buka`, `jam_tutup`) VALUES
(16, 'dwikiaditama', 'Royal', 'Jl. Kutisari Sel. I No.20, Kutisari, Kec. Tenggilis Mejoyo, Kota SBY, Jawa Timur 60291', 'Surabaya', '081332825565', 'img_animalcare/Royal.jpg', 1, 2, 3, 4, 5, 6, 0, '08:00:00', '16:00:00'),
(17, 'dwikiaditama', 'Duby', 'Jl. Rungkut Menanggal Harapan No.3, Rungkut Menanggal, Kec. Gn. Anyar, Kota SBY, Jawa Timur 60293', 'Surabaya', '087750408877', 'img_animalcare/duby.jpg', 1, 2, 3, 4, 5, 6, 7, '09:00:00', '20:00:00'),
(18, 'dwikiaditama', 'Cloe', 'Keputih, Sukolilo, Surabaya City, East Java 60111', 'Surabaya', '08178883188', 'img_animalcare/cloe.jpg', 1, 2, 3, 4, 5, 6, 0, '09:00:00', '21:00:00'),
(19, 'dwikiaditama', 'Reiby', 'Jl. Brawijaya No.58, Sawunggaling, Kec. Wonokromo, Kota SBY, Jawa Timur 60242', 'Surabaya', '085707071253', 'img_animalcare/Reiby.jpg', 1, 2, 3, 4, 5, 6, 7, '8', '21:00:00'),
(20, 'dwikiaditama', 'Aslan', 'Jl. Dr. Ir. H. Soekarno No.2j, Penjaringan Sari, Kec. Rungkut, Kota SBY, Jawa Timur 60293', 'Surabaya', '085648001800', 'img_animalcare/Aslan.jpg', 1, 2, 3, 4, 5, 6, 7, '07:00:00', '21:00:00'),
(21, 'Ripan', 'ACR', 'ACCC675757', 'ACE', '436345436787', 'img_animalcare/5fef0bab9fe960.98863995.jpg', 1, 2, 0, 0, 0, 0, 0, '06:45', '18:45');

-- --------------------------------------------------------

--
-- Table structure for table `artikel`
--

CREATE TABLE `artikel` (
  `id` int(11) NOT NULL,
  `penulis` varchar(50) NOT NULL,
  `judul` varchar(64) NOT NULL,
  `isi` varchar(2048) NOT NULL,
  `tanggal` varchar(50) NOT NULL,
  `picture` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `artikel`
--

INSERT INTO `artikel` (`id`, `penulis`, `judul`, `isi`, `tanggal`, `picture`) VALUES
(1, 'Ripan', 'Penghijauan', 'Penghijauan dibumi ini sekarang diperlukan karena akan membantu meringankan beban keluarga\r\n\r\nSiapa beban keluarga ? Ya kalian semua anjim', '30-12-2020', 'img_artikel/5fec61d4ca2c75.81808203.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `faskes`
--

CREATE TABLE `faskes` (
  `sip` varchar(32) NOT NULL,
  `owner` varchar(50) DEFAULT NULL,
  `nama` varchar(64) NOT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `address` varchar(256) NOT NULL,
  `city` varchar(100) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `picture` varchar(256) DEFAULT NULL,
  `hari_buka1` int(1) DEFAULT NULL,
  `hari_buka2` int(1) DEFAULT NULL,
  `hari_buka3` int(1) DEFAULT NULL,
  `hari_buka4` int(1) DEFAULT NULL,
  `hari_buka5` int(1) DEFAULT NULL,
  `hari_buka6` int(1) DEFAULT NULL,
  `hari_buka7` int(1) DEFAULT NULL,
  `jam_buka` varchar(8) NOT NULL,
  `jam_tutup` varchar(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `faskes`
--

INSERT INTO `faskes` (`sip`, `owner`, `nama`, `description`, `address`, `city`, `phone`, `picture`, `hari_buka1`, `hari_buka2`, `hari_buka3`, `hari_buka4`, `hari_buka5`, `hari_buka6`, `hari_buka7`, `jam_buka`, `jam_tutup`) VALUES
('324234234578', 'Ripan', 'Hell to', 'Gausah Kesini Mahal Bro', 'Gading Martin', 'Sidoarjo', '678658956870', 'img_faskes/5fef122f1841d3.96186808.jpg', 1, 0, 0, 0, 5, 0, 7, '06:30', '23:00');

-- --------------------------------------------------------

--
-- Table structure for table `komunitas`
--

CREATE TABLE `komunitas` (
  `id` int(11) NOT NULL,
  `owner` varchar(50) DEFAULT NULL,
  `nama` varchar(128) NOT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `address` varchar(256) DEFAULT NULL,
  `kota` varchar(64) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `picture` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `komunitas`
--

INSERT INTO `komunitas` (`id`, `owner`, `nama`, `description`, `address`, `kota`, `phone`, `picture`) VALUES
(1, 'Ripan', 'Komuka', 'Disini dilarang ngasi nomor', 'Sidoarjooooooo', 'Sidoarjoooo', '760783585675', 'img_komunitas/5fef357e89dac4.88230467.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `paket`
--

CREATE TABLE `paket` (
  `id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `picture` varchar(256) DEFAULT NULL,
  `harga` float(32,2) NOT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `animalcare` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `peliharaan`
--

CREATE TABLE `peliharaan` (
  `id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `spesies` varchar(32) NOT NULL,
  `ras` varchar(32) NOT NULL,
  `picture` varchar(256) DEFAULT NULL,
  `gender` varchar(6) NOT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `address` varchar(256) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `location` varchar(256) NOT NULL,
  `category` varchar(5) NOT NULL,
  `owner` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `pengguna`
--

CREATE TABLE `pengguna` (
  `username` varchar(50) NOT NULL,
  `pass` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `srtv` varchar(30) DEFAULT NULL,
  `level` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `pengguna`
--

INSERT INTO `pengguna` (`username`, `pass`, `email`, `phone`, `picture`, `srtv`, `level`) VALUES
('Bambang Gentolet', '$2y$10$oVB1yjgWm5ox6l.u4IOmNOq3ekXxdbJxocL1gYl8AxQ2fnM99SsZa', 'Bambangg@gmail.com', '089675463457', 'img_user/5ff03e7b3f6439.45960486.jpg', 'null', 1),
('dwikiaditama', '$2y$10$yjogl6TsZdfTVd4pX8At7OJn2sIK6KwZCYCB4Ssy.IjvvpLTfMdjG', 'dwikiaditamasupangkat@gmail.com', '085004555646', 'img_user/dwiki.jpg', '2.05.005865.03.2016.005791', 0),
('fahmina', '$2y$10$SHqkEv6LipgnuilxKFfQKeJbID5idszm5kCupkA31DskhTzXLSJBK', 'fahminugroho23@gmail.com', '082142632504', 'img_user/fahmi.jpg', '2.05.005865.03.2016.005792', 1),
('jefriismail', '$2y$10$ca8JfEz1a6T/r5dtykuN.uBIiFWfXSx/iwgSeLIuKyg7zrU4QzKQW', 'jefriismail99@gmail.com', '087855630276', 'img_user/jefri.jpg', '2.05.005865.03.2016.005789', 0),
('mrifandz', '$2y$10$SGt73Yxgbkml0PcOpKArk.yEi4qNnt47cEMgflUQStEpHOfEd3a5.', 'muhammadrifan153@gmail.com', '087755565590', 'img_user/rifan.jpg', '2.05.005865.03.2016.005790', 1),
('Ripan', '$2y$10$cVXmJaEXIgHry371P/R1q.OJwZbfkaFsUYZVtPPsP6dEvRYPJZWqm', 'Rifan@gmail.com', '087765748390', 'img_user/5fed6bcf57fd41.67012351.jpg', NULL, 1);

-- --------------------------------------------------------

--
-- Table structure for table `produk`
--

CREATE TABLE `produk` (
  `id` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `picture` varchar(256) DEFAULT NULL,
  `harga` float(32,2) NOT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `toko` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `toko`
--

CREATE TABLE `toko` (
  `id` int(11) NOT NULL,
  `owner` varchar(50) NOT NULL,
  `nama` varchar(64) NOT NULL,
  `address` varchar(256) NOT NULL,
  `city` varchar(100) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `picture` varchar(256) DEFAULT NULL,
  `hari_buka1` int(1) DEFAULT NULL,
  `hari_buka2` int(1) DEFAULT NULL,
  `hari_buka3` int(1) DEFAULT NULL,
  `hari_buka4` int(1) DEFAULT NULL,
  `hari_buka5` int(1) DEFAULT NULL,
  `hari_buka6` int(1) DEFAULT NULL,
  `hari_buka7` int(1) DEFAULT NULL,
  `jam_buka` varchar(8) NOT NULL,
  `jam_tutup` varchar(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `toko`
--

INSERT INTO `toko` (`id`, `owner`, `nama`, `address`, `city`, `phone`, `picture`, `hari_buka1`, `hari_buka2`, `hari_buka3`, `hari_buka4`, `hari_buka5`, `hari_buka6`, `hari_buka7`, `jam_buka`, `jam_tutup`) VALUES
(1, 'jefriismail', 'Mitra Fauna Pet Shop', 'Jl. Raya Buncitan No.305, Dusun Kp. Baro, Buncitan, Kec. Sedati, Kabupaten Sidoarjo, Jawa Timur 61253', 'Sidoarjo', '0895320646517', 'img_toko/Mitra%20Fauna.jpg', 1, 2, 3, 4, 0, 6, 7, '07:00:00', '21:00:00'),
(2, 'jefriismail', 'Yuri Pet Shop', 'Jl. Raya Sedati Gede No.6, Bono, Sedati Gede, Kec. Sedati, Kabupaten Sidoarjo, Jawa Timur 61253', 'Surabaya', '081332541456', 'img_toko/Yuri.jpg', 1, 2, 3, 4, 5, 6, 0, '09:00:00', '21:00:00'),
(3, 'jefriismail', 'Dock 9 Pet Shop', 'No 214 Kecamatan Gedangan Sidoarjo, Jl. Ahmad Yani, Megersari, Gedangan, Kec. Sidoarjo, Kabupaten Sidoarjo, Jawa Timur 61254', 'Sidoarjo', '087856506005', 'img_toko/Dock9.jpg', 1, 2, 3, 4, 5, 0, 0, '09:00:00', '21:00:00'),
(4, 'jefriismail', 'Mr. Gogon Pet Shop', 'Jalan Pandean, Pandean, Banjarkemantren, Kec. Buduran, Kabupaten Sidoarjo, Jawa Timur 61252', 'Surabaya', '085232606467', 'img_toko/gogon.jpg', 1, 2, 3, 4, 5, 6, 7, '08:00:00', '22:00:00'),
(5, 'jefriismail', 'Pussy Pet Shop', 'Jalan Anggrek 1C No. 7, Sritanjung, Wage, Kec. Taman, Kabupaten Sidoarjo, Jawa Timur 61257', 'Sidoarjo', '085733603975', 'img_toko/Pussy.jpg', 1, 2, 3, 4, 5, 0, 7, '07:00:00', '21:00:00'),
(11, 'Ripan', 'Rembulan', 'Jenggolo 54', 'Sidoarjo', '098797543451', 'img_toko/5feed9ecd4dd63.85712663.jpg', 1, 2, 3, 4, 5, 6, 7, '08:00', '19:30'),
(12, 'Ripan', 'Rembulan2', 'jenggolo', 'Kediri', '089', 'img_toko/5feed56d205769.19451808.jpg', 1, 0, 3, 4, 5, 6, 0, '07:00', '19:00'),
(13, 'Ripan', '546tyrtyry', 'rtyrtyr', 'Jombang', '456745645675', 'img_toko/5feeda51d98928.49060514.jpg', 1, 0, 3, 0, 5, 6, 0, '15:15', '15:15'),
(15, 'Ripan', 'qweqwe', 'qweqwewqe', 'Sidoarjo', '234141423452', 'img_toko/5feedb4cca53f0.41189399.jpg', 1, 2, 0, 0, 0, 6, 0, '15:18', '15:18');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `animalcare`
--
ALTER TABLE `animalcare`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nama` (`nama`),
  ADD KEY `owner` (`owner`);

--
-- Indexes for table `artikel`
--
ALTER TABLE `artikel`
  ADD PRIMARY KEY (`id`),
  ADD KEY `penulis` (`penulis`);

--
-- Indexes for table `faskes`
--
ALTER TABLE `faskes`
  ADD PRIMARY KEY (`sip`),
  ADD UNIQUE KEY `nama` (`nama`),
  ADD KEY `owner` (`owner`);

--
-- Indexes for table `komunitas`
--
ALTER TABLE `komunitas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nama` (`nama`),
  ADD KEY `owner` (`owner`);

--
-- Indexes for table `paket`
--
ALTER TABLE `paket`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD KEY `animalcare` (`animalcare`);

--
-- Indexes for table `peliharaan`
--
ALTER TABLE `peliharaan`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD KEY `owner` (`owner`);

--
-- Indexes for table `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`username`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `phone` (`phone`),
  ADD UNIQUE KEY `srtv` (`srtv`);

--
-- Indexes for table `produk`
--
ALTER TABLE `produk`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD KEY `toko` (`toko`);

--
-- Indexes for table `toko`
--
ALTER TABLE `toko`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nama` (`nama`),
  ADD KEY `owner` (`owner`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `animalcare`
--
ALTER TABLE `animalcare`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `artikel`
--
ALTER TABLE `artikel`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `komunitas`
--
ALTER TABLE `komunitas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `paket`
--
ALTER TABLE `paket`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `peliharaan`
--
ALTER TABLE `peliharaan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `produk`
--
ALTER TABLE `produk`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `toko`
--
ALTER TABLE `toko`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `animalcare`
--
ALTER TABLE `animalcare`
  ADD CONSTRAINT `animalcare_ibfk_1` FOREIGN KEY (`owner`) REFERENCES `pengguna` (`username`);

--
-- Constraints for table `artikel`
--
ALTER TABLE `artikel`
  ADD CONSTRAINT `artikel_ibfk_1` FOREIGN KEY (`penulis`) REFERENCES `pengguna` (`username`);

--
-- Constraints for table `faskes`
--
ALTER TABLE `faskes`
  ADD CONSTRAINT `faskes_ibfk_1` FOREIGN KEY (`owner`) REFERENCES `pengguna` (`username`);

--
-- Constraints for table `komunitas`
--
ALTER TABLE `komunitas`
  ADD CONSTRAINT `komunitas_ibfk_1` FOREIGN KEY (`owner`) REFERENCES `pengguna` (`username`);

--
-- Constraints for table `paket`
--
ALTER TABLE `paket`
  ADD CONSTRAINT `paket_ibfk_1` FOREIGN KEY (`animalcare`) REFERENCES `animalcare` (`id`);

--
-- Constraints for table `peliharaan`
--
ALTER TABLE `peliharaan`
  ADD CONSTRAINT `peliharaan_ibfk_1` FOREIGN KEY (`owner`) REFERENCES `pengguna` (`username`);

--
-- Constraints for table `produk`
--
ALTER TABLE `produk`
  ADD CONSTRAINT `produk_ibfk_1` FOREIGN KEY (`toko`) REFERENCES `toko` (`id`);

--
-- Constraints for table `toko`
--
ALTER TABLE `toko`
  ADD CONSTRAINT `toko_ibfk_1` FOREIGN KEY (`owner`) REFERENCES `pengguna` (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
