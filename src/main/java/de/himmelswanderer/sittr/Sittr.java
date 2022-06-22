package de.himmelswanderer.sittr;

import de.himmelswanderer.sittr.listeners.PlayerInteractListener;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class Sittr extends JavaPlugin {


    @Override
    public void onEnable() {
        //init logger
        Logger log = getLogger();
        //config startup
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        //register the PlayerJoinListener
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        //log that the plugin is enabled
        log.log(Level.INFO, "Sittr is enabled");
    }
}
