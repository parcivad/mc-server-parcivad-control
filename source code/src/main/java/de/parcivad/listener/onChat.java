package de.parcivad.listener;

import de.parcivad.tools.color;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static de.parcivad.main.log;
import static de.parcivad.main.pChat;

public class onChat implements Listener {
    
    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent e) {
        // get message
        Player p = e.getPlayer();
        String msg = e.getMessage();
        // bring color in chat
        if ( p.hasPermission("chat.color") ) {
            msg = ChatColor.translateAlternateColorCodes('&', msg);
        }
        // person highlight
        String[] args = msg.split(" ");
        for ( int i = 0; i < args.length; i++ ) {
            // check for @ symbol
            if ( args[i].contains("@") ) args[i] = "§e§o" + args[i] + "§r";
        }
        msg = String.join(" ", args);

        // check for private chat interaction
        if ( pChat.containsKey(p) ) {
            // no chat event because privat
            e.setCancelled(true);

            TextComponent privat = new TextComponent( color.success + "§o§lprivat §r");
            // get all player names
            StringBuilder receivePlayers = new StringBuilder();
            for ( Player receive : pChat.get(p) ) {
                receivePlayers.append(receive.getName() + " ");
            }
            privat.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new Text("§a" + receivePlayers.toString()) ) );

            TextComponent totalMessage = new TextComponent();
            totalMessage.addExtra( privat );
            totalMessage.addExtra(color.normal + p.getName() + "§r " + msg );
            // send message to each player
            for ( Player receiver : pChat.get(p) ) {
                receiver.spigot().sendMessage( totalMessage );
            }
            // after that normal message for the sender
            p.spigot().sendMessage(totalMessage);

            // chat log
            log.info("§oprivat chat from " + p.getDisplayName() + " to [" + receivePlayers.toString() + "]:§r" + msg);
        } else {
            // continue public message
            e.setFormat(p.getDisplayName() + "§r " + msg);
        }
    }
    
}
