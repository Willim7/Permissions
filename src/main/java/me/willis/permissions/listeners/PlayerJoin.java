package me.willis.permissions.listeners;

import me.willis.permissions.Permissions;
import me.willis.permissions.util.GroupManager;
import me.willis.permissions.util.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private Permissions plugin;
    private GroupManager groupManager;
    private PlayerManager playerManager;

    public PlayerJoin(Permissions plugin) {
        this.plugin = plugin;
        this.groupManager = new GroupManager(plugin);
        this.playerManager = new PlayerManager(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        //Group
        plugin.getSqlConfig().setGroup(player.getUniqueId(), groupManager.getDefaultGroup());
        plugin.getGroup().put(player.getUniqueId(), plugin.getSqlConfig().getGroup(player.getUniqueId()));
        groupManager.addGroupPermissions(player);

        //Player
        playerManager.createPlayerInfo(player.getUniqueId());
        playerManager.addPlayerPermissions(player);
    }
}
