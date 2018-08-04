package me.willis.permissions;

import me.justrayz.rlib.RHandler;
import me.willis.permissions.command.Command;
import me.willis.permissions.configuration.GConfig;
import me.willis.permissions.configuration.PConfig;
import me.willis.permissions.listeners.PlayerChat;
import me.willis.permissions.listeners.PlayerJoin;
import me.willis.permissions.listeners.PlayerQuit;
import me.willis.permissions.sql.SQL;
import me.willis.permissions.sql.SyncSQL;
import me.willis.permissions.util.GroupManager;
import me.willis.permissions.util.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Permissions extends JavaPlugin implements Listener {

    private Map<UUID, PermissionAttachment> attachment = new HashMap<UUID, PermissionAttachment>();
    private Map<UUID, String> group = new HashMap<UUID, String>();

    private SQL sql;
    private SyncSQL syncSQL;
    private GConfig gConfig;
    private PConfig pConfig;

    private GroupManager groupManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {

        //RLIB
        RHandler.registerHandler(this);

        //Configuration
        saveDefaultConfig();

        gConfig = new GConfig(this, "groups", "Groups");
        pConfig = new PConfig(this, "players", "Players");

        //SQL
        sql = new SQL(this);
        syncSQL = new SyncSQL(this);

        //Default Group
        groupManager = new GroupManager(this);
        groupManager.createGroup(groupManager.getDefaultGroup());

        playerManager = new PlayerManager(this);

        //Listeners
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerChat(this), this);
        pluginManager.registerEvents(new PlayerJoin(this), this);
        pluginManager.registerEvents(new PlayerQuit(this), this);

        //Command
        getCommand("p").setExecutor(new Command(this));

        //Reloads
        for (Player player : getServer().getOnlinePlayers()) {
            if (player != null) {

                //Add their group Back
                sql.getGroup(player.getUniqueId()).thenAccept(s -> {
                    group.put(player.getUniqueId(), s.toLowerCase());
                    groupManager.addGroupPermissions(player);
                });

                playerManager.addPlayerPermissions(player);
            }
        }

        //Sync Groups
        new BukkitRunnable() {
            @Override
            public void run() {
                syncSQL.syncGroups();

                for (Player player : getServer().getOnlinePlayers()) {
                    if (player != null) {
                        groupManager.updatePlayerGroupsGlobally(player);
                    }
                }
            }
        }.runTaskTimer(this, 0, getConfig().getInt("UpdateTime") * 20);
    }

    public Map<UUID, PermissionAttachment> getAttachment() { return attachment; }

    public Map<UUID, String> getGroup() { return group; }

    public SQL getSql() {
        return sql;
    }

    public SyncSQL getSyncSQL() { return syncSQL; }

    public GConfig getgConfig() {
        return gConfig;
    }

    public PConfig getpConfig() {
        return pConfig;
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
