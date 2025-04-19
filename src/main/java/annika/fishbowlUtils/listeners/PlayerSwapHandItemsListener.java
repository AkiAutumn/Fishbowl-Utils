package annika.fishbowlUtils.listeners;

import annika.fishbowlUtils.features.elytraTakeoff.elytraTakeoff;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerSwapHandItemsListener implements Listener {

    @EventHandler
    public void playerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        elytraTakeoff.playerSwapHandItemsEvent(event);
    }
}
