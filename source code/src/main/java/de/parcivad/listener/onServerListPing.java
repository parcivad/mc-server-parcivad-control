package de.parcivad.listener;

import de.parcivad.main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class onServerListPing implements Listener {

    public main plugin;

    public onServerListPing(main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerListPin(ServerListPingEvent e) {

        if ( !plugin.ServerConfig.get().isSet("Server.Motd") ) {
            plugin.ServerConfig.get().set("Server.MotdLocked", "§7> parcivad network §clocked\n§6⚠§c Netzwerk für alle Spieler gesperrt!");
            plugin.ServerConfig.get().set("Server.Motd", "§7> parcivad network §aonline\n§aServer ist für alle Spieler offen.");
            plugin.saveConfig();
        }

        // Getting Motd
        String motd = plugin.ServerConfig.get().getString("Server.Motd");
        String motdlocked = plugin.ServerConfig.get().getString( "Server.MotdLocked");

        // Change Motd when server is locked
        if ( plugin.ServerConfig.get().getBoolean("Server.LockMode") ) {
            e.setMotd(motdlocked);
        } else {
            e.setMotd(motd);
        }

    }
}
