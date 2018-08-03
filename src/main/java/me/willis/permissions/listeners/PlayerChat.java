package me.willis.permissions.listeners;

import me.willis.permissions.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

    private Permissions plugin;

    public PlayerChat(Permissions plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();

        String format = plugin.getConfig().getString("ChatFormat");

        String world = player.getWorld().getName();

        String name = player.getName();
        String displayName = player.getDisplayName();

        String prefix = plugin.getGroupManager().getPrefix(player.getUniqueId());
        String suffix = plugin.getGroupManager().getSuffix(player.getUniqueId());

        String playerPrefix = plugin.getPlayerManager().getPrefix(player.getUniqueId());
        String playerSuffix = plugin.getPlayerManager().getSuffix(player.getUniqueId());

        String message = event.getMessage();

        if (isEmpty(playerPrefix)) {
            format = format.replace("(PREFIX)", prefix);
        } else {
            format = format.replace("(PREFIX)", playerPrefix);
        }

        if (isEmpty(playerSuffix)) {
            format = format.replace("(SUFFIX)", suffix);
        } else {
            format = format.replace("(SUFFIX)", playerSuffix);
        }

        format = format.replace("(WORLD)", world);
        format = format.replace("(PLAYER)", name);
        format = format.replace("(DISPLAYNAME)", displayName);
        format = format.replace("(MESSAGE)", message.replace("%", "%%"));
        format = ChatColor.translateAlternateColorCodes('&', format);
        event.setFormat(format);
    }

    private boolean isEmpty(String input) {
        return input == null || input.isEmpty();
    }
}