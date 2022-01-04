package de.parcivad.command;

import de.parcivad.main;
import de.parcivad.tools.color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

import static de.parcivad.main.*;

public class chat implements CommandExecutor, TabCompleter {

    public main plugin;
    public CheckActive checkActive;

    public chat(main plugin) {
        this.plugin = plugin;
        checkActive = new CheckActive(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        // Cancel the command for console and check if it's active
        if ( !(sender instanceof Player) ) { log.warning(ANSI_RED + " That Command is not optimized for console!" + ANSI_RESET); return true; }
        if ( !checkActive.check("inv") ) { sender.sendMessage( prefixDeny + color.important + "This command is not active!"); }
        // get player
        Player p = (Player) sender;

        if ( args[0].isEmpty() ) { p.sendMessage(prefix + "Command: " + color.highlight + "/chat public/privat/broadcast {player/message} {player..."); return false; }

        switch ( args[0] ) {
            case "public":
                // check if player is on privat chat
                if ( pChat.containsKey( p ) ) {
                    // remove player from privat chat and feedback
                    pChat.remove(p);
                    p.sendMessage(prefix + "You returned to the " + color.success + "§lPUBLIC§r" + color.normal + " chat!");
                } else {
                    p.sendMessage( prefix + color.important + "No change" + color.normal + ", you are in " + color.success + "§lPUBLIC§r" + color.normal + " chat!" );
                }
                break;
            case "privat":
                if ( args.length > 1 ) {
                    // create player list
                    Set<Player> players = new HashSet<Player>();
                    StringBuilder playerList = new StringBuilder();
                    for ( int i = 1; i < args.length; i++ ) {
                        try {
                            // add player to set
                            Player argPlayer = Bukkit.getPlayer( args[i] );
                            if ( !players.contains(argPlayer) ) {
                                players.add( argPlayer );
                                // add player to readable string
                                playerList.append(" " + argPlayer.getName() );
                            }
                        } catch (Exception ex) {
                            // error player offline
                            p.sendMessage( prefixDeny + "Can't find player " + args[i] + "!");
                            return false;
                        }
                    }

                    // detailed feedback message
                    if ( pChat.containsKey( p ) ) p.sendMessage( prefix + "You still in " + color.important + "§lPRIVAT§r" + color.normal + " chat with:" + playerList.toString() );
                    else p.sendMessage( prefix + "You entered the " + color.important + "§lPRIVAT§r" + color.normal + " chat with:" + playerList.toString() );
                    // overwrite or write the HashMap with the privat chat
                    pChat.put(p, players);
                } else {
                    // help message
                    p.sendMessage(prefix + "Command: " + color.highlight + "/chat privat {player} {player} {player}...");
                }
                break;
            case "broadcast":
                // check permission for server broadcast
                if ( p.hasPermission("chat.broadcast") ) {
                    // get message with possible color codes
                    String msg = String.join( " ", args ).replace("broadcast ", "");
                    // send broadcast
                    Bukkit.broadcastMessage(prefix + color.info + "BROADCAST " + color.normal + "from " + color.important + p.getDisplayName() + "\n \n" + ChatColor.translateAlternateColorCodes('&', msg) + "\n \n" + prefix + "end of broadcast");
                } else {
                    // bad feedback
                    p.sendMessage(prefixDeny + "Dazu hast du keine Berechtigung!");
                }

                break;
            default:
                // help message
                p.sendMessage(prefix + "Command: " + color.highlight + "/chat public/privat/broadcast {player/message} {player...");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args) {
        // return on argument length
        switch ( args.length ) {
            case 1:
                String[] firstArguments = new String[]{"public", "privat", "broadcast"};
                return Arrays.asList(firstArguments);
            case 2:
                Set<String> allPlayer = new HashSet<>();
                for ( Player all : Bukkit.getOnlinePlayers() ) {
                    allPlayer.add( all.getName() );
                    // direct return of found player
                    if ( !args[1].isEmpty() && all.getName().startsWith( args[1]) ) {
                        return Collections.singletonList( all.getName() );
                    }
                }
                return new ArrayList<>(allPlayer);
            default:
                return null;
        }
    }


}
