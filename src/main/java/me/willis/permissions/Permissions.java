package me.willis.permissions;

import me.justrayz.rlib.RHandler;
import me.willis.permissions.command.Command;
import me.willis.permissions.listeners.PlayerChat;
import me.willis.permissions.listeners.PlayerJoin;
import me.willis.permissions.listeners.PlayerQuit;
import me.willis.permissions.sql.SQL;
import me.willis.permissions.util.GroupManager;
import me.willis.permissions.util.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Permissions extends JavaPlugin implements Listener {

    private Map<UUID, PermissionAttachment> attachment = new HashMap<UUID, PermissionAttachment>();
    private Map<UUID, String> group = new HashMap<UUID, String>();

    private GroupManager groupManager;
    private PlayerManager playerManager;
    private SQL sql;

    @Override
    public void onEnable() {

        //RLIB
        RHandler.registerHandler(this);

        //Configuration
        saveDefaultConfig();

        //SQL
        sql = new SQL(this);

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
    }

    public Map<UUID, PermissionAttachment> getAttachment() { return attachment; }

    public Map<UUID, String> getGroup() { return group; }

    public SQL getSql() {
        return sql;
    }
}
