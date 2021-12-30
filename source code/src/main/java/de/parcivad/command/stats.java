package de.parcivad.command;

import de.parcivad.tools.color;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import de.parcivad.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static de.parcivad.main.*;

public class stats implements CommandExecutor, TabCompleter {

    public main plugin;
    public CheckActive checkActive;

    public stats(main plugin) {
        this.plugin = plugin;
        checkActive = new CheckActive( plugin );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        // Cancel the command for console and check if it's active
        if ( !(sender instanceof Player) ) { log.warning(ANSI_RED + " That Command is not optimized for console!" + ANSI_RESET); return true; }
        if ( !checkActive.check("stats") ) { sender.sendMessage( prefixDeny + color.important + "Dieser Befehl ist nicht aktiv!"); return true; }
        // get player
        Player p = (Player) sender;

        if ( args.length == 0 ) {
            // send stats
            sendStats(p, p);

        } else if ( args.length == 1 ) {

            try {
                // get player
                Player p2 = Bukkit.getPlayer(args[0]);
                // print statistics
                sendStats(p, p2);
            } catch (Exception ex) {
                // feedback
                p.sendMessage(prefixDeny + "Der Spieler ist nicht erreichbar!");
            }

        } else {
            p.sendMessage(prefix + "Befehl: " + color.highlight + "/stats {player}");
        }


        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args) {

        if ( args.length == 1 ) {
            List<String> firstArguments = new ArrayList<>();
            for ( Player all : Bukkit.getOnlinePlayers() ) {
                firstArguments.add( all.getName() );
            }
            return firstArguments;
        }

        return null;
    }


    /**
     * Send statistic by function
     * @param p Player to send the stats
     * @param p2 Player to take the stats from
     */
    public void sendStats(Player p, Player p2) {
        p.sendMessage(prefix + "Statistiken von " + p2.getDisplayName() );
        p.sendMessage(prefix + "> Player " + color.important + "Kills§7: " + p2.getStatistic(Statistic.PLAYER_KILLS) );
        p.sendMessage(prefix + "> Mob " +  color.important + "Kills§7: " + p2.getStatistic(Statistic.MOB_KILLS) );
        p.sendMessage(prefix + "> Tode: " + p2.getStatistic(Statistic.DEATHS) );
        p.sendMessage(prefix + "§ostatistiken sind von der world data");
    }
}
