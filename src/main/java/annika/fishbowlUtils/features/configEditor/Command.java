package annika.fishbowlUtils.features.configEditor;

import annika.fishbowlUtils.FishbowlUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if(!sender.hasPermission("fishbowl-utils.fixPlayer")) {
            sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:red:dark_red>No permission - leck Eier!</gradient>"));
            sender.playSound(Sound.sound(Key.key("minecraft:block.note_block.hat"), SoundCategory.NEUTRAL, 1f, 0f));
            return true;
        }

        if (args.length < 2) {
            sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:red:dark_red>Usage: /configset <key> <value></gradient>"));
            sender.playSound(Sound.sound(Key.key("minecraft:block.note_block.hat"), SoundCategory.NEUTRAL, 1f, 0f));
            return true;
        }

        String key = args[0];
        String value = args[1];
        String type = args[2].toLowerCase();

        FileConfiguration config = FishbowlUtils.instance.getConfig();

        // Example: If you want to set a string value in the config
        if (config.contains(key)) {
            switch (type) {
                case "string":
                    config.set(key, value);
                    break;
                case "int":
                    try {
                        config.set(key, Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:red:dark_red>Invalid integer value</gradient>"));
                        sender.playSound(Sound.sound(Key.key("minecraft:block.note_block.hat"), SoundCategory.NEUTRAL, 1f, 0f));
                        return true;
                    }
                    break;
                case "float":
                    try {
                        config.set(key, Float.parseFloat(value));
                    } catch (NumberFormatException e) {
                        sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:red:dark_red>Invalid float value</gradient>"));
                        sender.playSound(Sound.sound(Key.key("minecraft:block.note_block.hat"), SoundCategory.NEUTRAL, 1f, 0f));
                        return true;
                    }
                    break;
                case "double":
                    try {
                        config.set(key, Double.parseDouble(value));
                    } catch (NumberFormatException e) {
                        sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:red:dark_red>Invalid double value</gradient>"));
                        sender.playSound(Sound.sound(Key.key("minecraft:block.note_block.hat"), SoundCategory.NEUTRAL, 1f, 0f));
                        return true;
                    }
                    break;
                case "boolean":
                    if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                        config.set(key, Boolean.parseBoolean(value));
                    } else {
                        sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:red:dark_red>Invalid boolean value</gradient>"));
                        sender.playSound(Sound.sound(Key.key("minecraft:block.note_block.hat"), SoundCategory.NEUTRAL, 1f, 0f));
                        return true;
                    }
                    break;
                default:
                    sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:red:dark_red>Unknown type. Valid types are: string, int, float, double, boolean.</gradient>"));
                    sender.playSound(Sound.sound(Key.key("minecraft:block.note_block.hat"), SoundCategory.NEUTRAL, 1f, 0f));
                    return true;
            }

            FishbowlUtils.instance.saveConfig();  // Save the changes immediately
            sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:aqua:green>Config value updated</gradient>"));
            sender.playSound(Sound.sound(Key.key("minecraft:block.note_block.hat"), SoundCategory.NEUTRAL, 1f, 1f));
        } else {
            sender.sendActionBar(MiniMessage.miniMessage().deserialize("<gradient:red:dark_red>Config key not found</gradient>"));
            sender.playSound(Sound.sound(Key.key("minecraft:block.note_block.hat"), SoundCategory.NEUTRAL, 1f, 0f));
        }

        return true;
    }
}
