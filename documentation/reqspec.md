# Requirement specification

## Purpose

The application serves to manage a chess tournament using the swiss system for pairing opponents each round.

## User Interface

In a graphical interface the user can open and read tournament data files, generate pairings and input match results.

## Functionality

#### File types

Data used in the tournament is stored in a text file written and read by the program. Three types of files are recognized by the program. The user can open these files observations, as well as generate the next type of file.

 - Initial file; contains the names and ratings of all players. A new match file can be created from the initial file.
 - Match file; contains pairings for the current round and data on previous rounds' results for the players, if any. A new results file can be created from a match file by inputting the matches' results.
 - Standings file; contains the standings of the current round and data on previous rounds' results for the players. A new match file can be created from a standings file.

#### Pairing rules

 - The amount of rounds in a tournament is defined beforehand
 - Winning a match scores a point, drawing scores each player a half-point
 - No pairing can repeat
 - No player can have three games in a row as the same color
 - No player, at any point, can have played three more games as one color than the other
 - Players with same score are paired with each other when possible
 - Players will have, at any point, played an equal amount of games with both colors when possible
 - Players will play with a different color than previous round when possible

## Ideas for later development

 - Defining different rulesets for pairing (e.g. removing color from consideration for use in other games)
