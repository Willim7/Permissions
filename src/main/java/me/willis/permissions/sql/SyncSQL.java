package me.willis.permissions.sql;

import me.justrayz.rlib.datamanagement.database.DatabaseType;
import me.justrayz.rlib.datamanagement.database.RDatabaseManager;
import me.willis.permissions.Permissions;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SyncSQL extends RDatabaseManager {

    private Permissions plugin;

    public SyncSQL(Permissions plugin) {
        super(plugin.getConfig().getString("Host") + ":" + plugin.getConfig().getString("Port") + "/" + plugin.getConfig().getString("Database"), DatabaseType.MYSQL, plugin.getConfig().getString("Username"), plugin.getConfig().getString("Password"));
        this.plugin = plugin;
    }

    @Override
    public void generate() {
        boolean success = this.query("CREATE TABLE IF NOT EXISTS groups (RANKS VARCHAR(100))");
    }

    private CompletableFuture<Boolean> hasGroup(String group) {
        return this.queryResultAsync("SELECT * FROM groups WHERE RANKS = ?", group).thenApply((rows) -> {
            return !rows.isEmpty();
        });
    }

    public void syncGroups() {
        this.queryResultAsync("SELECT * FROM groups").thenAccept(rows -> {
            for (Map<String, Object> row : rows) {

                if (!(plugin.getGroupManager().isGroupCreated((String) row.get("RANKS")))) {
                    plugin.getGroupManager().createGroup(((String) row.get("RANKS")).toLowerCase());
                }

                for (String groups : plugin.getgConfig().getConfig().getConfigurationSection("Groups").getKeys(false)) {
                    hasGroup(groups).thenAccept(success -> {
                        if (!success) {
                            plugin.getGroupManager().deleteGroup(groups);
                        }
                    });
                }
            }
        });
    }

    public void addGroup(String group) {
        hasGroup(group.toLowerCase()).thenAccept(success -> {
            if (!success) {
                this.queryAsync("INSERT INTO groups (RANKS) VALUES (?)", group.toLowerCase());
            }
        });
    }

    public void removeGroup(String group) {
        hasGroup(group.toLowerCase()).thenAccept(success -> {
            if (success) {
                this.queryAsync("DELETE FROM groups WHERE RANKS = ?", group.toLowerCase());
            }
        });
    }
}