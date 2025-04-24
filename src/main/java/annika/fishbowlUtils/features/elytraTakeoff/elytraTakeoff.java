package annika.fishbowlUtils.features.elytraTakeoff;

import annika.fishbowlUtils.FishbowlUtils;
import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class elytraTakeoff {

    public static final Set<UUID> glidingPlayersUUIDs = new HashSet<>();

    public static void playerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {

        boolean elytraTakeoffEnabled = FishbowlUtils.instance.getConfig().getBoolean("elytra-takeoff-enabled");

        if (!elytraTakeoffEnabled) {
            return;
        }

        int spawnRadius = FishbowlUtils.instance.getConfig().getInt("elytra-takeoff-radius");
        String worldName = FishbowlUtils.instance.getConfig().getString("elytra-takeoff-world");

        if (worldName == null) {
            return;
        }

        World world = FishbowlUtils.instance.getServer().getWorld(worldName);
        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();

        if (playerLocation.getWorld() != world) {
            return;
        }

        boolean checkBlocksAbovePlayer = FishbowlUtils.instance.getConfig().getBoolean("elytra-takeoff-check-blocks-above-player");

        if (checkBlocksAbovePlayer && !checkBlocksAbovePlayer(player)) {
            return;
        }

        Vector velocity = new Vector(0, 2.5, 0);
        Location spawnLocation = player.getWorld().getSpawnLocation();

        if(!glidingPlayersUUIDs.contains(player.getUniqueId())) {
            if (Math.abs(spawnLocation.x() - playerLocation.x()) <= spawnRadius &&
                    Math.abs(spawnLocation.y() - playerLocation.y()) <= spawnRadius &&
                    Math.abs(spawnLocation.z() - playerLocation.z()) <= spawnRadius) {

                event.setCancelled(true);

                glidingPlayersUUIDs.add(player.getUniqueId());

                new ParticleBuilder(Particle.CLOUD)
                        .location(playerLocation.add(0, .1, 0))
                        .count(20)
                        .extra(.05)
                        .offset(.5, .2, .5)
                        .spawn();

                player.getWorld().playSound(playerLocation, Sound.ENTITY_PARROT_FLY, 1, 1);
                player.setVelocity(velocity);

                Bukkit.getScheduler().runTaskLater(FishbowlUtils.instance, () -> {
                    event.getPlayer().setGliding(true);
                }, 10L);
            }
        }
    }

    public static void entityToggleGlideEvent(EntityToggleGlideEvent event) {

        Entity entity = event.getEntity();

        if (entity instanceof Player player && glidingPlayersUUIDs.contains(player.getUniqueId())) {

            ItemStack chestplate = player.getInventory().getChestplate();

            if ((chestplate == null || chestplate.getType() != Material.ELYTRA) && !player.isOnGround()) {
                event.setCancelled(true);
            } else {
                glidingPlayersUUIDs.remove(player.getUniqueId());
            }
        }
    }

    public static boolean checkBlocksAbovePlayer(Player player) {

        Location location = player.getLocation();
        World world = player.getWorld();

        int startY = location.getBlockY() + 1; // Start checking above the player's head
        int maxY = world.getMaxHeight();

        for (int y = startY; y < maxY; y++) {
            Material type = world.getBlockAt(location.getBlockX(), y, location.getBlockZ()).getType();
            if (!type.isAir()) {
                return false;
            }
        }
        return true;
    }
}
