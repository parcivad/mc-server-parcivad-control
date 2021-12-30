package de.parcivad.listener;

import de.parcivad.command.CheckActive;
import de.parcivad.main;
import de.parcivad.tools.color;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;
import java.util.stream.Stream;

import static de.parcivad.main.*;

public class ZoneListener implements Listener {

    public main plugin;
    public CheckActive checkActive;

    public ZoneListener(main plugin) {
        this.plugin = plugin;
        checkActive = new CheckActive(plugin);
    }

    /**
     * Check if the player is in a radius of a zone
     * @param e
     */
    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent e) {
        // zone function active check and get player
        if ( !checkActive.check("zone") ) { return; }
        Player p = e.getPlayer();

        // check if there are even zones
        if ( !plugin.PlayerConfig.get().isSet("Zone") ) { return; }

        // get zone data
        Location loc = p.getLocation();
        String zoneID = getZone(loc, 0);
        String nearbyZoneID = getZone(loc, 50);

        // check zone
        if ( zoneID != null ) {
            // check if already marked
            if ( !zoneEntered.containsKey(p) ) {
                if ( plugin.PlayerConfig.get().isSet("Zone." + zoneID + ".enemy." + p.getUniqueId() ) ) {
                    // update Hashmap
                    zoneEntered.put(p, zoneID);
                    nearZone.remove(p);
                    // bad effects
                    harm(p);
                    // notify
                    notifyFriends(zoneID, prefixDeny + color.normal + "An enemy " + color.important + "entered " + color.normal + "your zone!");
                    p.sendMessage(prefix + "You entered " + color.important + plugin.PlayerConfig.get().getString("Zone." + zoneID + ".name") + color.normal + " as an " + color.important + "enemy!" );

                } else {
                    noHarm(p);
                }
            } else if ( !zoneEntered.get(p).equals(zoneID) ) {
                // remove effect because the player entered a new zone => next movement will be checked new
                // set entered off
                zoneEntered.remove(p);
                // remove bad effects
                noHarm(p);
            }
            return;
        }

        if ( nearbyZoneID != null ) {
            // check if already marked
            if ( !nearZone.containsKey(p) && !zoneEntered.containsKey(p) ) {
                if ( plugin.PlayerConfig.get().isSet("Zone." + nearbyZoneID + ".enemy." + p.getUniqueId() ) ) {
                    // update Hashmap
                    nearZone.put(p, nearbyZoneID);
                    zoneEntered.remove(p);
                    // notify for the player
                    notifyFriends(nearbyZoneID, prefixDeny + color.normal + "An enemy " + color.highlight + "is near " + color.normal + "your zone!");
                    p.sendMessage(prefix + "You are " + color.highlight + "near a zone " + color.normal + "that marked you as an enemy!");
                }
            }
            return;
        }

        if ( zoneEntered.containsKey(p) || nearZone.containsKey(p) ) {
            // set entered off
            zoneEntered.remove(p);
            nearZone.remove(p);
            // remove bad effects
            noHarm(p);
        }
    }


    //================================================ Zone + Spawn protection when all friends are offline
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        // zone function active
        if ( !checkActive.check("zone") ) { return; }
        // permission check
        if ( e.getPlayer().hasPermission("zone.noProtection") ) { return; }
        // check if in zone
        String zoneID = getZone( e.getBlock().getLocation(), 0 );
        if ( zoneID != null ) {
            if ( friendsOffline( zoneID ) ) {
                if ( zoneEntered.containsKey(e.getPlayer()) ) {
                    e.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        // zone function active
        if ( !checkActive.check("zone") ) { return; }
        // permission check
        if ( e.getPlayer().hasPermission("zone.noProtection") ) { return; }
        // check if in zone
        String zoneID = getZone( e.getBlock().getLocation(), 0 );
        if ( zoneID != null ) {
            if ( friendsOffline( zoneID ) ) {
                if ( zoneEntered.containsKey(e.getPlayer()) ) {
                    e.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        // zone function active
        if ( !checkActive.check("zone") ) { return; }
        // check if in zone
        String zoneID = getZone( e.getLocation(), 0 );
        if ( zoneID != null ) {
            if ( friendsOffline( zoneID ) ) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onBlockBurn(BlockIgniteEvent e) {
        // zone function active
        if ( !checkActive.check("zone") ) { return; }
        // check if in zone
        String zoneID = getZone( e.getBlock().getLocation(), 1 );
        if ( zoneID != null ) {
            if ( friendsOffline( zoneID ) ) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        // zone function active
        if ( !checkActive.check("zone") ) { return; }
        // permission check
        if ( e.getPlayer().hasPermission("zone.noProtection") ) { return; }
        // check if in zone
        String zoneID = getZone( e.getPlayer().getLocation(), 0 );
        if ( zoneID != null ) {
            if ( friendsOffline( zoneID ) ) {
                if ( zoneEntered.containsKey(e.getPlayer()) ) {
                    e.setCancelled(true);
                }
            }
        }
    }
    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {
        // only milk because potion clear
        if( !e.getItem().getType().equals(Material.MILK_BUCKET)) { return; }
        // zone function active
        if ( !checkActive.check("zone") ) { return; }
        // permission check
        if ( e.getPlayer().hasPermission("zone.noProtection") ) { return; }
        // check if in zone
        if ( zoneEntered.containsKey(e.getPlayer()) ) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        // protect player only
        if ( e.getDamager() instanceof Player ) {
            Player p = (Player) e.getDamager();
            // check if he is an town
            String zoneID = getZone( p.getLocation(), 0 );
            if ( zoneID != null ) {
                if ( zoneEntered.containsKey(p) ) {
                    e.setDamage(0);
                    e.setCancelled(true);
                }
            }

        }  else if ( e.getDamager() instanceof Arrow){
            Arrow arrow = (Arrow) e.getDamager();
            // is the shooter a player or mob
            if ( arrow.getShooter() instanceof Player ) {
                Player p = (Player) arrow.getShooter();
                // check if he is an town
                String zoneID = getZone( p.getLocation(), 0 );
                if ( zoneID != null ) {
                    if ( zoneEntered.containsKey(p) ) {
                        e.setCancelled(true);
                        e.setDamage(0);
                    }
                }
            }
        }
    }

    /**
     * function to check if a location is in a zone
     * @param loc Location to check
     * @param extra Extra width of the zone square
     * @return string of the zoneID
     */
    private String getZone(Location loc, int extra ) {
        // zone protection only in the normal world
        if ( !loc.getWorld().getName().equals("world") ) return null;
        // get zones
        Set<String> zones = plugin.PlayerConfig.get().getConfigurationSection("Zone").getKeys(false);
        //TODO: loop through zones and check if the player is near or in one => then check if he is enemy => inform all friends
        for ( String zone : zones ) {
            // needed information
            String[] coordinates = zone.split(":");
            int x = Math.min(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[2])) - extra;
            int z = Math.min(Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[3])) - extra;
            int x2 = Math.max( Integer.parseInt( coordinates[0] ), Integer.parseInt( coordinates[2] ) ) + extra;
            int z2 = Math.max( Integer.parseInt( coordinates[1] ), Integer.parseInt( coordinates[3] ) ) + extra;
            // check if in position
            if ( x < loc.getX() && loc.getX() < x2 ) {
                if ( z < loc.getZ() && loc.getZ() < z2 ) {
                    return zone;
                }
            }
        }

        return null;
    }

    private void notifyFriends(String zoneID, String msg ) {
        // friend player
        Stream<String> friends = plugin.PlayerConfig.get().getConfigurationSection("Zone." + zoneID + ".friend").getKeys(false).stream();
        friends.forEach((friend) -> {
            String name = plugin.PlayerConfig.get().getString("Zone." + zoneID + ".friend." + friend);
            if ( Bukkit.getServer().getPlayer(name) != null ) {
                Player pTarget = Bukkit.getPlayer(name);
                pTarget.sendMessage(msg);
            }
        });
    }

    /**
     * give the player bad effects
      * @param p Player to get negative effects
     */
    private void harm(Player p) {
        // bad effects
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 1, false, false, true));
        p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 1, false, false, true));
        // red glowing
        p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 1, false, false, false));
    }

    /**
     * remove bad effects from the player
     * @param p Player to remove bad effects from
     */
    private void noHarm(Player p) {
        // bad effects
        p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
        p.removePotionEffect(PotionEffectType.WEAKNESS);
        // remove red glowing
        p.removePotionEffect(PotionEffectType.GLOWING);
    }

    /**
     * A function that checks if player of a certain zone are all offline => protection
     * @param zoneID String id of the Zone
     * @return bool when all offline true
     */
    private boolean friendsOffline(String zoneID) {
        Set<String> friends = plugin.PlayerConfig.get().getConfigurationSection("Zone." + zoneID + ".friend").getKeys(false);
        for ( String friend : friends ) {
            String name = plugin.PlayerConfig.get().getString("Zone." + zoneID + ".friend." + friend);
            if ( Bukkit.getServer().getPlayer(name) != null ) { return false; }
        }
        return true;
    }

}
