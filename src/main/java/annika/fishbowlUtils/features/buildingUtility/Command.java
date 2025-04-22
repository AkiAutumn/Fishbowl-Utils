package annika.fishbowlUtils.features.buildingUtility;

import annika.fishbowlUtils.FishbowlUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (sender instanceof Player player) {

            Plugin plugin = FishbowlUtils.getPlugin();

            if (!plugin.getConfig().getBoolean("allow-invalid-block-placement")) {
                sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:red:dark_red>This feature has been disabled by administrators</gradient>"));
                sender.playSound(Sound.sound(Key.key("minecraft:block.note_block.hat"), SoundCategory.NEUTRAL, 1f, 0f));
                return true;
            }

            UUID uuid = player.getUniqueId();

            if (BuildingUtility.enabledPlayers.contains(uuid)) {
                BuildingUtility.enabledPlayers.remove(uuid);
                sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:red:aqua>Disabled invalid block placement</gradient>"));
                sender.playSound(Sound.sound(Key.key("minecraft:block.note_block.hat"), SoundCategory.NEUTRAL, 1f, 1f));
            } else {
                BuildingUtility.enabledPlayers.add(uuid);
                sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:green:aqua>Enabled invalid block placement</gradient>"));
                sender.playSound(Sound.sound(Key.key("minecraft:block.note_block.hat"), SoundCategory.NEUTRAL, 1f, 1f));
            }
        }

        return true;
    }
}
