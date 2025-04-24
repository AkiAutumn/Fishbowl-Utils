package annika.fishbowlUtils.features.buildingUtility;

import annika.fishbowlUtils.FishbowlUtils;
import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BuildingUtility {

    public static final Set<UUID> enabledPlayers = new HashSet<>();

    public static void playerInteractEvent(PlayerInteractEvent event) {

        if(!enabledPlayers.contains(event.getPlayer().getUniqueId())) return;

        if (!FishbowlUtils.instance.getConfig().getBoolean("allow-invalid-block-placement")) return;

        int spawnRadius = FishbowlUtils.instance.getConfig().getInt("spawn-protection-radius");
        Location spawnLocation = Bukkit.getWorld("world").getSpawnLocation();

        Block clickedBlock = event.getClickedBlock();

        if (clickedBlock == null) return;

        Location blockLocation = clickedBlock.getLocation();

        if (Math.abs(spawnLocation.x() - blockLocation.x()) <= spawnRadius && Math.abs(spawnLocation.y() - blockLocation.y()) <= spawnRadius && Math.abs(spawnLocation.z() - blockLocation.z()) <= spawnRadius) return;

        if(event.isBlockInHand() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            BlockFace blockFace = event.getBlockFace();

            int modX = blockFace.getModX();
            int modY = blockFace.getModY();
            int modZ = blockFace.getModZ();

            Location targetLocation = clickedBlock.getLocation().add(modX, modY, modZ);
            Block block = targetLocation.getBlock();
            Material originalType = block.getType();

            if (originalType != Material.AIR) return;

            Material newType = event.getItem().getType();

            Bukkit.getScheduler().runTaskLater(FishbowlUtils.instance, () -> {
                if (block.getType() == originalType) {

                    // Check if player is placing hanging sign
                    if (!newType.name().endsWith("_WALL_HANGING_SIGN") && newType.name().endsWith("_HANGING_SIGN")) {
                        // Set material to wall hanging sign
                        block.setType(Material.getMaterial(newType.name().replace("_HANGING_SIGN", "_WALL_HANGING_SIGN")));
                    } else {
                        // Set whatever the material is otherwise
                        block.setType(newType);
                    }

                    block.getWorld().playSound(block.getLocation(), Sound.BLOCK_STONE_PLACE, 1, 1);
                    new ParticleBuilder(Particle.GLOW)
                            .location(block.getLocation().add(0.5, 0.5, 0.5))
                            .count(10)
                            .extra(0)
                            .offset(0.25, 0.25, 0.25)
                            .spawn();

                    BlockData blockData = block.getBlockData();
                    Player player = event.getPlayer();

                    if (blockData instanceof Directional directional) {

                        BlockFace playerFacing = player.getFacing();

                        directional.setFacing(playerFacing);
                        block.setBlockData(directional);
                    }

                    ItemStack item = event.getItem();

                    if (item == null || item.getType() == Material.AIR || player.getGameMode() == GameMode.CREATIVE) return;

                    item.setAmount(item.getAmount() - 1);

                    if (event.getHand() == EquipmentSlot.HAND) {
                        player.getInventory().setItemInMainHand(item);
                    } else if (event.getHand() == EquipmentSlot.OFF_HAND) {
                        player.getInventory().setItemInOffHand(item);
                    }
                }
            }, 1L); // Wait 1 tick to see wether the block was placed successfully
        }
    }
}
