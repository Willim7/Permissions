package me.willis.permissions;

import me.willis.permissions.command.Command;
import me.willis.permissions.events.PlayerChat;
import me.willis.permissions.events.PlayerJoin;
import me.willis.permissions.events.PlayerQuit;
import me.willis.permissionsAPI.PermissionsAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Permissions extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {

        saveDefaultConfig();

        PermissionsAPI.registerAPI(this);

        PermissionsAPI permissionsAPI = PermissionsAPI.getPermissionsAPI();

        permissionsAPI.getGroupManager().createGroup(permissionsAPI.getGroupManager().getDefaultGroup());

        Bukkit.getOnlinePlayers().forEach(player -> {
            permissionsAPI.getSql().getGroup(player.getUniqueId()).thenAccept(s -> {
                permissionsAPI.getPlayerGroups().put(player.getUniqueId(), s.toLowerCase());
                permissionsAPI.getGroupManager().applyGroupPermissions(player);
            });

            new BukkitRunnable() {
                @Override
                public void run() {
                    permissionsAPI.getPlayerManager().applyPlayerPermissions(player);
                }
            }.runTaskLater(this, 2L);
        });

        new BukkitRunnable() {

            public void run() {

                permissionsAPI.getSyncSQL().syncGroups();

                Bukkit.getOnlinePlayers().forEach(player -> {

                    if (player != null) {
                        permissionsAPI.getGroupManager().groupDeletionUpdater(player);
                    }
                });
            }
        }.runTaskTimer(this, 0, getConfig().getInt("UpdateTime") * 20);

        new BukkitRunnable() {

            public void run() {

                Bukkit.getOnlinePlayers().forEach(player -> {

                    if (player != null) {
                        permissionsAPI.getGroupManager().groupChangeUpdater(player);
                    }
                });
            }
        }.runTaskTimer(this, 0, getConfig().getInt("UpdateTime") * 20);

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerChat(this), this);
        pluginManager.registerEvents(new PlayerJoin(this), this);
        pluginManager.registerEvents(new PlayerQuit(this), this);

        getCommand("p").setExecutor(new Command(this));
    }

    public PermissionsAPI getPermissionsAPI() {
        return PermissionsAPI.getPermissionsAPI();
    }
}

