# Comlang_final-project

# 🧩 Class Structure Overview

This project implements a modular and object-oriented text-based adventure game. Below is a high-level summary of the class structure:

---

## 🧍 Hero
Represents the player character.  
**Fields:** position `(x, y)`, HP, equipped weapon, key possession  
**Methods:** `move()`, `attack()`, `pickUp()`, `takeDamage()`, etc.

---

## 👾 Monster
Represents an enemy in the room.  
**Fields:** name, position, HP, damage, key drop flag  
**Methods:** `takeDamage()`, `dealDamage()`, `dropsKey()`

---

## 🧱 Room
Represents a single room loaded from a CSV file.  
**Fields:** grid, monsters, items, doors, hero spawn position  
**Methods:**  
- `loadFromCSV()`  
- `saveToCSV()`  
- `checkInteractions()`  
- `display()`  

---

## 🚪 Door
Represents a door to another room.  
**Fields:** position, destination path, door type (`'d'` or `'D'`)  
**Methods:** `isMasterDoor()`

---

## 🧪 Item (interface)
Interface for usable items.  
**Methods:** `use(Hero hero)`, `getSymbol()`

---

## ⚔️ Weapon (implements Item)
Represents a weapon.  
**Fields:** name, damage, symbol  
**Methods:** `use(Hero)`, `getSymbol()`

---

## 🧴 Potion (implements Item)
Represents a healing potion.  
**Fields:** heal amount, symbol  
**Methods:** `use(Hero)`, `getSymbol()`

---

## 🎮 Game
Main controller for the game logic.  
**Handles:** game loop, transitions, player status, and file I/O

---

## 🚀 Main
Program entry point.  
**Starts** the game using `Game.start()`

---

💡 *Designed using modular OOP principles with clear class responsibility separation.*

