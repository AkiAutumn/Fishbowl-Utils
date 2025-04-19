package annika.fishbowlUtils;

import annika.fishbowlUtils.features.configEditor.configEditor;
import annika.fishbowlUtils.features.elytraTakeoff.fixPlayerCommand;
import annika.fishbowlUtils.listeners.EntityToggleGlideListener;
import annika.fishbowlUtils.listeners.PlayerSwapHandItemsListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class FishbowlUtils extends JavaPlugin {

    private static boolean grimAcEnabled;
    private static boolean ncpEnabled;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        checkAntiCheatPlugins();
        listenerRegistration();
        commandRegistration();
    }

    private void checkAntiCheatPlugins() {
        grimAcEnabled = Bukkit.getPluginManager().getPlugin("GrimAC") != null;
        ncpEnabled = Bukkit.getPluginManager().getPlugin("NoCheatPlus") != null;

        getLogger().info("GrimAC detected: " + grimAcEnabled);
        getLogger().info("NoCheatPlus detected: " + ncpEnabled);
    }

    public static void exemptFromAntiCheat(Player player) {
        if (grimAcEnabled) {
            try {
                Class<?> grimAPI = Class.forName("ac.grim.grimac.GrimAPI");
                Object grimInstance = grimAPI.getMethod("getInstance").invoke(null);
                Object playerData = grimAPI.getMethod("getPlayerData", Player.class).invoke(grimInstance, player);
                playerData.getClass().getField("bypassAnticheat").set(playerData, true);
            } catch (Exception e) {
                Bukkit.getLogger().warning("Failed to exempt from GrimAC: " + e.getMessage());
            }
        }

        if (ncpEnabled) {
            try {
                Class<?> ncpExempt = Class.forName("fr.neatmonster.nocheatplus.hooks.NCPExemptionManager");
                Object checkType = Class.forName("fr.neatmonster.nocheatplus.checks.CheckType")
                        .getField("MOVING_SURVIVALFLY").get(null);
                ncpExempt.getMethod("exemptPermanently", Player.class, checkType.getClass())
                        .invoke(null, player, checkType);
            } catch (Exception e) {
                Bukkit.getLogger().warning("Failed to exempt from NoCheatPlus: " + e.getMessage());
            }
        }
    }

    public static void unexemptFromAntiCheat(Player player) {
        if (grimAcEnabled) {
            try {
                Class<?> grimAPI = Class.forName("ac.grim.grimac.GrimAPI");
                Object grimInstance = grimAPI.getMethod("getInstance").invoke(null);
                Object playerData = grimAPI.getMethod("getPlayerData", Player.class).invoke(grimInstance, player);
                playerData.getClass().getField("bypassAnticheat").set(playerData, false);
            } catch (Exception e) {
                Bukkit.getLogger().warning("Failed to unexempt from GrimAC: " + e.getMessage());
            }
        }

        if (ncpEnabled) {
            try {
                Class<?> ncpExempt = Class.forName("fr.neatmonster.nocheatplus.hooks.NCPExemptionManager");
                Object checkType = Class.forName("fr.neatmonster.nocheatplus.checks.CheckType")
                        .getField("MOVING_SURVIVALFLY").get(null);
                ncpExempt.getMethod("unexempt", Player.class, checkType.getClass())
                        .invoke(null, player, checkType);
            } catch (Exception e) {
                Bukkit.getLogger().warning("Failed to unexempt from NoCheatPlus: " + e.getMessage());
            }
        }
    }

    private void listenerRegistration() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerSwapHandItemsListener(), this);
        pluginManager.registerEvents(new EntityToggleGlideListener(), this);
    }

    private void commandRegistration() {
        getCommand("fixPlayer").setExecutor(new fixPlayerCommand());
        getCommand("config-edit").setExecutor(new configEditor());
    }

    public static Plugin getPlugin() {
        return FishbowlUtils.getProvidingPlugin(FishbowlUtils.class);
    }
}