package me.willis.permissions.events;

import me.willis.permissions.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoin implements Listener {

    private Permissions plugin;

    public PlayerJoin(Permissions plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        //Group
        plugin.getPermissionsAPI().getSql().setGroup(player.getUniqueId(), plugin.getPermissionsAPI().getGroupManager().getDefaultGroup());

        plugin.getPermissionsAPI().getSql().getGroup(player.getUniqueId()).thenAccept(s ->{

            if (!plugin.getPermissionsAPI().getGroupManager().isGroupCreated(s)) {
                plugin.getPermissionsAPI().getSql().updateGroup(player.getUniqueId(), plugin.getPermissionsAPI().getGroupManager().getDefaultGroup());
            }

            plugin.getPermissionsAPI().getPlayerGroups().put(player.getUniqueId(), s.toLowerCase());
        });

        plugin.getPermissionsAPI().getGroupManager().applyGroupPermissions(player);


        //Player
        plugin.getPermissionsAPI().getPlayerManager().createAccount(player.getUniqueId());

        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getPermissionsAPI().getPlayerManager().applyPlayerPermissions(player);
            }
        }.runTaskLater(plugin, 2L);
    }
}
