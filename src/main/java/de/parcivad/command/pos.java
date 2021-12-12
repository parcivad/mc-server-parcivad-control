package de.parcivad.command;

import de.parcivad.tools.color;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import de.parcivad.main;

import java.util.*;

import static de.parcivad.main.*;

public class pos implements CommandExecutor, TabCompleter {

    public main plugin;
    public CheckActive checkActive;

    public pos(main plugin) {
        this.plugin = plugin;
        checkActive = new CheckActive( plugin );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        // Cancel the command for console and check if it's active
        if ( !(sender instanceof Player) ) { System.out.println(ANSI_RED + " That Command is not optimized for console!" + ANSI_RESET); return false; }
        if ( !checkActive.check("pos") ) { sender.sendMessage( prefixDeny + color.important + "Dieser Befehl ist nicht aktiv!"); return false; }
        // get player
        Player p = (Player) sender;

        if ( args.length == 2 && args[0].equals("save") ) { // /pos save {name}
            // get current position and name
            Location pos = p.getLocation();
            String posName = args[1].toString();

            // Catch home overwrite ( home command uses a position save as "home")
            if ( posName.equals("home") ) { p.sendMessage( prefixDeny + "Dieser Name ist geschützt!"); return false; }

            // exist the name
            if ( !plugin.PlayerConfig.get().isSet("User." + p.getUniqueId() + ".pos." + posName) ) {
                // Save it in PlayerConfig
                plugin.PlayerConfig.get().set("User." + p.getUniqueId() + ".pos." + posName, pos);
                plugin.PlayerConfig.save();

                // Sending Player a Message
                p.sendMessage(prefix + "Die Position " + color.position + posName + color.normal + " wurde " + color.success + "abgespeichert!");

            } else {
                p.sendMessage(prefixDeny + "Es existiert bereits eine Position mit diesem Namen!");
            }

        } else if ( args.length == 2 && args[0].equals("delete") ) { // /pos delete {name}
            // get name
            String posName = args[1].toString();

            // Catch home overwrite ( home command uses a position save as "home")
            if ( posName.equals("home") ) { p.sendMessage( prefixDeny + "Dieser Name ist geschützt!"); return false; }

            // Is there any Location with the same name
            if ( plugin.PlayerConfig.get().isSet("User." + p.getUniqueId() + ".pos." + posName) ) {
                // Save it in PlayerConfig
                plugin.PlayerConfig.get().set("User." + p.getUniqueId() + ".pos." + posName, null);
                plugin.PlayerConfig.save();

                // Sending Player a Message
                p.sendMessage(prefix + "Die Position " + color.position + posName + color.normal + " wurde §cgelöscht!");

            } else {
                p.sendMessage(prefixDeny + "Es existiert keine Position mit diesem Namen!");
            }

        } else if ( args.length == 1 ) { // /pos {name}

            String posName = args[0].toString();

            // Is there any Location the name
            if ( plugin.PlayerConfig.get().isSet("User." + p.getUniqueId() + ".pos." + posName) ) {
                Location pos = plugin.PlayerConfig.get().getLocation("User." + p.getUniqueId() + ".pos." + posName);
                p.sendMessage(prefix + "Die Position " + color.highlight + posName + color.normal + " wurde aufgerufen [" + color.position + pos.getBlockX() + "; " + pos.getBlockY() + "; " + pos.getBlockZ() + color.normal + "]");
            } else {
                p.sendMessage(prefix + "Diese Location " + color.important + "existiert " + color.normal + "unter deinem Account " + color.important  + "nicht!");
            }

        } else {
            p.sendMessage(prefix + "Befehl: " + color.highlight + "/pos {save/delete/name} {name}");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args) {

        switch ( args.length ) {
            case 1:
                String[] firstArguments = new String[]{"save", "delete", "name"};
                return Arrays.asList( firstArguments );
            case 2:
                return Collections.singletonList("name");
            default:
                return null;
        }
    }
}
