package de.parcivad.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class onChat implements Listener {
    
    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent e) {
        // get message
        Player p = e.getPlayer();
        String msg = e.getMessage();
        // reform
        e.setFormat(p.getDisplayName() + "§r " + msg);
    }
    
}
