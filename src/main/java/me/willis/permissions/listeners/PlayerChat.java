package me.willis.permissions.listeners;

import me.willis.permissions.Permissions;
import me.willis.permissions.util.GroupManager;
import me.willis.permissions.util.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

    private Permissions plugin;
    private GroupManager groupManager;
    private PlayerManager playerManager;

    public PlayerChat(Permissions plugin) {
        this.plugin = plugin;
        this.groupManager = new GroupManager(plugin);
        this.playerManager = new PlayerManager(plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();

        String format = plugin.getConfig().getString("ChatFormat");

        String world = player.getWorld().getName();

        String name = player.getName();
        String displayName = player.getDisplayName();

        String prefix = groupManager.getPrefix(player.getUniqueId());
        String suffix = groupManager.getSuffix(player.getUniqueId());

        String playerPrefix = playerManager.getPrefix(player.getUniqueId());
        String playerSuffix = playerManager.getSuffix(player.getUniqueId());

        String message = event.getMessage();

        if (isEmpty(playerPrefix)) {
            format = format.replace("%prefix%", prefix);
        } else {
            format = format.replace("%prefix%", playerPrefix);
        }

        if (isEmpty(playerSuffix)) {
            format = format.replace("%suffix%", suffix);
        } else {
            format = format.replace("%suffix%", playerSuffix);
        }

        format = format.replace("%world%", world);
        format = format.replace("%player%", name);
        format = format.replace("%displayname%", displayName);
        format = ChatColor.translateAlternateColorCodes('&', format);
        format = format.replace("%message%", message);
        format = format.trim().replaceAll("\\s+", " ");
        event.setFormat(format);
    }

    private boolean isEmpty(String input) {
        return input == null || input.isEmpty();
    }
}