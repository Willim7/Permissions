package me.willis.permissions.events;

import me.willis.permissions.Permissions;
import me.willis.permissions.util.ArgumentFormatter;
import me.willis.permissionsAPI.PermissionsAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerChat implements Listener {

    private Permissions plugin;

    public PlayerChat(Permissions plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();

        if (plugin.getPermissionsAPI().getPlayerGroups().containsKey(player.getUniqueId())) {

            String chatFormat = plugin.getConfig().getString("ChatFormat");

            Map<String, Object> key = new HashMap<>();

            key.put("WORLD", player.getWorld().getName());
            key.put("PLAYER", player.getName());
            key.put("DISPLAYNAME", player.getDisplayName());

            if (plugin.getPermissionsAPI().getPlayerManager().getPlayerPrefix(player.getUniqueId()).equalsIgnoreCase("")) {
                key.put("PREFIX", plugin.getPermissionsAPI().getGroupManager().getPrefix(player.getUniqueId()));
            } else {
                key.put("PREFIX", plugin.getPermissionsAPI().getPlayerManager().getPlayerSuffix(player.getUniqueId()));
            }

            if (plugin.getPermissionsAPI().getPlayerManager().getPlayerSuffix(player.getUniqueId()).equalsIgnoreCase("")) {
                key.put("SUFFIX", plugin.getPermissionsAPI().getGroupManager().getSuffix(player.getUniqueId()));
            } else {
                key.put("SUFFIX", plugin.getPermissionsAPI().getPlayerManager().getPlayerSuffix(player.getUniqueId()));
            }

            key.put("MESSAGE", event.getMessage());

            ArgumentFormatter argumentFormatter = new ArgumentFormatter(chatFormat, key);

            event.setFormat(argumentFormatter.format());
        }
    }
}
