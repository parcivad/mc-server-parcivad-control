package de.parcivad.listener;

import de.parcivad.main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static de.parcivad.main.*;

public class onPlayerDeath implements Listener {

    public main plugin;

    public onPlayerDeath(main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        // get player and send location
        Player p = e.getEntity();
        Location pos = p.getLocation();
        p.sendMessage( prefix + "§7Todes Kooridnaten: [§9§l" + pos.getBlockX() + "§7, §9§l" + pos.getBlockY() + "§7, §9§l" + pos.getBlockZ() + "§7]");
        log.info(p.getName() + " Ist hier gestorben: " + pos.getBlockX() + ", " + pos.getBlockY() + ", " + pos.getBlockZ() );
    }
}
