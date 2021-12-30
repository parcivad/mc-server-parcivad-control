package de.parcivad.command;

import de.parcivad.tools.color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import de.parcivad.main;

import java.util.ArrayList;
import java.util.List;

import static de.parcivad.main.*;

public class inv implements CommandExecutor, TabCompleter {

    public main plugin;
    public CheckActive checkActive;

    public inv(main plugin) {
        this.plugin = plugin;
        checkActive = new CheckActive( plugin );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        // Cancel the command for console and check if it's active
        if ( !(sender instanceof Player) ) { log.warning(ANSI_RED + " That Command is not optimized for console!" + ANSI_RESET); return true; }
        if ( !checkActive.check("inv") ) { sender.sendMessage( prefixDeny + color.important + "Dieser Befehl ist nicht aktiv!"); }
        // get player
        Player p = (Player) sender;

        if ( args.length == 1 ) { // /inv Player
            if ( p.hasPermission("inventory.open")) {

                // can only access online palyers
                try {
                    // target player
                    Player p2 = Bukkit.getPlayer(args[0]);
                    // get/open inventory
                    Inventory p2inv = p2.getInventory();
                    p.openInventory(p2inv);

                } catch (Exception ex) {
                    p.sendMessage(prefix + color.important + "Dieser Spieler ist nicht erreichbar!");
                }

            } else {
                p.sendMessage(prefixDeny + "Du hast dafür keine Berechtigung!");
            }
        } else {
            p.sendMessage(prefix + "Befehl: " + color.highlight + "/inv {player}");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args) {

        if ( args.length == 1 ) {
            List<String> firstArguments = new ArrayList<>();
            for( Player all : Bukkit.getOnlinePlayers() ) {
                firstArguments.add(all.getName());
            }
            return firstArguments;
        }

        return null;
    }
}
