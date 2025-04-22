package annika.fishbowlUtils.features.elytraTakeoff;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static annika.fishbowlUtils.features.elytraTakeoff.elytraTakeoff.glidingPlayersUUIDs;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if(!sender.hasPermission("fishbowl-utils.fixPlayer")) {
            sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:red:dark_red>No permission - leck Eier!</gradient>"));
            sender.playSound(Sound.sound(Key.key("minecraft:block.note_block.hat"), SoundCategory.NEUTRAL, 1f, 0f));
            return true;
        }

        if(args.length != 1) {
            sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:red:dark_red>Invalid argument length</gradient>"));
            sender.playSound(Sound.sound(Key.key("minecraft:block.note_block.hat"), SoundCategory.NEUTRAL, 1f, 0f));
            return true;
        }

        Player player = sender.getServer().getPlayer(args[0]);

        if(player == null) {
            sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:red:dark_red>Player not found</gradient>"));
            sender.playSound(Sound.sound(Key.key("minecraft:block.note_block.hat"), SoundCategory.NEUTRAL, 1f, 0f));
            return true;
        }

        player.setGliding(false);
        glidingPlayersUUIDs.remove(player.getUniqueId());

        sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:light_purple:blue>Fixed " + player.getName() + "</gradient>"));
        sender.playSound(Sound.sound(Key.key("minecraft:block.note_block.hat"), SoundCategory.NEUTRAL, 1f, 1f));

        return true;
    }
}
