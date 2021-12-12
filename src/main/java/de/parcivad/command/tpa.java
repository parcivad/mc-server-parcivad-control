package de.parcivad.command;

import de.parcivad.tools.color;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import de.parcivad.main;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.parcivad.main.*;


public class tpa implements CommandExecutor, TabCompleter {

    public main plugin;
    public CheckActive checkActive;

    public tpa(main plugin) {
        this.plugin = plugin;
        checkActive = new CheckActive( plugin );
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        // Cancel the command for console and check if it's active
        if ( !(sender instanceof Player) ) { System.out.println(ANSI_RED + " That Command is not optimized for console!" + ANSI_RESET); return false; }
        if ( !checkActive.check("tpa") ) { sender.sendMessage( prefixDeny + color.important + "Dieser Befehl ist nicht aktiv!"); return false; }
        // get player
        Player p = (Player) sender;

        if ( args.length == 1 && Bukkit.getPlayer(args[0]) instanceof Player) {
            if ( p == Bukkit.getPlayer(args[0])) { p.sendMessage(prefixDeny + "Du kannst dich nicht zu dir selbst teleportieren!"); return true; }
            // Is the player on the server
            try {
                // Getting player to send
                Player p2 = Bukkit.getPlayer(args[0]);

                // Set it in a HashMap
                main.tpaPlayer.put(p2, p);

                // Text Components ---------------------------------------- MESSAGE FOR PLAYER 1
                TextComponent defaultMessagePlayer1 = new TextComponent(prefix + "§7Die Anfrage wurde an §6" + p2.getDisplayName() + "§7 geschickt!");

                TextComponent revoke = new TextComponent(" §7[§cREVOKE§7]");
                revoke.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa revoke"));
                revoke.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§cTPA zurücknehmen")));

                TextComponent confirmation = new TextComponent();
                confirmation.addExtra(defaultMessagePlayer1);
                confirmation.addExtra(revoke);


                // Text Components ---------------------------------------- MESSAGE FOR PLAYER 2
                TextComponent defaultMessagePlayer2 = new TextComponent(prefix + "Der Spieler " + color.highlight + p.getDisplayName() + color.normal + " möchte sich zu dir " + color.important + "teleportieren§7! Entscheide: §a§l/tpa ");

                TextComponent accept = new TextComponent("§a§laccept§r§7/");
                accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa accept"));
                accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§a§lTPA akzeptieren")));

                TextComponent denied = new TextComponent("§c§ldeny");
                denied.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa deny"));
                denied.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§c§lTPA ablehnen")));

                TextComponent acceptanddenied = new TextComponent();
                acceptanddenied.addExtra(defaultMessagePlayer2);
                acceptanddenied.addExtra(accept);
                acceptanddenied.addExtra(denied);


                // Send Message
                p2.spigot().sendMessage(acceptanddenied);
                p.spigot().sendMessage(confirmation);

            } catch (Exception ex) {
                p.sendMessage(prefixDeny + "Der Spieler ist nicht erreichbar.");
            }

        } else if ( args.length == 1 && args[0].equals("accept")) {
            // Is the Player online check
            try {
                // Getting Player asked
                Player p2 = main.tpaPlayer.get(p);

                p2.teleport(p.getLocation());

                p2.sendMessage(prefix + "Teleport Anfrage §aangenommen!");
            } catch (Exception ex) {
                p.sendMessage(prefixDeny + "§cDer Spieler ist nicht mehr erreichbar.");
            }
            // Clearing HashMap
            main.tpaPlayer.clear();
        } else if ( args.length == 1 && args[0].equals("deny")) {
            // Is the Player online check
            try {
                // Getting Player asked
                Player p2 = main.tpaPlayer.get(p);

                p2.sendMessage(prefix + "Teleport Anfrage §cabgelehnt!");

            } catch (Exception ex) {
                p.sendMessage(prefixDeny + "Der Spieler ist nicht mehr erreichbar.");
            }
            // Clearing HashMap
            main.tpaPlayer.clear();
        } else if ( args.length == 1 && args[0].equals("revoke")) {
            // Is the Player online check
            try {
                main.tpaPlayer.clear();

                p.sendMessage(prefix + "Teleport Anfrage §czurückgenommen!");

            } catch (Exception ex) {
                p.sendMessage(prefixDeny + "Der Spieler ist nicht mehr erreichbar.");
            }
            // Clearing HashMap
            main.tpaPlayer.clear();
        } else {
            p.sendMessage(prefix + "Befehl: " + color.highlight + "/tpa {player/revoke/accept/deny} {player}");
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args) {

        switch (args.length) {
            case 1:
                List<String> firstArguments = new ArrayList<>();
                firstArguments.addAll( Arrays.asList("revoke", "accept", "deny") );
                for ( Player all : Bukkit.getOnlinePlayers() ) {
                    firstArguments.add( all.getName() );
                }
                return firstArguments;
            case 2:
                List<String> secondArguments = new ArrayList<>();
                for ( Player all : Bukkit.getOnlinePlayers() ) {
                    secondArguments.add( all.getName() );
                }
                return secondArguments;
            default:
                return null;
        }
    }
}
