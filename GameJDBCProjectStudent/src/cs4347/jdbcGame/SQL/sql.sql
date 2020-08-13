-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema games
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gamescc_number
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `games` DEFAULT CHARACTER SET UTF8MB4 ;
USE `games` ;

-- -----------------------------------------------------
-- Table `games`.`player`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `games`.`player` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(20) NOT NULL,
  `last_name` VARCHAR(20) NOT NULL,
  `join_date` DATE NOT NULL,
  `email` VARCHAR(50) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `games`.`game`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `games`.`game` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(50) NOT NULL,
  `description` VARCHAR(200) NULL,
  `release_date` DATE NOT NULL,
  `version` VARCHAR(50) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `games`.`gamesowned`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `games`.`gamesowned` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `purchase_date` DATE NOT NULL,
  `purchase_price` FLOAT NOT NULL,
  `player_id` BIGINT NOT NULL,
  `game_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_gamesowned_player1_idx` (`player_id` ASC) VISIBLE,
  INDEX `fk_gamesowned_game1_idx` (`game_id` ASC) VISIBLE,
  CONSTRAINT `fk_gamesowned_Player1`
    FOREIGN KEY (`player_id`)
    REFERENCES `games`.`player` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_gamesowned_game1`
    FOREIGN KEY (`game_id`)
    REFERENCES `games`.`game` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `games`.`gamesplayed`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `games`.`gamesplayed` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `time_finished` DATE NOT NULL,
  `score` INT NOT NULL,
  `player_id` BIGINT NOT NULL,
  `game_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_gamesplayed_player1_idx` (`player_id` ASC) visible,
  INDEX `fk_gamesplayed_game1_idx` (`game_id` ASC) visible,
  CONSTRAINT `fk_gamesplayed_player1`
    FOREIGN KEY (`player_id`)
    REFERENCES `games`.`player` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_gamesplayed_game1`
    FOREIGN KEY (`game_id`)
    REFERENCES `games`.`game` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `games`.`creditrard`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `games`.`creditcard` (
  `player_id` BIGINT NOT NULL,
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `cc_name` VARCHAR(30) NOT NULL,
  `cc_number` CHAR(30) NOT NULL,
  `security_code` INT NOT NULL,
  `exp_date` CHAR(7) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_creditcard_player_idx` (`player_id` ASC) visible,
  CONSTRAINT `fk_creditcard_player`
    FOREIGN KEY (`player_id`)
    REFERENCES `games`.`player` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
