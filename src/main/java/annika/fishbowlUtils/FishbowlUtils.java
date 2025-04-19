package annika.fishbowlUtils;

import annika.fishbowlUtils.listeners.EntityToggleGlideListener;
import annika.fishbowlUtils.listeners.PlayerSwapHandItemsListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class FishbowlUtils extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        listenerRegistration();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void listenerRegistration() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerSwapHandItemsListener(), this);
        pluginManager.registerEvents(new EntityToggleGlideListener(), this);
    }
}
