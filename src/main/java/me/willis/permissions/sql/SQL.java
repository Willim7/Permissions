package me.willis.permissions.sql;

import me.justrayz.rlib.datamanagement.database.DatabaseType;
import me.justrayz.rlib.datamanagement.database.RDatabaseManager;
import me.willis.permissions.Permissions;
import me.willis.permissions.util.GroupManager;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SQL extends RDatabaseManager {

    private GroupManager groupManager;

    public SQL(Permissions plugin) {
        super(plugin.getConfig().getString("Host") + ":" + plugin.getConfig().getString("Port") + "/" + plugin.getConfig().getString("Database"), DatabaseType.MYSQL, plugin.getConfig().getString("Username"), plugin.getConfig().getString("Password"));
        this.groupManager = new GroupManager(plugin);
    }

    @Override
    public void generate() {
        boolean success = this.query("CREATE TABLE IF NOT EXISTS players (UUID VARCHAR(100),RANK VARCHAR(100))");
    }

    public CompletableFuture<Boolean> hasGroup(UUID uuid) {
        return this.queryResultAsync("SELECT * FROM players WHERE UUID = ?", uuid.toString()).thenApply((rows) -> {
            return !rows.isEmpty();
        });
    }

    public void setGroup(UUID uuid, String group) {
        hasGroup(uuid).thenAccept(ok -> {
            if (!ok) {
                this.queryAsync("INSERT INTO players (UUID,RANK) VALUES (?,?)", uuid.toString(), group.toLowerCase());
            }
        });
    }

    public void updateGroup(UUID uuid, String group) {
        this.queryAsync("UPDATE players SET RANK = ? WHERE UUID = ?", group.toLowerCase(), uuid.toString());
    }

    public CompletableFuture<String> getGroup(UUID uuid) {
        return this.queryResultAsync("SELECT * FROM players WHERE UUID=? LIMIT 1;", uuid.toString()).thenApply((rows) -> {
            if (!rows.isEmpty()) {
                return (String) rows.get(0).get("RANK");
            } else {
                return groupManager.getDefaultGroup();
            }
        });
    }
}