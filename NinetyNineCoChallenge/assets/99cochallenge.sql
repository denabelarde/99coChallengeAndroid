--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `favoritestores` (
  `_id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `place_id` text NOT NULL,
  `name` text NOT NULL,
  `lat` text NOT NULL,
  `lng` text NOT NULL,
  `address` text NOT NULL,
  `icon` text NOT NULL,
  `datecreated` text NOT NULL
);
