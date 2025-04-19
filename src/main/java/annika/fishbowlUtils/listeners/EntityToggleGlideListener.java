package annika.fishbowlUtils.listeners;

import annika.fishbowlUtils.features.elytraTakeoff.elytraTakeoff;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class EntityToggleGlideListener implements Listener {

    @EventHandler
    public void entityToggleGlideEvent(EntityToggleGlideEvent event) {
        elytraTakeoff.entityToggleGlideEvent(event);
    }
}
