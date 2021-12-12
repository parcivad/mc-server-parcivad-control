package de.parcivad.command;

import de.parcivad.tools.color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import de.parcivad.main;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static de.parcivad.main.*;

public class spawn implements CommandExecutor {

    public main plugin;
    public CheckActive checkActive;

    public spawn(main plugin) {
        this.plugin = plugin;
        checkActive = new CheckActive( plugin );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        // Cancel the command for console and check if it's active
        if ( !(sender instanceof Player) ) { System.out.println(ANSI_RED + " That Command is not optimized for console!" + ANSI_RESET); return true; }
        if ( !checkActive.check("spawn") ) { sender.sendMessage( prefixDeny + color.important + "Dieser Befehl ist nicht aktiv!"); return true; }
        // get player
        Player p = (Player) sender;

        // teleport to spawn
        p.teleport(Bukkit.getWorld("world").getSpawnLocation());
        p.sendMessage(prefix + "Du wurdest zum " + color.highlight + "Spawn " + color.normal + "teleportiert!");

        return false;
    }
}
