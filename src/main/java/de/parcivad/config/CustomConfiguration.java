package de.parcivad.config;

import de.parcivad.main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

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

}