package me.willis.permissions;

import me.willis.permissions.configuration.PConfig;
import me.willis.permissions.configuration.SConfig;
import me.willis.permissions.configuration.SQLConfig;
import me.willis.permissions.listeners.PlayerChat;
import me.willis.permissions.listeners.PlayerJoin;
import me.willis.permissions.listeners.PlayerQuit;
import me.willis.permissions.util.GroupManager;
import org.bukkit.event.Listener;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Permissions extends JavaPlugin implements Listener {

    private Map<UUID, PermissionAttachment> attachment = new HashMap<UUID, PermissionAttachment>();

    private SConfig sConfig;
    private PConfig pConfig;
    private SQLConfig sqlConfig;

    @Override
    public void onEnable() {

        //Configuration
        saveDefaultConfig();

        sqlConfig = new SQLConfig(this);
        sqlConfig.connect();
        sqlConfig.createTable();

        sConfig = new SConfig();
        sConfig.createSConfig(this);

        pConfig = new PConfig();
        pConfig.createPConfig(this);

        //Default Group
        GroupManager groupManager = new GroupManager(this);
        groupManager.createGroup(groupManager.getDefaultGroup());

        //Listeners
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerChat(this), this);
        pluginManager.registerEvents(new PlayerJoin(this), this);
        pluginManager.registerEvents(new PlayerQuit(this), this);
    }

    @Override
    public void onDisable() { sqlConfig.disconnect(); }

    public Map<UUID, PermissionAttachment> getAttachment() { return attachment; }

    public SConfig getsConfig() {
        return sConfig;
    }

    public PConfig getpConfig() { return pConfig; }

    public SQLConfig getSqlConfig() {
        return sqlConfig;
    }

    public void clear(UUID uuid) {
        if (attachment.containsKey(uuid)) {
            attachment.remove(uuid);
        }
    }
}
