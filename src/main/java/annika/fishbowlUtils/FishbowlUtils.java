package annika.fishbowlUtils;

import annika.fishbowlUtils.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class FishbowlUtils extends JavaPlugin {

    public static FishbowlUtils instance;

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();
        listenerRegistration();
        commandRegistration();
    }

    private void listenerRegistration() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerSwapHandItemsListener(), this);
        pluginManager.registerEvents(new EntityToggleGlideListener(), this);
        pluginManager.registerEvents(new PlayerInteractListener(), this);
    }

    private void commandRegistration() {
        getCommand("fixPlayer").setExecutor(new annika.fishbowlUtils.features.elytraTakeoff.Command());
        getCommand("config-edit").setExecutor(new annika.fishbowlUtils.features.configEditor.Command());
        getCommand("toggle-invalid-placement").setExecutor(new annika.fishbowlUtils.features.buildingUtility.Command());
    }
}