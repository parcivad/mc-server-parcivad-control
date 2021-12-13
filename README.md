[![JavaJDK](https://img.shields.io/badge/Java%20JDK-17-blue?logo=java&style=flat-square)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.18.1-success?logo=minecraft&style=flat-square)](https://minecraft.net)
[![Discord](https://img.shields.io/discord/690934524955197471?label=Discord&logo=discord&style=flat-square)](https://discord.gg/C2HrEeCQ)

# mc-server-parcivad-control
ParcivadControl is a plugin for your simple survival server. It brings some helpful commands for all your players.
You can customize or limit the commands with the `/commands` command that can turn commands of the plugin on or off.

## Tablist
The plugin will create plugin prefix for players with given permission. The prefix will also show up in chat with other format

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
