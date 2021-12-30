package de.parcivad.command;

import de.parcivad.tools.color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import de.parcivad.main;

import java.util.*;

import static de.parcivad.main.*;

public class lock implements CommandExecutor, TabCompleter {

    public main plugin;
    public CheckActive checkActive;

    public lock(main plugin) {
        this.plugin = plugin;
        checkActive = new CheckActive( plugin );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        // Check if command active
        if ( !checkActive.check("lock") ) { sender.sendMessage( prefixDeny + color.important + "Dieser Befehl ist nicht aktiv!"); return true; }
        // needed for any update or check of the current lockmode
        boolean lockmode = plugin.ServerConfig.get().getBoolean("Server.LockMode");

        // Use optimized command for console
        if ( !(sender instanceof Player) ) {
            // Setting Lock Message (FROM CONSOLE)
            if (args.length == 0) {return true;}

            if (args[0].equalsIgnoreCase("on")) {
                // define lockmode on - print out - kick
                log.info("Der Server ist nun gelockt!");
                lockmode = true;

                // kick all online player
                for (Player all : Bukkit.getOnlinePlayers() ) {
                    all.kickPlayer("§6⚠\n" + color.normal + "You got " + color.important + "kicked!\n\n" + color.highlight + "Reason:\n" + color.normal + "Server Locked from Console" );
                }

            } else if (args[0].equalsIgnoreCase("off")) {
                // system out
                log.info("Der Server ist nun entgelockt!");
                lockmode = false;

            }

            // update config state
            plugin.ServerConfig.get().set("Server.message", "Server Locked from Console");
            plugin.ServerConfig.get().set("Server.LockMode", lockmode);
            plugin.ServerConfig.save();
            return true;
        }

        // get player
        Player p = (Player) sender;

        if (p.hasPermission("server.settings")) {

            if (args.length == 1) { // /lock [on/off]

                // Lock the Server
                if ( args[0].equalsIgnoreCase("on") ) {
                    // set lockmode for update and get lock message
                    lockmode = true;
                    String message = (String) plugin.ServerConfig.get().get("Server.message");
                    // kick all online players
                    for (Player all : Bukkit.getOnlinePlayers() ) {
                        all.kickPlayer("§6⚠\n" + color.normal + "You got " + color.important + "kicked!\n\n" + color.highlight + "Reason:\n" + color.normal + "Server Locked from Console" );
                    }
                    // feedback
                    p.sendMessage(prefix + "Der Server befindet sich nun im " + color.important + "LOCKMODE!");

                } else if ( args[0].equalsIgnoreCase("off") ) {
                    // send feedback and set lockmode for update
                    p.sendMessage(prefix + "Der Server befindet sich nun nicht mehr im " + color.success + "LOCKMODE!");
                    lockmode = false;
                }

                // update lock mode
                plugin.ServerConfig.get().set("Server.LockMode", lockmode);
                plugin.ServerConfig.save();

            } else if ( args.length >= 2) { // /lock message {message}

                if (args[0].equalsIgnoreCase("message")) {

                    // For Message
                    ArrayList<String> message = new ArrayList<String>();
                    // Setting the Message
                    for (int i = 1; i < args.length; i++) {
                        message.add(args[i]);
                    }
                    // Message back to String. (right format)
                    String messageToSet = message.toString().replace("[", "").replace("]", "").replace(",", "");

                    // update config server message
                    plugin.ServerConfig.get().set("Server.message", messageToSet);
                    plugin.ServerConfig.save();
                    p.sendMessage(prefix + "Server LockMessage wurde auf: " + color.important + messageToSet + color.normal + " gesetzt!");


                } else {
                    p.sendMessage(prefix + "Befehl: " + color.highlight + "/lock [on/off/message] {optional message}");
                }
            } else {
                p.sendMessage(prefix + "Befehl: " + color.highlight + "/lock [on/off]");
            }
        } else {
            p.sendMessage(prefixDeny + "Dazu hast du keine Berechtigung!");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args) {
        switch ( args.length ) {
            case 1:
                String[] firstArgument = new String[]{"on", "off", "message"};
                return Arrays.asList(firstArgument);
            case 2:
                return Collections.singletonList("lock message");
            default:
                return null;
        }
    }
}
