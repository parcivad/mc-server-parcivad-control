package de.parcivad.command;

import de.parcivad.main;
import de.parcivad.tools.color;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Stream;

import static de.parcivad.main.*;

public class zone implements CommandExecutor, TabCompleter {

    public main plugin;
    public CheckActive checkActive;

    public zone(main plugin) {
        this.plugin = plugin;
        checkActive = new CheckActive( plugin );
    }

    private final String noZoneErr = prefix + "There is" + color.important + " no zone " + color.normal + "where you are marked as a friend (only friends of a zone can use this command) or this " + color.important + "zone doesn't exist";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        // Cancel the command for console and check if it's active
        if ( !(sender instanceof Player) ) { log.warning(ANSI_RED + " That Command is not optimized for console!" + ANSI_RESET); return false; }
        if ( !checkActive.check("zone") ) { sender.sendMessage( prefixDeny + color.important + "Dieser Befehl ist nicht aktiv!"); return false; }
        // get player
        Player p = (Player) sender;

        if ( args.length == 0 ) { p.sendMessage(prefix + "Command: " + color.highlight + "/zone add/remove/friend/enemy"); return false;}

        // add or [remove/friend/enemy] (other functions than add need a existing zone)
        if ( args[0].equals("add") ) {//============================================================================================================================ ZONE ADD FUNCTION
            // permission check
            if ( !p.hasPermission("manage.zone") ) { p.sendMessage( prefixDeny + "Dafür hast du keine Berechtigung!"); return false;}
            // check if all arguments are present
            if ( args.length == 6 ) {
                // needed information
                String zoneName = args[1];
                String zoneID = args[2] + ":" + args[3] + ":" + args[4] + ":" + args[5];
                // double name and overlapping isn't allowed
                if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID ) ) {
                    if ( plugin.PlayerConfig.get().get("Zone." + zoneID + ".name").equals(zoneName) ) {
                        p.sendMessage( prefixDeny + "This name already exist!");
                        return false;
                    }
                }

                // add zone and the player to the friend of the zone
                plugin.PlayerConfig.get().set("Zone." + zoneID + ".name", zoneName);
                plugin.PlayerConfig.get().set("Zone." + zoneID + ".friend." + p.getUniqueId(), p.getName());
                plugin.PlayerConfig.save();
                // feedback
                p.sendMessage(prefix + "The zone " + color.highlight +  zoneName + color.success + " added!");
            } else {
                p.sendMessage(prefix + "Command: " + color.highlight + "/zone add {name} x z x2 z2");
            }

        } else if ( args[0].equals("edit") ) {
            // permission check
            if ( !p.hasPermission("manage.zone") ) { p.sendMessage( prefixDeny + "Dafür hast du keine Berechtigung!"); return false;}
            // check if all arguments are present
            if ( args.length == 6 ) {
                // needed information
                String zoneName = args[1];
                String zoneID = args[2] + ":" + args[3] + ":" + args[4] + ":" + args[5];
                // get zones and check their name, if the searched is present remove else feedback
                Set<String> zones = plugin.PlayerConfig.get().getConfigurationSection("Zone").getKeys(false);

                for ( String overwriteZone : zones ) {
                    if ( plugin.PlayerConfig.get().getString("Zone." + overwriteZone + ".name" ).equals(zoneName) ) {
                        // add zone with old data
                        plugin.PlayerConfig.get().set("Zone." + zoneID + ".name", zoneName);
                        plugin.PlayerConfig.get().set("Zone." + zoneID + ".friend", plugin.PlayerConfig.get().get("Zone." + overwriteZone + ".friend") );
                        plugin.PlayerConfig.get().set("Zone." + zoneID + ".enemy", plugin.PlayerConfig.get().get("Zone." + overwriteZone + ".enemy") );
                        plugin.PlayerConfig.save();
                        // delete old zone
                        plugin.PlayerConfig.get().set("Zone." + overwriteZone, null);
                        // feedback
                        p.sendMessage(prefix + "The zone " + color.highlight +  zoneName + color.success + " got overwritten " + color.normal + "[ " + color.info + overwriteZone + color.important + " => " + color.info + zoneID + color.normal + " ]!");
                        return true;
                    }
                }
                // bad feedback
                p.sendMessage( prefixDeny + "Can't find zone to overwrite!");
                return false;
            } else {
                p.sendMessage(prefix + "Command: " + color.highlight + "/zone edit {name} x z x2 z2");
            }

        } else {
            // all arguments below need a minimum of one zone!
            if ( !plugin.PlayerConfig.get().isSet("Zone")) { p.sendMessage( prefixDeny + "Zero zones exist!");}
            // get zones and check their name, if the searched is present remove else feedback
            Set<String> zones = plugin.PlayerConfig.get().getConfigurationSection("Zone").getKeys(false);

            if ( args[0].equals("remove") ) { //=================================================================================================================== ZONE REMOVE FUNCTION
                // permission check
                if ( !p.hasPermission("manage.zone") ) { p.sendMessage( prefixDeny + "Dafür hast du keine Berechtigung!"); return false;}

                // check if all arguments are present
                if ( args.length == 2 ) {
                    // get zones and check their name, if the searched is present remove
                    for ( String zone : zones ) {
                        if ( plugin.PlayerConfig.get().getString("Zone." + zone + ".name").equals(args[1]) ) {
                            // remove from config
                            plugin.PlayerConfig.get().set("Zone." + zone, null);
                            plugin.PlayerConfig.save();
                            // feedback
                            p.sendMessage(prefix + color.important + "Deleted " + color.normal + "the zone " + color.highlight + args[1] );
                            return true;
                        }
                    }
                    // negative feedback because no zone was found
                    p.sendMessage(prefixDeny + "There is no zone with the name " + color.highlight + args[1]);
                } else {
                    p.sendMessage(prefix + "Command: " + color.highlight + "/zone remove {name}");
                }

            } else if ( args[0].equals("friend") ) { //============================================================================================================= ZONE FRIEND FUNCTION
                // check if all arguments are present
                if ( args.length == 4 ) {
                    // try because the targeted player cloud be offline or not existing
                    try {
                        // get player
                        Player pTarget = Bukkit.getPlayer(args[2]);
                        String zoneName = args[3];

                        if ( args[1].equals("add") ) { // add a friend
                            // check if the player wants to add himself => block
                            if ( pTarget.getUniqueId().equals(p.getUniqueId()) ) { p.sendMessage( prefixDeny + "You can't add yourself!"); return true; }

                            // loop through zones to find the correct one where the player is a friend
                            for ( String zoneID : zones ) {
                                if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".friend." + p.getUniqueId() ) && plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name").equals(zoneName) ) {
                                    // detailed info to the player
                                    if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".enemy." + pTarget.getUniqueId() ) ) { // CHANCE from enemy to friend
                                        // remove from enemy list
                                        plugin.PlayerConfig.get().set("Zone." + zoneID + ".enemy." + pTarget.getUniqueId(), null);
                                        // feedback that it was a friend
                                        p.sendMessage(prefix + color.highlight + "Changed " + color.normal + "the player " + color.highlight + pTarget.getName() + color.normal + " from " + color.important + "enemy " + color.normal + "to " + color.success + "friend " + color.normal + "of the zone " + color.info + plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name") );

                                    } else if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".friend." + pTarget.getUniqueId() ) ) { // PLAYER IS ALREADY FRIEND
                                        // feedback that the player is already added
                                        p.sendMessage(prefix + "Player " + pTarget.getName() + color.important + " is already " + color.normal + "a friend of the zone " + color.info + plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name") );

                                    } else { // default
                                        // feedback
                                        p.sendMessage(prefix + "Player " + pTarget.getName() + color.success + " added " + color.normal + "as an friend of the zone " + color.info + plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name") );
                                    }

                                    // change config and save
                                    plugin.PlayerConfig.get().set("Zone." + zoneID + ".friend." + pTarget.getUniqueId(), pTarget.getName());
                                    plugin.PlayerConfig.save();
                                    return true;
                                }
                            }
                            // negative feedback in case no zone was found
                            p.sendMessage(noZoneErr);

                        } else if ( args[1].equals("remove") ) { // remove a friend
                            // check if the player wants to remove himself => block
                            if ( pTarget.getUniqueId().equals(p.getUniqueId()) ) { p.sendMessage( prefixDeny + "You can't remove yourself!"); return true; }

                            // loop through zones to find the correct one where the player is a friend
                            for ( String zoneID : zones ) {
                                if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".friend." + p.getUniqueId() ) && plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name").equals(zoneName)  ) {
                                    // is the player a friend?
                                    if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".friend." + p.getUniqueId() ) ) {
                                        // set player
                                        plugin.PlayerConfig.get().set("Zone." + zoneID + ".friend." + pTarget.getUniqueId(), null);
                                        plugin.PlayerConfig.save();
                                        // feedback
                                        p.sendMessage(prefix + color.important + "Removed player " + color.normal + "from the zone " + color.highlight + plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name") );
                                    } else {
                                        // negative feedback because the targeted player wasn't a friend
                                        p.sendMessage( prefixDeny + pTarget.getName() + " is no friend of " + zoneName);
                                    }

                                    return true;
                                }
                            }
                            // negative feedback in case no zone was found
                            p.sendMessage(noZoneErr);
                        }
                    } catch (Exception ex) {
                        // get player
                        OfflinePlayer pTarget = Bukkit.getOfflinePlayer(args[2]);
                        String zoneName = args[3];

                        if ( args[1].equals("add") ) { // add a friend
                            // check if the player wants to add himself => block
                            if ( pTarget.getUniqueId().equals(p.getUniqueId()) ) { p.sendMessage( prefixDeny + "You can't add yourself!"); return true; }

                            // loop through zones to find the correct one where the player is a friend
                            for ( String zoneID : zones ) {
                                if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".friend." + p.getUniqueId() ) && plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name").equals(zoneName) ) {
                                    // detailed info to the player
                                    if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".enemy." + pTarget.getUniqueId() ) ) { // CHANCE from enemy to friend
                                        // remove from enemy list
                                        plugin.PlayerConfig.get().set("Zone." + zoneID + ".enemy." + pTarget.getUniqueId(), null);
                                        // feedback that it was a friend
                                        p.sendMessage(prefix + color.highlight + "Changed " + color.normal + "the player " + color.highlight + pTarget.getName() + color.normal + " from " + color.important + "enemy " + color.normal + "to " + color.success + "friend " + color.normal + "of the zone " + color.info + plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name") );

                                    } else if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".friend." + pTarget.getUniqueId() ) ) { // PLAYER IS ALREADY FRIEND
                                        // feedback that the player is already added
                                        p.sendMessage(prefix + "Player " + pTarget.getName() + color.important + " is already " + color.normal + "a friend of the zone " + color.info + plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name") );

                                    } else { // default
                                        // feedback
                                        p.sendMessage(prefix + "Player " + pTarget.getName() + color.success + " added " + color.normal + "as an friend of the zone " + color.info + plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name") );
                                    }

                                    // change config and save
                                    plugin.PlayerConfig.get().set("Zone." + zoneID + ".friend." + pTarget.getUniqueId(), pTarget.getName());
                                    plugin.PlayerConfig.save();
                                    return true;
                                }
                            }
                            // negative feedback in case no zone was found
                            p.sendMessage(noZoneErr);

                        } else if ( args[1].equals("remove") ) { // remove a friend
                            // check if the player wants to remove himself => block
                            if ( pTarget.getUniqueId().equals(p.getUniqueId()) ) { p.sendMessage( prefixDeny + "You can't remove yourself!"); return true; }

                            // loop through zones to find the correct one where the player is a friend
                            for ( String zoneID : zones ) {
                                if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".friend." + p.getUniqueId() ) && plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name").equals(zoneName)  ) {
                                    // is the player a friend?
                                    if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".friend." + p.getUniqueId() ) ) {
                                        // set player
                                        plugin.PlayerConfig.get().set("Zone." + zoneID + ".friend." + pTarget.getUniqueId(), null);
                                        plugin.PlayerConfig.save();
                                        // feedback
                                        p.sendMessage(prefix + color.important + "Removed player " + color.normal + "from the zone " + color.highlight + plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name") );
                                    } else {
                                        // negative feedback because the targeted player wasn't a friend
                                        p.sendMessage( prefixDeny + pTarget.getName() + " is no friend of " + zoneName);
                                    }

                                    return true;
                                }
                            }
                            // negative feedback in case no zone was found
                            p.sendMessage(noZoneErr);
                        }
                    }

                } else if ( args.length == 3 ) {
                    // present a list of all friends of the zone to the player
                    if ( args[1].equals("list") ) {
                        // get town name
                        String zoneName = args[2];
                        // loop through zones to find the correct one where the player is a friend
                        for ( String zoneID : zones ) {
                            if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".friend." + p.getUniqueId() ) && plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name").equals(zoneName)  ) {
                                // Set of friends to stream
                                Stream<String> friends = plugin.PlayerConfig.get().getConfigurationSection("Zone." + zoneID + ".friend").getKeys(false).stream();
                                // print out data message
                                p.sendMessage( prefix + "All friends of " + color.highlight + zoneName);
                                friends.forEach((friend) -> p.sendMessage( prefix + ">" + color.success + plugin.PlayerConfig.get().getString("Zone." + zoneID + ".friend." + friend)));
                                p.sendMessage(prefix + "ZoneID: " + color.info + zoneID);
                                return true;
                            }
                        }
                        // negative feedback
                        p.sendMessage(prefix + "There is" + color.important + " no zone " + color.normal + "where you are marked as a friend (only friends of a zone can use this command) or this zone doesn't exist");

                    } else { p.sendMessage(prefix + "Befehl: " + color.highlight + "/zone friend add/remove/list {player/name} {name}"); }
                } else { p.sendMessage(prefix + "Befehl: " + color.highlight + "/zone friend add/remove/list {player/name} {name}"); }

            } else if ( args[0].equals("enemy") ) { //================================================================================================================= ZONE ENEMY FUNCTION
                // check if all arguments are present
                if ( args.length == 4 ) {
                    try {
                        // get player
                        Player pTarget = Bukkit.getPlayer(args[2]);
                        String zoneName = args[3];

                        // add a player to the enemy list
                        if ( args[1].equals("add") ) {

                            //check if the player wants to add himself => block
                            if ( pTarget.getUniqueId().equals(p.getUniqueId()) ) { p.sendMessage( prefixDeny + "You can't add yourself!"); return true; }

                            // loop through zones to find the correct one where the player is a friend
                            for ( String zoneID : zones ) {
                                if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".friend." + p.getUniqueId() ) && plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name").equals(zoneName) ) {
                                    // detailed feedback for the player
                                    if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".friend." + pTarget.getUniqueId() ) ) { // CHANGE from friend to enemy
                                        // remove from enemy list
                                        plugin.PlayerConfig.get().set("Zone." + zoneID + ".friend." + pTarget.getUniqueId(), null);
                                        // feedback that it was a friend
                                        p.sendMessage(prefix + color.highlight + "Changed " + color.normal + "the player " + color.highlight + pTarget.getName() + color.normal + " from " + color.success + "friend " + color.normal + "to " + color.important + "enemy " + color.normal + "of the zone " + color.info + plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name") );

                                    } else if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".enemy." + pTarget.getUniqueId() ) ) { // ALREADY a enemy
                                        // feedback that the player is already added
                                        p.sendMessage(prefix + "Player " + pTarget.getName() + color.important + " is already " + color.normal + "a enemy of the zone " + color.info + plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name") );

                                    } else {
                                        // feedback
                                        p.sendMessage(prefix + "Player " + pTarget.getName() + color.success + " added " + color.normal + "as an enemy of the zone " + color.info + plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name") );
                                    }
                                    // change config and save
                                    plugin.PlayerConfig.get().set("Zone." + zoneID + ".enemy." + pTarget.getUniqueId(), pTarget.getName());
                                    plugin.PlayerConfig.save();
                                    // feedback that it was a friend
                                    return true;
                                }
                            }
                            // negative feedback because no zone was found
                            p.sendMessage(noZoneErr);

                        } else if ( args[1].equals("remove") ) {
                            // check if the player wants to add himself => block
                            if ( pTarget.getUniqueId().equals(p.getUniqueId()) ) { p.sendMessage( prefixDeny + "You can't remove yourself!"); return true; }

                            // loop through zones to find the correct one where the player is a friend
                            for ( String zoneID : zones ) {
                                if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".friend." + p.getUniqueId() ) && plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name").equals(zoneName)  ) {
                                    // check if the player is a enemy
                                    if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".enemy." + pTarget.getUniqueId() ) ) {
                                        // set player
                                        plugin.PlayerConfig.get().set("Zone." + zoneID + ".enemy." + pTarget.getUniqueId(), null);
                                        plugin.PlayerConfig.save();
                                        // feedback
                                        p.sendMessage(prefix + color.important + "Removed player " + color.normal + "from the zone " + color.highlight + plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name") );
                                    } else {
                                        // negative feedback because the targeted player wasn't a enemy
                                        p.sendMessage(prefixDeny + pTarget.getName() + " is no enemy of " + color.highlight + zoneName );
                                    }
                                    return true;
                                }
                            }
                            // negative feedback because no zone was found in the for loop
                            p.sendMessage(noZoneErr);
                        }
                    } catch (Exception ex) {
                        // get player
                        OfflinePlayer pTarget = Bukkit.getOfflinePlayer(args[2]);
                        String zoneName = args[3];

                        // add a player to the enemy list
                        if ( args[1].equals("add") ) {

                            //check if the player wants to add himself => block
                            if ( pTarget.getUniqueId().equals(p.getUniqueId()) ) { p.sendMessage( prefixDeny + "You can't add yourself!"); return true; }

                            // loop through zones to find the correct one where the player is a friend
                            for ( String zoneID : zones ) {
                                if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".friend." + p.getUniqueId() ) && plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name").equals(zoneName) ) {
                                    // detailed feedback for the player
                                    if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".friend." + pTarget.getUniqueId() ) ) { // CHANGE from friend to enemy
                                        // remove from enemy list
                                        plugin.PlayerConfig.get().set("Zone." + zoneID + ".friend." + pTarget.getUniqueId(), null);
                                        // feedback that it was a friend
                                        p.sendMessage(prefix + color.highlight + "Changed " + color.normal + "the player " + color.highlight + pTarget.getName() + color.normal + " from " + color.success + "friend " + color.normal + "to " + color.important + "enemy " + color.normal + "of the zone " + color.info + plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name") );

                                    } else if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".enemy." + pTarget.getUniqueId() ) ) { // ALREADY a enemy
                                        // feedback that the player is already added
                                        p.sendMessage(prefix + "Player " + pTarget.getName() + color.important + " is already " + color.normal + "a enemy of the zone " + color.info + plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name") );

                                    } else {
                                        // feedback
                                        p.sendMessage(prefix + "Player " + pTarget.getName() + color.success + " added " + color.normal + "as an enemy of the zone " + color.info + plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name") );
                                    }
                                    // change config and save
                                    plugin.PlayerConfig.get().set("Zone." + zoneID + ".enemy." + pTarget.getUniqueId(), pTarget.getName());
                                    plugin.PlayerConfig.save();
                                    // feedback that it was a friend
                                    return true;
                                }
                            }
                            // negative feedback because no zone was found
                            p.sendMessage(noZoneErr);

                        } else if ( args[1].equals("remove") ) {
                            // check if the player wants to add himself => block
                            if ( pTarget.getUniqueId().equals(p.getUniqueId()) ) { p.sendMessage( prefixDeny + "You can't remove yourself!"); return true; }

                            // loop through zones to find the correct one where the player is a friend
                            for ( String zoneID : zones ) {
                                if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".friend." + p.getUniqueId() ) && plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name").equals(zoneName)  ) {
                                    // check if the player is a enemy
                                    if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".enemy." + pTarget.getUniqueId() ) ) {
                                        // set player
                                        plugin.PlayerConfig.get().set("Zone." + zoneID + ".enemy." + pTarget.getUniqueId(), null);
                                        plugin.PlayerConfig.save();
                                        // feedback
                                        p.sendMessage(prefix + color.important + "Removed player " + color.normal + "from the zone " + color.highlight + plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name") );
                                    } else {
                                        // negative feedback because the targeted player wasn't a enemy
                                        p.sendMessage(prefixDeny + pTarget.getName() + " is no enemy of " + color.highlight + zoneName );
                                    }
                                    return true;
                                }
                            }
                            // negative feedback because no zone was found in the for loop
                            p.sendMessage(noZoneErr);
                        }
                    }

                } else if ( args.length == 3 ) {
                    if ( args[1].equals("list") ) {
                        // get town name
                        String zoneName = args[2];
                        // loop through zones to find the correct one where the player is a friend
                        for ( String zoneID : zones ) {
                            if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".friend." + p.getUniqueId() ) && plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name").equals(zoneName)  ) {
                                // check if there are enemies in the zone set
                                if ( !plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".enemy") ) { p.sendMessage(prefixDeny + "no enemies!"); return false; }
                                // Set of friends to stream
                                Stream<String> friends = plugin.PlayerConfig.get().getConfigurationSection("Zone." + zoneID + ".enemy").getKeys(false).stream();
                                // print out data message
                                p.sendMessage( prefix + "All enemies of " + color.highlight + zoneName);
                                friends.forEach((friend) -> p.sendMessage( prefix + ">" + color.important + plugin.PlayerConfig.get().getString("Zone." + zoneID + ".enemy." + friend)));
                                p.sendMessage(prefix + "ZoneID: " + color.info + zoneID);
                                return true;
                            }
                        }
                        // negative feedback
                        p.sendMessage(prefix + "There is " + color.important + "no zone " + color.normal + "where you are marked as a friend (only friends of a zone can use this command) or this zone doesn't exist");

                    } else { p.sendMessage(prefix + "Befehl: " + color.highlight + "/zone enemy add/remove/list {player/name} {name}"); }
                } else { p.sendMessage(prefix + "Befehl: " + color.highlight + "/zone enemy add/remove/list {player/name} {name}"); }
            }

        }

        // for security
        plugin.PlayerConfig.save();
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args) {
        // get player
        Player p = (Player) sender;
        String x = p.getLocation().getBlockX() + "";
        String z = p.getLocation().getBlockZ() + "";

        switch (args.length) {
            case 1:
                String[] firstArguments = new String[]{"add", "edit", "remove", "friend", "enemy"};
                return Arrays.asList(firstArguments);
            case 2:
                if ( plugin.PlayerConfig.get().isSet("Zone") ) {
                    // get all zones from the config
                    Stream<String> zones = plugin.PlayerConfig.get().getConfigurationSection("Zone").getKeys(false).stream();
                    // return list of all zones or add/remove option
                    if ( args[0].equals("remove") ) {
                        // collect zone names and return
                        Set<String> zoneNames = new HashSet<String>();
                        zones.forEach((zone) -> { zoneNames.add( plugin.PlayerConfig.get().getString("Zone." + zone + ".name") ); } );
                        return new ArrayList<String>(zoneNames);

                    } else if ( args[0].equals("friend") || args[0].equals("enemy" ) ){
                        String[] secondArguments = new String[]{"add", "remove", "list"};
                        return Arrays.asList(secondArguments);

                    }
                }
                if ( args[0].equals("add") || args[0].equals("edit") ) {
                    return Collections.singletonList("name");
                }
                break;
            case 3:
                // Set of all players on the server
                Set<String> allPlayer = new HashSet<>();
                for ( Player all : Bukkit.getOnlinePlayers() ) {
                    allPlayer.add( all.getName() );
                    // direct return of found player
                    if ( all.getName().startsWith( args[1]) ) {
                        return Collections.singletonList( all.getName() );
                    }
                }
                // return list of all player or location
                if ( args[1].equals("list") ) {
                    if ( plugin.PlayerConfig.get().isSet("Zone") ) {
                        // get all zones from the config
                        Stream<String> zones = plugin.PlayerConfig.get().getConfigurationSection("Zone").getKeys(false).stream();
                        // collect zone names and return
                        Set<String> zoneNames = new HashSet<String>();
                        zones.forEach((zone) -> { zoneNames.add( plugin.PlayerConfig.get().getString("Zone." + zone + ".name") ); } );
                        return new ArrayList<String>(zoneNames);
                    }
                } else if ( args[0].equals("friend") || args[0].equals("enemy") ) {
                    return new ArrayList<String>(allPlayer);

                } else if ( args[0].equals("add") || args[0].equals("edit") ) {
                    return Collections.singletonList( x );

                }
                break;
            case 5:
                if ( args[0].equals("add") || args[0].equals("edit") ) {
                    return Collections.singletonList( x );
                }
                break;
            case 4:
                if ( plugin.PlayerConfig.get().isSet("Zone") ) {
                    // get all zones from the config
                    Stream<String> zones = plugin.PlayerConfig.get().getConfigurationSection("Zone").getKeys(false).stream();
                    // return list of all zones or add/remove option
                    if ( args[0].equals("friend") || args[0].equals("enemy" ) ){
                        // collect zone names and return
                        Set<String> zoneNames = new HashSet<String>();
                        zones.forEach((zone) -> { zoneNames.add( plugin.PlayerConfig.get().getString("Zone." + zone + ".name") ); } );
                        return new ArrayList<String>(zoneNames);
                    }
                }
            case 6:
                if ( args[0].equals("add") || args[0].equals("edit") ) {
                    return Collections.singletonList( z );
                }
                break;
            default:
                return null;
        }
        return null;
    }
}
