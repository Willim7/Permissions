package me.willis.permissions.listeners;

import me.willis.permissions.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private Permissions plugin;

    public PlayerJoin(Permissions plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        //Group
        plugin.getSql().setGroup(player.getUniqueId(), plugin.getGroupManager().getDefaultGroup());

        plugin.getSql().getGroup(player.getUniqueId()).thenAccept(s -> {
            if (!plugin.getGroupManager().isGroupCreated(s)) {
                plugin.getSql().updateGroup(player.getUniqueId(), plugin.getGroupManager().getDefaultGroup());
            }
            plugin.getGroup().put(player.getUniqueId(), s.toLowerCase());
            plugin.getGroupManager().addGroupPermissions(player); });

        //Player
        plugin.getPlayerManager().createPlayerInfo(player.getUniqueId());
        plugin.getPlayerManager().addPlayerPermissions(player);
    }
}