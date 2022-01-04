package de.parcivad;

import de.parcivad.command.*;
import de.parcivad.listener.*;
import de.parcivad.tools.tpsTracker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import de.parcivad.config.CustomConfiguration;
import de.parcivad.tools.color;

import java.util.*;
import java.util.logging.Logger;

public class main extends JavaPlugin {

    // Plugin Prefix
    public static final String prefix = "§7§opa§r " +  color.seperator + "| " + color.normal;
    public static final String prefixDeny = "§6⚠§r " +  color.seperator + "| " + color.important;

    // Define Custom configurations Player and Server -Config
    public CustomConfiguration PlayerConfig;
    public CustomConfiguration ServerConfig;

    // Define plugin scoreboard and team
    public static Scoreboard sb = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
    // Hashmaps
    public static HashMap<Player, Player> tpaPlayer = new HashMap<Player, Player>();
    public static HashMap<Player, Long> homeWait = new HashMap<Player, Long>();
    public static HashMap<Player, String> zoneEntered = new HashMap<Player, String>();
    public static HashMap<Player, String> nearZone = new HashMap<Player, String>();
    public static HashMap<Player, Set<Player>> pChat = new HashMap<Player, Set<Player>>();

    // ANSI Colors for Console
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    // plugin logger
    public static Logger log = Bukkit.getLogger();

    public void onEnable() {

        // Server Scheduler
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new tpsTracker(), 100L, 100L);

        // Team Register
        sb.registerNewTeam("00000Owner").setPrefix("§4Owner §8| §7");
        sb.getTeam("00000Owner").setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        sb.registerNewTeam("00001Mod").setPrefix("§cMod §8| §7");
        sb.getTeam("00001Mod").setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        sb.registerNewTeam("00002Dev").setPrefix("§bDev §8| §7");
        sb.getTeam("00002Dev").setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        sb.registerNewTeam("00008Player").setPrefix("§7Player §8| §7");
        sb.getTeam("00008Player").setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);

        // Reload and save Config files
        reloadConfig();
        getConfig().options().copyDefaults();
        saveConfig();

        // Player Config for Player Data like positions
        PlayerConfig = new CustomConfiguration("PlayerConfig", this );
        PlayerConfig.reload();
        PlayerConfig.save();

        // Server Config for general settings
        ServerConfig = new CustomConfiguration( "ServerConfig", this );
        ServerConfig.reload();
        ServerConfig.save();

        // Register Events
        Bukkit.getPluginManager().registerEvents(new onJoin(), this);
        Bukkit.getPluginManager().registerEvents(new onQuit( this), this);
        Bukkit.getPluginManager().registerEvents(new onLogin(this), this);
        Bukkit.getPluginManager().registerEvents(new onServerListPing(this), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerDeath(this),this);
        Bukkit.getPluginManager().registerEvents(new onChat(), this);
        Bukkit.getPluginManager().registerEvents(new ZoneListener(this), this);

        // try/catch to catch null
        try {
            Objects.requireNonNull(getCommand("tpa")).setExecutor(new tpa(this));
            Objects.requireNonNull(getCommand("lock")).setExecutor(new lock(this));
            Objects.requireNonNull(getCommand("pos")).setExecutor(new pos(this));
            Objects.requireNonNull(getCommand("ec")).setExecutor(new ec(this));
            Objects.requireNonNull(getCommand("inv")).setExecutor(new inv(this));
            Objects.requireNonNull(getCommand("spawn")).setExecutor(new spawn(this));
            Objects.requireNonNull(getCommand("stats")).setExecutor(new stats(this));
            Objects.requireNonNull(getCommand("seed")).setExecutor(new seed(this));
            Objects.requireNonNull(getCommand("tempban")).setExecutor(new tempban(this));
            Objects.requireNonNull(getCommand("tempunban")).setExecutor(new tempunban(this));
            Objects.requireNonNull(getCommand("commands")).setExecutor(new commands(this));
            Objects.requireNonNull(getCommand("home")).setExecutor(new home(this));
            Objects.requireNonNull(getCommand("zone")).setExecutor(new zone(this));
            Objects.requireNonNull(getCommand("performance")).setExecutor(new performance(this));
            Objects.requireNonNull(getCommand("chat")).setExecutor(new chat(this));
        } catch(NullPointerException exception) {
            exception.printStackTrace();
            messageOps("§c§lPLUGIN ERROR: " + Arrays.toString(exception.getStackTrace()));
        }
    }

    // Saves Config
    public void onDisable() {
        ServerConfig.save();
        PlayerConfig.save();
    }

    public void messageOps(String message) {
        Bukkit.getOperators().forEach(op -> {
            if(op.isOnline()) {
                ((Player) op).sendMessage(message);
            }
        });
    }
}
