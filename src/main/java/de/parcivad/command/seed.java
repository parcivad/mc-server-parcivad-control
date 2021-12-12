package de.parcivad.command;

import de.parcivad.tools.color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import de.parcivad.main;

import static de.parcivad.main.*;

public class seed implements CommandExecutor {

    public main plugin;
    public CheckActive checkActive;

    public seed(main plugin) {
        this.plugin = plugin;
        checkActive = new CheckActive( plugin );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        // Cancel the command for console and check if it's active
        if ( !(sender instanceof Player) ) { System.out.println(ANSI_RED + " That Command is not optimized for console!" + ANSI_RESET); return true; }
        if ( !checkActive.check("seed") ) { sender.sendMessage( prefixDeny + color.important + "Dieser Befehl ist nicht aktiv!"); return true; }
        // get player
        Player p = (Player) sender;

        // If the Player is op...
        if ( !p.isOp() ) {
            p.sendMessage( prefixDeny + "Seed ist nicht zugänglich!");
        } else {
            p.sendMessage( prefix + color.success + "Seed: " + Bukkit.getWorld(p.getWorld().getName()).getSeed());
        }

        return false;
    }
}