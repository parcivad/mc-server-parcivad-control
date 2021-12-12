package de.parcivad.listener;

import de.parcivad.main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static de.parcivad.main.*;

public class onQuit implements Listener {

    public main plugin;

    public onQuit(main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        nearZone.remove(p);
        zoneEntered.remove(p);
        e.setQuitMessage("§7§l<<§r§c " + p.getName() + " §r§7ist §coff.");
    }
}
