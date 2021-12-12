package de.parcivad.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static de.parcivad.main.*;

public class onJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        // Set Role == Tablist Team
        if ( p.hasPermission("tablist.owner" ) ) {
            sb.getTeam("00000Owner").addPlayer(p);
            p.setDisplayName(sb.getTeam("00000Owner").getPrefix() + p.getName() + "§f");
        } else if ( p.hasPermission("tablist.mod")){
            sb.getTeam("00001Mod").addPlayer(p);
            p.setDisplayName(sb.getTeam("00001Mod").getPrefix() + p.getName()+ "§f");
        } else if ( p.hasPermission("tablist.dev") ) {
            sb.getTeam("00002Dev").addPlayer(p);
            p.setDisplayName(sb.getTeam("00002Dev").getPrefix() + p.getName()+ "§f");

            // Set Default role in case of no permission
        } else {
            sb.getTeam("00008Player").addPlayer(p);
            p.setDisplayName(sb.getTeam("00008Player").getPrefix() + p.getName()+ "§f");
        }

        for (Player all : Bukkit.getOnlinePlayers() ) {
            all.setScoreboard(sb);
        }

        e.setJoinMessage("§7§l>>§r§a " + p.getName() + " §r§7ist §aon.");

    }

}
