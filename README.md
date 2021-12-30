[![JavaJDK](https://img.shields.io/badge/Java%20JDK-17-orange?logo=java&style=flat-square)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.18.1-success?logo=minecraft&style=flat-square)](https://minecraft.net)
[![Spigot](https://img.shields.io/badge/Spigot-%20-orange?&style=flat-square)](http://spigotmc.org)
[![Paper](https://img.shields.io/badge/Paper-%20-orange?&style=flat-square)](https://papermc.io)
[![Discord](https://img.shields.io/discord/690934524955197471?label=Discord&logo=discord&style=flat-square)](https://discord.gg/C2HrEeCQ)

# parcivadControl
ParcivadControl is a mc plugin that brings some popular and helpful commands in your survival Server. It also includes a prefix Tablist, clean Chat and Admin-Commands.\
One of the biggest differences to other plugins is that you have to choice to decide what feature is active or not. With `/commands` you can turn specified features of and customize the plugin for your need. 

## Features
### Commands
All commands can be turned on or off with the `/commands` Command.\

__How to use the command__\
When you first installed the plugin and use a command the plugin will give you a deny for using it because its not active.
With `/commands` you can change the state to active

<p align="left">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/commands-turn-on.png?raw=true">
</p>

After that the command will work, you can easily turn it off again at any time.

### Enderchest
With `/ec`, short form for enderchest, you can open your own enderchest at any time in the game. Its like a "backpack" function. The only requirement needed for the player is the achievment of
opening a enderchest in this world. Roles with the permission `enderchest.open` are able to open the enderchest of other players.

__How to use the command__\
This command is not complex. Just type `/ec` and a inventory of your enderchest will popup. To open a enderchest of another player you can type his name at the second argument of the command.

<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/enderchest-other-player.png?raw=true">
</p>

### Teleportation Request
With the popular `/tpa` command you can ask other online players to teleport to them. They can accept your request or deny it. In the first case you would be teleported to the player you requested. Important to know is that there is no cooldown for the teleportation.

__How to use the command__\
To start you have to request a player for a teleportation. For that just type `/tpa {player}`.\
The player then will get a request in his chat
<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/tpa-request.png?raw=true">
</p>

The player can decide to accept or deny by typing the command. Alternative he can _hover_ over `accept` or `deny` and click to take action.
<p align="center">
  <img width="auto" height="auto" src="https://github.com/parcivad/mc-server-parcivad-control/blob/main/img/tpa-request-hover.png?raw=true">
</p>

> **DOCUMENTATION NOT DONE YET**


## Commands
List of all available commands

| Command | Use | Description |
|---------|--------------------------|------------------------|
| tpa | /tpa {player/revoke/accept/deny} {player} | Ask a player to teleport to his location
| lock | /lock {on/off/message} {message... | Only player with permission can join
| pos | /pos {save/delete/name} {name} | Saves a position for the player in a config
| ec | /ec {player} | A Command to open your enderchest immediately
| inv | /inv {player} | Opens the inventory of another player
| spawn | /spawn | teleports the player to the spawn
| stats | /stats {player} | Shows some statistics form the world data
| seed | /seed | show the player the seed
| tempban | /tempban {player} {time} {m/h} {reason... | Bans a player for a certain time
| tempunban | /tempunban {player} | Unbans a tempbaned player
| commmands | /commands {command} {on/off} | Turn on or off commands of the plugin
| home | /home {set/remove} | Set a home and teleport each 5 minutes to the position
| zone | /zone {add/remove/friend/enemy} {add/remove/list/x} {player/z} {zoneName/x2} {z2} | Lets you set a zone with a specified name, all the friends of a zone are able to add/remove enemies or add/remove friends of the zone. When all friends of a zone are offline the area will be protected to enemies by the plugin. If the friends are online enemies will get a warning by entering the zone and will be given bad effects.

## Permissions
| Permission | Use |
|------------|---------------|
enderchest.open | Player is able to open the enderchest of another player
inventory.open | Player is able to open the inventory of another player
tablist.owner | Gives the player owner prefix
tablist.mod | Gives the player moderator prefix
tablist.dev | Gives the player developer prefix
manage.lock | Player is able to lock or unlock the server for others
manage.ban | Player is able to tempban other players
manage.unban | Player is able to unban other players
manage.zone | Player is able to create or delete a zone with x and z coordinates
home.noWait | Player has no 5 minute limit for home teleportation
zone.noProtection | Player is not affected by the offline zone protection 

## SOON
shop feature maybe comming soon

### comment
🧇just a hobby plugin, do not take the source code to serious 😉
