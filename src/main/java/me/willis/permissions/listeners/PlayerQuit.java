package me.willis.permissions.listeners;

import me.willis.permissions.Permissions;
import me.willis.permissions.util.GroupManager;
import me.willis.permissions.util.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    private Permissions plugin;
    private GroupManager groupManager;
    private PlayerManager playerManager;

    public PlayerQuit(Permissions plugin) {
        this.plugin = plugin;
        this.groupManager = new GroupManager(plugin);
        this.playerManager = new PlayerManager(plugin);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        //Group
        groupManager.removeGroupPermissions(player);

        //Player
        playerManager.removePlayerPermissions(player);

        //Attachment
        plugin.clear(player.getUniqueId());
    }

}
