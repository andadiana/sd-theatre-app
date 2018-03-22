# Create user table

CREATE TABLE user
(
    user_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    username VARCHAR(20) NOT NULL,
    password CHAR(32) NOT NULL,
    user_type VARCHAR(20) DEFAULT "ADMIN"
);
CREATE UNIQUE INDEX user_username_uindex ON user (username);

# Triggers for user

CREATE TRIGGER ins_usertype BEFORE INSERT ON user
FOR EACH ROW
BEGIN
  IF NEW.user_type NOT IN ('ADMIN', 'CASHIER') THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'bad user type';
  END IF;
END;

CREATE TRIGGER update_usertype BEFORE UPDATE ON user
FOR EACH ROW
BEGIN
  IF NEW.user_type NOT IN ('ADMIN', 'CASHIER') THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'bad user type';
  END IF;
END;

# Populate user table

INSERT INTO user (username, password, user_type)
    VALUES ("admin", "21232F297A57A5A743894A0E4A801FC3", "ADMIN"),
      ("cashier1", "136989BAAC262EA3F560297AAB280C8D", "CASHIER");

# Create show table

CREATE TABLE `show`
(
    show_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(30) NOT NULL,
    cast VARCHAR(500) NOT NULL,
    date DATETIME NOT NULL,
    nr_tickets INT NOT NULL
);

# Triggers for show

CREATE TRIGGER ins_showgenre
  BEFORE INSERT
  ON `show`
  FOR EACH ROW
  BEGIN
    IF NEW.genre NOT IN ('BALLET', 'OPERA') THEN
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'bad show genre';
    END IF;
  END;

CREATE TRIGGER update_showgenre
  BEFORE UPDATE
  ON `show`
  FOR EACH ROW
  BEGIN
    IF NEW.genre NOT IN ('BALLET', 'OPERA') THEN
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'bad show genre';
    END IF;
  END;

# Populate

INSERT INTO `show` (title, genre, cast, date, nr_tickets)
    VALUES
      ("Aida", "OPERA", "CARMEN GURBAN - Aida,SORIN LUPU - Radames,ANDRADA IOANA ROSU - Amneris",
       '2018-04-01 20:30:00', 100),
      ("Barbierul din Sevilla", "OPERA", "Figaro, barbier din Sevilla - Geani Brad,Rosina, pupila lui Bartolo: Adriana Festeu",
       '2018-04-05 20:30:00', 100),
      ("Giselle", "BALLET", "Giselle: Andreea Jura, Printul Albert: Dan Haja, Myrtha: Adelina Filipas, Hans: Lucian Bacoiu",
       '2018-04-06 20:30:00', 100);

# Create seat table

CREATE TABLE seat
(
    seatid INT PRIMARY KEY AUTO_INCREMENT,
    row_nr INT NOT NULL,
    seat_nr INT NOT NULL
);

# Create ticket table



