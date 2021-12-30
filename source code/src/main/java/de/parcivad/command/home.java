package de.parcivad.command;

import de.parcivad.tools.color;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import de.parcivad.main;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static de.parcivad.main.*;

public class home implements CommandExecutor, TabCompleter {

    public main plugin;
    public CheckActive checkActive;

    public home(main plugin) {
        this.plugin = plugin;
        checkActive = new CheckActive( plugin );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        // Cancel the command for console and check if it's active
        if (!(sender instanceof Player)) { log.warning(ANSI_RED + " That Command is not optimized for console!" + ANSI_RESET); return true; }
        if (!checkActive.check("home")) { sender.sendMessage( prefixDeny + color.important + "Dieser Befehl ist nicht aktiv!"); return true; }
        // get Player
        Player p = (Player) sender;

        if ( args.length == 1 && args[0].equals("set") ) { // /home set

            // Player Location
            Location loc = p.getLocation();
            // save in config
            plugin.PlayerConfig.get().set("User." + p.getUniqueId() + ".home", loc);
            plugin.PlayerConfig.save();
            // Send Feedback
            p.sendMessage( prefix + "Dein " + color.highlight + "Home " + color.normal + "wurde " + color.success + "abgespeichert. §7[ " + color.position  + loc.getBlockX() + "; " + loc.getBlockY() + "; " + loc.getBlockZ() + color.normal + "]");

        } else if ( args.length == 1 && args[0].equals("remove") ) { // /home remove

            // remove from the config
            plugin.PlayerConfig.get().set("User." + p.getUniqueId() + ".home", null);
            plugin.PlayerConfig.save();
            // Send Feedback
            p.sendMessage( prefix + "§7Dein §6Home §7wurde §cgelöscht.");

        } else if ( args.length == 0 ) { // / home

            // Check last use
            if ( !checkTimer(p) && !p.hasPermission("home.noTimer")) { return false; } else { setTimer(p); }

            if ( plugin.PlayerConfig.get().isSet("User." + p.getUniqueId() + ".home")) {
                // teleport
                p.teleport( Objects.requireNonNull(plugin.PlayerConfig.get().getLocation("User." + p.getUniqueId() + ".home")) );
                // feedback
                p.sendMessage( prefix + "Du wurdest zu deinem " + color.highlight + "Home " + color.normal + "teleportiert.");
            } else {
                p.sendMessage( prefix + "Du hast " + color.important + "kein " + color.highlight + "Home!");
                resetTimer(p);
            }

        } else {
            p.sendMessage( prefix + "Befehl: " + color.highlight + "/home {set/remove}");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args) {

        if ( args.length == 1 ) {
            String[] firstArguments = new String[]{"set", "remove"};
            return Arrays.asList(firstArguments);
        }

        return null;
    }

    // open a timer in a hashmap up
    private void setTimer(Player p) {
        // Timestamp
        long unixTime = System.currentTimeMillis() / 1000L;
        // save
        homeWait.put(p, unixTime);
    }

    // delete timer from hashmap
    private void resetTimer(Player p) {
        homeWait.remove(p);
    }

    // check timer from player in hashmap
    private boolean checkTimer(Player p) {
        // skip check (player has no wait timer)
        if ( !homeWait.containsKey(p) ) { return true;}
        // Timestamp
        long unixTime = System.currentTimeMillis() / 1000L;
        long difference = unixTime - homeWait.get(p);
        long timeToWait = 300 - difference;

        if ( difference >= 300 ) {
            return true;
        } else {
            if ( !p.hasPermission("home.noTimer") ) {
                p.sendMessage( prefix + color.important + "Dieser Befehl kann erst in " + timeToWait + "sek. genutzt werden!");
            }
        }

        return false;
    }
}
