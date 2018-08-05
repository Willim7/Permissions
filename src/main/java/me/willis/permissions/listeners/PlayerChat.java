package me.willis.permissions.listeners;

import me.justrayz.rlib.chat.ArgumentFormatter;
import me.willis.permissions.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerChat implements Listener {

    private Permissions plugin;

    public PlayerChat(Permissions plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();

        if (plugin.getGroup().containsKey(player.getUniqueId())) {

            String chatFormat = plugin.getConfig().getString("ChatFormat");

            Map<String, Object> keys = new HashMap<>();
            keys.put("WORLD", player.getWorld().getName());
            keys.put("PLAYER", player.getName());
            keys.put("DISPLAYNAME", player.getDisplayName());

            if (plugin.getPlayerManager().getPrefix(player.getUniqueId()).equalsIgnoreCase("")) {
                keys.put("PREFIX", plugin.getGroupManager().getPrefix(player.getUniqueId()));
            } else {
                keys.put("PREFIX", plugin.getPlayerManager().getPrefix(player.getUniqueId()));
            }

            if (plugin.getPlayerManager().getSuffix(player.getUniqueId()).equalsIgnoreCase("")) {
                keys.put("SUFFIX", plugin.getGroupManager().getSuffix(player.getUniqueId()));
            } else {
                keys.put("SUFFIX", plugin.getPlayerManager().getSuffix(player.getUniqueId()));
            }

            keys.put("MESSAGE", event.getMessage());

            ArgumentFormatter argumentFormatter = new ArgumentFormatter(chatFormat, keys);

            event.setFormat(ChatColor.translateAlternateColorCodes('&', argumentFormatter.format()));
        }
    }
}
