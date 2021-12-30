package de.parcivad.command;

import de.parcivad.tools.color;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import de.parcivad.main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static de.parcivad.main.*;


public class ec implements CommandExecutor, TabCompleter {

    public main plugin;
    public CheckActive checkActive;

    public ec(main plugin) {
        this.plugin = plugin;
        checkActive = new CheckActive( plugin );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        // Cancel the command for console and check if it's active
        if ( !(sender instanceof Player) ) { log.warning(ANSI_RED + " That Command is not optimized for console!" + ANSI_RESET); return true; }
        if ( !checkActive.check("ec") ) { sender.sendMessage( prefixDeny + color.important + "Dieser Befehl ist nicht aktiv!"); return true; }
        // get player
        Player p = (Player) sender;

        if ( args.length == 1 ) { // /ec {player}
            // Check if the player has permission to open a ender chest from another player
            if ( !p.hasPermission("enderchest.open")) { p.sendMessage(prefixDeny + "Du hast dafür keine Berechtigung");return true; }

            // only able to open when player online
            try {
                // target player
                Player p2 = Bukkit.getPlayer(args[0]);
                // get/open inventory
                Inventory enderChest = p2.getEnderChest();
                p.openInventory(enderChest);

            } catch (Exception ex) {
                p.sendMessage(prefix + color.important + "Spieler konnte nicht gefunden werden!");
            }

        } else { // /ec
            // Check if the player ever opened a enderchest
            if ( p.getStatistic(Statistic.ENDERCHEST_OPENED) >= 1 ) {
                // get/open inventory
                Inventory enderChest = p.getEnderChest();
                p.openInventory(enderChest);

            } else {
                p.sendMessage(prefix + "Es tut mir leid, du hast " + color.highlight + "noch nie " + color.normal +" eine §5EnderChest " +  color.highlight + "benutzt!");
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args ) {

        if ( args.length == 1 ) {
            List<String> firstArguments = new ArrayList<>();
            for (Player all : Bukkit.getOnlinePlayers() ) {
                firstArguments.add(all.getName());
            }
            return firstArguments;
        }

        return null;
    }
}
