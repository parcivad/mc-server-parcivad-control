package de.parcivad.config;

import de.parcivad.main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getLogger;

public class CustomConfiguration {

    private final File file;
    private FileConfiguration customFile;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public CustomConfiguration(String fileName, main plugin) {

        file = new File(plugin.getDataFolder(), fileName + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        this.customFile = YamlConfiguration.loadConfiguration(this.file);
    }

    public FileConfiguration get() {
        return this.customFile;
    }

    public void save() {
        try {
            this.customFile.save(this.file);
        } catch (IOException e) {
            System.out.println("Couldn't save file");
        }
    }

    public void reload() {
        this.customFile = YamlConfiguration.loadConfiguration(this.file);
    }

    private void copyDefaultConfig(InputStream input, File actual, String name) {
        try (FileOutputStream output = new FileOutputStream(actual)) {
            byte[] buf = new byte[8192];
            int length;
            while ((length = input.read(buf)) > 0) {
                output.write(buf, 0, length);
            }

            getLogger().info("Default configuration file written: " + name);
        } catch (IOException ex) {
            getLogger().log(Level.WARNING, "Failed to write default config file", ex);
        }
    }
}