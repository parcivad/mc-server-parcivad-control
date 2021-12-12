package de.parcivad.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import de.parcivad.main;

import static de.parcivad.main.*;
import static java.lang.String.valueOf;

import de.parcivad.tools.*;

import java.util.Arrays;
import java.util.List;

public class commands implements CommandExecutor, TabCompleter {

    public main plugin;

    public commands(main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        // Cancel the command for console
        if ( !(sender instanceof Player) ) { System.out.println(ANSI_RED + " That Command is not optimized for console!" + ANSI_RESET); return true; }
        // get player
        Player p = (Player) sender;

        if ( p.hasPermission("manage.commands")) {
            // has the command all arguments
            if ( args.length == 2 ) {
                // check for the only allowed parameters "true" or "false"
                boolean status = false;
                if (args[1].equals("on")) { status = true; }

                if ( plugin.ServerConfig.get().isSet("Server.commands." + args[0]) ) {

                    plugin.ServerConfig.get().set("Server.commands." + args[0], status);
                    plugin.ServerConfig.save();

                    p.sendMessage(prefix + "Der Status von dem Befehl " + color.info + args[0] + color.normal + " wurde geändert." );

                } else {
                    p.sendMessage( prefix + "Dieser Befehl " + color.important + "gibt es nicht!");
                }

            } else {
                p.sendMessage( prefix + "Befehl: " + color.highlight + "/commands {command} {on/off}");
            }
        } else {
            p.sendMessage( prefixDeny + "Dafür hast du keine Berechtigung.");
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args ) {

        switch ( args.length ) {
            case 1:
                String[] firstArguments = new String[]{"tpa", "lock", "pos", "ec", "inv", "spawn", "stats", "seed", "home", "zone"};
                return Arrays.asList(firstArguments);
            case 2:
                String[] secondArguments = new String[]{"on", "off" };
                return Arrays.asList(secondArguments);
        }

        return null;
    }
}
