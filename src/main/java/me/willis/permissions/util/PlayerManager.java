package me.willis.permissions.util;

import me.willis.permissions.Permissions;
import me.willis.permissions.configuration.Config;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.List;
import java.util.UUID;

public class PlayerManager extends Config {

    private Permissions plugin;

    public PlayerManager(Permissions plugin) {
        super(plugin, "players", "Players");
        this.plugin = plugin;
    }

    public void createPlayerInfo(UUID uuid) {
        if (!getConfig().contains("Players." + uuid.toString())) {
            getConfig().set("Players." + uuid.toString() + ".Prefix", "");
            getConfig().set("Players." + uuid.toString() + ".Suffix", "");
            getConfig().set("Players." + uuid.toString() + ".Permissions", "");
            saveConfig();
        }
    }

    public void setPrefix(UUID uuid, String prefix) {
        getConfig().set("Players." + uuid.toString() + ".Prefix", prefix);
        saveConfig();
    }

    public void setSuffix(UUID uuid, String suffix) {
        getConfig().set("Players." + uuid.toString() + ".Suffix", suffix);
        saveConfig();
    }

    public void addPermission(Player player, String permission) {

        PermissionAttachment attachment = plugin.getAttachment().get(player.getUniqueId());

        List<String> permissions = getConfig().getStringList("Players." + player.getUniqueId().toString() + ".Permissions");

        if (!permissions.contains(permission.toLowerCase())) {
            permissions.add(permission.toLowerCase());

            getConfig().set("Players." + player.getUniqueId().toString() + ".Permissions", permissions);
            saveConfig();

            if (attachment != null) {
                attachment.setPermission(permission, true);
            }
        }
    }

    public void addPermission(OfflinePlayer player, String permission) {

        List<String> permissions = getConfig().getStringList("Players." + player.getUniqueId().toString() + ".Permissions");

        if (!permissions.contains(permission.toLowerCase())) {
            permissions.add(permission.toLowerCase());
            getConfig().set("Players." + player.getUniqueId().toString() + ".Permissions", permissions);
            saveConfig();
        }
    }

    public void removePermission(Player player, String permission) {

        PermissionAttachment attachment = plugin.getAttachment().get(player.getUniqueId());

        List<String> permissions = getConfig().getStringList("Players." + player.getUniqueId().toString() + ".Permissions");

        if (permissions.contains(permission.toLowerCase())) {
            permissions.remove(permission.toLowerCase());

            getConfig().set("Players." + player.getUniqueId().toString() + ".Permissions", permissions);
            saveConfig();

            if (attachment != null) {
                attachment.setPermission(permission, false);
            }
        }
    }

    public void removePermission(OfflinePlayer player, String permission) {

        List<String> permissions = getConfig().getStringList("Players." + player.getUniqueId().toString() + ".Permissions");

        if (permissions.contains(permission.toLowerCase())) {
            permissions.remove(permission.toLowerCase());
            getConfig().set("Players." + player.getUniqueId().toString() + ".Permissions", permissions);
            saveConfig();
        }
    }

    public void addPlayerPermissions(Player player) {

        PermissionAttachment permissionAttachment = plugin.getAttachment().get(player.getUniqueId());

        if (permissionAttachment == null) {

            PermissionAttachment attachment = player.addAttachment(plugin);

            for (String permissions : getConfig().getStringList("Players." + player.getUniqueId().toString() + ".Permissions")) {

                attachment.setPermission(permissions, true);

                plugin.getAttachment().put(player.getUniqueId(), attachment);

            }
        } else {
            for (String permissions : getConfig().getStringList("Players." + player.getUniqueId().toString() + ".Permissions")) {
                permissionAttachment.setPermission(permissions, true);
            }
        }
    }

    public void removePlayerPermissions(Player player) {

        PermissionAttachment permissionAttachment = plugin.getAttachment().get(player.getUniqueId());

        if (permissionAttachment != null) {
            for (String permissions : getConfig().getStringList("Players." + player.getUniqueId().toString() + ".Permissions")) {
                permissionAttachment.unsetPermission(permissions);
            }
        }
    }

    public String getPrefix(UUID uuid) {
        return getConfig().getString("Players." + uuid.toString() + ".Prefix");
    }

    public String getSuffix(UUID uuid) {
        return getConfig().getString("Players." + uuid.toString() + ".Suffix");
    }
}
