package annika.fishbowlUtils.features.elytraTakeoff;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static annika.fishbowlUtils.features.elytraTakeoff.elytraTakeoff.glidingPlayersUUIDs;

public class fixPlayerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("fishbowl-utils.fixPlayer")) {
            sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:red:dark_red>No permission - leck Eier!</gradient>"));
            return true;
        }

        if(args.length != 1) {
            sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:red:dark_red>Invalid argument length</gradient>"));
            return true;
        }

        Player player = sender.getServer().getPlayer(args[0]);

        if(player == null) {
            sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:red:dark_red>Player not found</gradient>"));
            return true;
        }

        player.setGliding(false);
        glidingPlayersUUIDs.remove(player.getUniqueId());
        sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:light_purple:dark_purple>Fixed " + player.getName() + "</gradient>"));

        return true;
    }
}
