[![JavaJDK](https://img.shields.io/badge/Java%20JDK-17-orange?logo=java&style=flat-square)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.18.1-success?logo=minecraft&style=flat-square)](https://minecraft.net)
[![Spigot](https://img.shields.io/badge/Spigot-%20-orange?&style=flat-square)](http://spigotmc.org)
[![Paper](https://img.shields.io/badge/Paper-%20-orange?&style=flat-square)](https://papermc.io)
[![Discord](https://img.shields.io/discord/690934524955197471?label=Discord&logo=discord&style=flat-square)](https://discord.gg/C2HrEeCQ)

# parcivadControl
ParcivadControl is a mc plugin that brings some popular and helpful commands in your survival Server. It also includes a prefix Tablist, clean Chat and Admin-Commands.
One of the biggest differences to other plugins is that you have to choice to decide what feature is active or not. With `/commands` you can turn specified features of and customize the plugin for your need. 

## Table of Contents

- [All Commands](#all-commands)
  - [Commands](#commands)
  - [Enderchest](#enderchest)
  - [Tpa](#teleportation-request)
  - [Home](#home)
  - [Spawn](#spawn)
  - [Stats](#stats)
  - [Seed](#seed)
  - [Lock](#lock)
  - [Inventory](#inventory)
  - [Zone](#zone)
  - [Chat](#chat)
  - [Tempban](#tempban)
  - [Tempunban](#tempunban)
- [MiniBlocks](#miniblocks)
- [Tablist](#tablist)
- [Comment](#comment) 
  

## All Commands
### Commands
All commands can be turned on or off with the `/commands` Command.

__How to use the command__\
When you first installed the plugin and use a command the plugin will give you a deny for using it. This can be changed by turning the state of the command. With `/commands {command} on` you can activate the command.
<p align="left">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/commands-turn-on.png?raw=true">
</p>

Then the command should work. If you want you can easily turn it at any time off.

```diff
- permission: manage.commands | USE
```

### Enderchest
With `/ec`, short form for enderchest, you can open your own enderchest immediately in the game. It's like a "backpack" function. The only requirement needed is the achievement of the enderchest opening. Roles with the permission `enderchest.open` are able to open the enderchest of other players too.

__How to use the command__\
This command is not complex. Just type `/ec` and an inventory of your enderchest will popup. To open an enderchest of another player you can type his name as the second argument of the command.

<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/enderchest-other-player.png?raw=true">
</p>

```diff
- permission: enderchest.open | OPEN other enderchests
```

### Teleportation Request
With the popular `/tpa` command you can ask other online players to teleport to them. They can accept or deny your request. In the first case you would be teleported to the player, that you requested. Important to know is that there is no cooldown for the teleportation.

__How to use the command__\
To begin a request you can easily type `/tpa {player}`. Then the targeted player will get a message in his chat.
<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/tpa-request.png?raw=true">
</p>

The player can decide to accept or deny by typing the command. Alternative he can _hover_ over `accept` or `deny` and click to take action.
<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/tpa-request-hover.png?raw=true">
</p>

### Home
Also known command `/home` allows you to set your home and teleport each five minutes to the saved location. Players with the `home.noWait` permission don't have to wait.

__How to use the command__\
First you must set your home, because that is needed for the teleportation.
<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/home-set.png?raw=true">
</p>

After that you can teleport there every five minutes.
<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/home-teleport.png?raw=true">
</p>

With `/home remove` you can delete the saved home location.

```diff
- permission: home.noWait | for no cooldown
```

### Spawn
The `/spawn` command is so simple that there is no need for an explanation. It just teleports you to the __world__ spawn.

### Stats
`/stats` will show information of the world data. Also, statistics of other player can be requested with `/stats {player}`.

<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/stats.png?raw=true">
</p>

### Seed
`/seed` overwrites the normal seed function to hide it for other players. Only Operatores can see the Seed.

```diff
- permission: Server op | USE
```

### Lock
`/lock` is a command that should support the manager of the server. When active `/lock` will lock the server for everyone expect users with the permission `server.settings`.\
There are also two different motds you can setup in `ServerConfig.yml`. One for the unlocked state and the other one for the locked.

__How to use the command__\
You can unlock the server by typing `/lock off`. To set a personal kick/lock message type `/lock message {message...`. When the server gets locked from the console, the message will be set to "Server locked from console".
<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/lock-command.png?raw=true">
</p>

When the server gets locked with `/lock on` all players get kicked and the server changes the motd.
<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/lock-motd.png?raw=true">
</p>

```diff
- permission: manage.lock | to manage the lock command
- permission: server.settings | to join when server locked
```

### Inventory
The `/inv`, short form for inventory, command is a simple Admin-Command to open up other inventorys with full access.

__How to use the command__\
easily type ``/inv {player}`` and the players inventory will open up.
<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/inv.png?raw=true">
</p>
The top row will be the players hotbar!

```diff
- permission: inventory.open | to use this command
```

### Position
Helpful should be the `/pos` command. It allows every player to save locations by name and then reopen them to show their coordinates.

__How to use the command__\
Positions can be saved by typing `/pos save {name}`. In the feedback message you can check if the coordinates are right.
<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/pos-save.png?raw=true">
</p>

When you want a list of your locations you can do this by typing `/pos list`, then you can call them by just typing the name after pos like `/pos {name}`.
<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/pos-call.png?raw=true">
</p>

With `/pos delete {name}` you can easily delete your saved locations.

### Zone
Zone gives the opportunity to select an area that will be protected from enemies.\
The Zone function is build up of a friend/neutral/enemy table.

| Friends | Neutral | Enemy | 
|-----|-----|-----|
| Can add/remove other friends and are able to add/remove enemies. They will also get notified if an enemy is near their zone or in their zone. | Will be completely ignored (world will appear as an normal vanilla area). | Enemies will get bad effect by entering a zone, also they get notified if they`re near a zone where there are an enemy |

When all friends of a certain zone are offline it gets protected and not attackable for enemies. Neutrals will recognize nothing (no change).

__How to use the command__\
A zone can be added with ``/zone {name} x z x2 z2``. The coordinates define the size of the zone.
<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/zone-add-command.png?raw=true">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/zone-add-answer.png?raw=true">
</p>

After that you can look up for the friend list with ``/zone friend list {zoneName}`` or the enemy list `/zone enemy list {zoneName}`.
<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/zone-lists.png?raw=true">
</p>

Like you can see there are no enemies in the list and only one friend. You can fill these lists up with ``/zone friend add {name}`` or ``/zone enemy add {name}``.
<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/zone-add.png?raw=true">
</p>

To delete a zone with `/zone delete {name}`, but be carefull because this step cant be reversed!

```diff
- permission: manage.zone | ADD OR REMOVE ZONE
- permission: zone.noProtection | NO ZONE PROTECTION FOR USER
```

__Function__\
These messages will appear, if an enemy is near or in your zone:

_ENEMY SIDE_:
<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/zone-enemy-near.png?raw=true">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/zone-enemy-in.png?raw=true">
</p>

_FRIEND SIDE_:
<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/zone-friend-near.png?raw=true">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/zone-friend-in.png?raw=true">
</p>

### Tempban
`/tempban` is a simple admin command to ban a player for a certain time _m_ stands for minutes and _h_ stands for hours.

__How to use the command__\
Just type ``/tempban {name} {time} {m/h} {reason...``:

```diff
- permission: manage.ban | USE
```

### Tempunban
`/tempunban` is a simple admin command to unban a player.

__How to use the command__\
Just type ``/tempunban {name}``:

```diff
- permission: manage.unban | USE
```

### Chat
With `/chat` there is the possibility between a broadcast message and privat or public talk.

__How to use the command__\
To switch from public to privat talk type `/chat private {player} {player} {player} ...`.

To toggle back to public talk type `/chat public`.

To Broadcast type `/chat broadcast {message...` and it will appear bigger in chat. In the message you can use color codes with `&`.

```diff
- permission: chat.color | Color codes in chat
- permission: chat.broadcast | Broadcast function
```

## MiniBlocks
MiniBlock is a simple crafting feature that allows you to add textured heads to your survival world.\
You can add your own textured heads as an crafting recipe. In the `MiniBlockConfig.yml` you can set a minecraft material and the texture that should be the result of the crafting.
For example [(BIGGER LIST)](https://github.com/parcivad/mc-server-parcivad-control/blob/main/example/MiniBlockConfig.yml):
```yml
textures:
  APPLE: eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2JiMzExZjNiYTFjMDdjM2QxMTQ3Y2QyMTBkODFmZTExZmQ4YWU5ZTNkYjIxMmEwZmE3NDg5NDZjMzYzMyJ9fX0=
```
After the recipes are loaded (look up for console log message) it is possible to craft a miniBlock by using the minecraft material in the middle of the crafting table surrounded by glass blocks.
<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/miniBlockCrafting.png?raw=true">
</p>

When the material is not set in the `MiniBlockConfig.yml` there will be no crafting recipe!
## Tablist
The plugin will create other chat format and a prefix for players with these Roles

| Permission | Prefix |
|------------|---------------|
tablist.owner | Owner | {name}
tablist.mod | Mod | {name}
tablist.dev | Dev | {name}
everyone | Player | {name}

### comment
🧇just a hobby plugin, do not take the source code to serious 😉
 
