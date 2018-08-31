package me.willis.permissions.events;

import me.willis.permissions.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    private Permissions plugin;

    public PlayerQuit(Permissions plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        //Permissions
        plugin.getPermissionsAPI().getPermissionsManager().unApplyPermissions(player);

        //Maps
        plugin.getPermissionsAPI().getPlayerGroups().remove(player.getUniqueId());
        plugin.getPermissionsAPI().getAttachment().remove(player.getUniqueId());
    }
}