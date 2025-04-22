package annika.fishbowlUtils.listeners;

import annika.fishbowlUtils.features.buildingUtility.BuildingUtility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public static void playerInteractEvent(PlayerInteractEvent event) {
        BuildingUtility.playerInteractEvent(event);
    }
}
