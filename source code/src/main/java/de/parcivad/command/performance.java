package de.parcivad.command;

import de.parcivad.main;
import de.parcivad.tools.color;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.HashMap;

import static de.parcivad.tools.tpsTracker.*;
import static de.parcivad.main.*;

public class performance implements CommandExecutor {

    public main plugin;
    public CheckActive checkActive;

    public performance(main plugin) {
        this.plugin = plugin;
        checkActive = new CheckActive(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        // Cancel the command for console and check if it's active
        if ( !(sender instanceof Player) ) { log.warning(ANSI_RED + " That Command is not optimized for console!" + ANSI_RESET); return true; }
        if ( !checkActive.check("performance") ) { sender.sendMessage( prefixDeny + color.important + "Dieser Befehl ist nicht aktiv!"); return true; }
        // get player
        Player p = (Player) sender;

        String tpsIcon = "";
        String memoryIcon = "";
        if ( tpsRecords.containsKey( tpsRecords.size() - 1 ) ) { tpsIcon = hexColor( tpsRecords.get( tpsRecords.size() - 1) ); }
        if ( memoryRecords.containsKey( memoryRecords.size() - 1 ) ) { memoryIcon = hexColorMemory( memoryRecords.get( memoryRecords.size() - 1 ) ); }

        // introduction of statistic
        p.sendMessage( prefix + "Server system status" );
        p.sendMessage(" ");
        p.sendMessage( serverState() );
        p.sendMessage(color.success + tpsIcon + "•" + color.normal + " §otps");
        p.spigot().sendMessage( dataBlock( tpsRecords ));
        p.sendMessage(color.success + memoryIcon + "•" + color.normal + " §omemory use");
        p.spigot().sendMessage( dataBlockMemory( memoryRecords ));
        p.sendMessage(" ");

        return true;
    }

    private TextComponent dataBlock(HashMap<Integer, Float> quantity) {

        TextComponent dataBlock = new TextComponent();
        // prefix
        dataBlock.addExtra("§8[");

        for ( int i = 0; i < 60; i++ ) {

            if ( quantity.containsKey(i) ) {
                // get Timestamp by adding each minute to the started recording time
                String time = "§cMeasuring";
                if ( recordIDAtTimestamp.containsKey( (int) quantity.keySet().toArray()[i] ) ) {
                    // set time
                    Timestamp recordIDTimestamp = recordIDAtTimestamp.get( (int) quantity.keySet().toArray()[i] );
                    time = "§9" + recordIDTimestamp.getHours() + ":" + recordIDTimestamp.getMinutes();
                }
                Float tps = quantity.get(i);

                // if there is no next measured point "this" point is the currently measured = bold text
                String bold = "";
                if ( !quantity.containsKey( i + 1 ) ) { bold = "§l"; }

                // build TextComponent to add to the dataBlock
                TextComponent block = new TextComponent( hexColor(tps) + bold + "|");
                block.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(time  + " §7- " + hexColor(tps) + tps + "§7tps")));
                dataBlock.addExtra(block);

            } else {
                // build TextComponent to add to the dataBlock
                TextComponent block = new TextComponent( "§7|");
                block.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§cData point not measured yet")));
                dataBlock.addExtra(block);
            }
        }

        // suffix
        dataBlock.addExtra("§r§8]");
        return dataBlock;
    }

    private TextComponent dataBlockMemory(HashMap<Integer, Float> quantity) {

        TextComponent dataBlock = new TextComponent();
        // prefix
        dataBlock.addExtra("§8[");

        for ( int i = 0; i < 60; i++ ) {

            if ( quantity.containsKey(i) ) {
                // get Timestamp by adding each minute to the started recording time
                // get Timestamp by adding each minute to the started recording time
                String time = "§cMeasuring";
                if ( recordIDAtTimestamp.containsKey( (int) quantity.keySet().toArray()[i] ) ) {
                    // set time
                    Timestamp recordIDTimestamp = recordIDAtTimestamp.get( (int) quantity.keySet().toArray()[i] );
                    time = "§9" + recordIDTimestamp.getHours() + ":" + recordIDTimestamp.getMinutes();
                }
                Float memory = quantity.get(i);

                // if there is no next measured point "this" point is the currently measured = bold text
                String bold = "";
                if ( !quantity.containsKey( i + 1 ) ) { bold = "§l"; }

                // build TextComponent to add to the dataBlock
                TextComponent block = new TextComponent( hexColorMemory(memory) + bold + "|");
                block.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(time + " §7- " + hexColorMemory(memory) + memory + "§7gb")));
                dataBlock.addExtra(block);

            } else {
                // build TextComponent to add to the dataBlock
                TextComponent block = new TextComponent( "§7|");
                block.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§cData point not measured yet")));
                dataBlock.addExtra(block);
            }
        }

        // suffix
        dataBlock.addExtra("§r§8]");
        return dataBlock;
    }

    private String serverState() {
        // check if data is provided
        if ( !tpsRecords.containsKey( tpsRecords.size() - 1 ) ) { return "       §c⦿ §r§7No Data       "; }
        Float currentTps = tpsRecords.get( tpsRecords.size() - 1 );

        // simple table of colors
        if ( currentTps < 15 ) {
            return "       " + hexColor( currentTps ) + "⦿ §r" + "§7Server overloaded       ";
        } else if ( currentTps < 17) {
            return "       " + hexColor( currentTps ) + "⦿ §r" + "§7Problematic       ";
        } else if ( currentTps < 18 ) {
            return "       " + hexColor( currentTps ) + "⦿ §r" + "§7Entity Lag       ";
        } else {
            return "       " + hexColor( currentTps ) + "⦿ §r" + "§7Operational       ";
        }
    }

    private String hexColor(Float Index ) {
        // simple table of colors
        if ( Index < 15 ) {
            return "§4";
        } else if ( Index < 17) {
            return "§c";
        } else if ( Index < 18 ) {
            return "§6";
        } else {
            return "§a";
        }
    }

    private String hexColorMemory(Float Index ) {
        Index = Index * 1000000000;
        // simple table of colors
        if ( Index > Runtime.getRuntime().totalMemory() * 0.9 ) {
            return "§4";
        } else if ( Index > Runtime.getRuntime().totalMemory() * 0.85 ) {
            return "§c";
        } else if ( Index > Runtime.getRuntime().totalMemory() * 0.75 ) {
            return "§6";
        } else {
            return "§a";
        }
    }
    
}
