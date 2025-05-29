# Comlang_final-project

🧩 Class Structure Overview
This project implements a modular and object-oriented text-based adventure game. Below is a high-level summary of the class structure:

⚔️ Hero

Represents the player character.
Fields: position (x, y), HP, equipped weapon, key possession.
Methods: move(), attack(), pickUp(), takeDamage(), etc.
👾 Monster

Represents an enemy in the room.
Fields: name, position, HP, damage, key drop flag.
Methods: takeDamage(), dealDamage(), dropsKey().
🧱 Room

Represents a single room loaded from a CSV.
Fields: grid, monsters, items, doors, hero spawn position.
Methods: loadFromCSV(), saveToCSV(), checkInteractions(), display().
🚪 Door

Represents a transition point between rooms.
Fields: position, destination room path, door type ('d' or 'D').
Methods: isMasterDoor().
🧪 Item (interface)

Interface for items usable by Hero.
Methods: use(), getSymbol().
⚔️ Weapon implements Item

Represents a weapon with a name, damage value, and symbol.
Methods: use(Hero), getSymbol().
🧴 Potion implements Item

Represents a healing potion.
Methods: use(Hero), getSymbol().
🎮 Game

Main game controller class.
Handles game loop, room transitions, file loading/saving.
🚀 Main

Entry point of the program.
Starts the game.
