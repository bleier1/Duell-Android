# Duell-Android
A chess variant board game using dice designed in Java for Android devices.

## Basic Information
In Duell, you roll dice across the board with the objective of either capturing the enemy's key die (the die with 1 on every side) or capturing the key space (where the key die originally starts).
You can capture other dice as well to assist in an easier time capturing the dice.
You can only move the die by the number that is shown on the top of the die (in this implementation, the first number after H or C).

## Features
* The ability to save and resume games
  * Stored as .txt files, allowing the player to modify the game as they wish
  * Proper checking for saved game format
* An AI that plays to win making the best move possible at each turn, describing its methodology
  * AI also assists the player when they need a recommendation for a move to make
* Automatic checking for spaces a die is able to move to

## Screenshots
<img src="/screenshots/mainscreen.png?raw=true" height="400"> <img src="/screenshots/whogoesfirst.png?raw=true" height="400"> <img src="/screenshots/playergoesfirst.png?raw=true" height="400">
<img src="/screenshots/takingyourturn.png?raw=true" height="400"> <img src="/screenshots/saveask.png?raw=true" height="400"> <img src="/screenshots/computermoves.png?raw=true" height="400">
<img src="/screenshots/recommendation.png?raw=true" height="400"> <img src="/screenshots/saving.png?raw=true" height="400">
