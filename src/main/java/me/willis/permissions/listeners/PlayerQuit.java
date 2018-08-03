package me.willis.permissions.listeners;

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

        //Group
        plugin.getGroupManager().removeGroupPermissions(player);

        //Player
        plugin.getPlayerManager().removePlayerPermissions(player);

        //Attachment/Group
        plugin.getGroup().remove(player.getUniqueId());
        plugin.getAttachment().remove(player.getUniqueId());
    }
}
