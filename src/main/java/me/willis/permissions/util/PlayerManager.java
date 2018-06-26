package me.willis.permissions.util;

import me.willis.permissions.Permissions;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.List;
import java.util.UUID;

public class PlayerManager {

    private Permissions plugin;

    public PlayerManager(Permissions plugin) {
        this.plugin = plugin;
    }

    public void createPlayerInfo(UUID uuid) {
        if (!plugin.getpConfig().getConfig().contains("Players." + uuid.toString())) {
            plugin.getpConfig().getConfig().set("Players." + uuid.toString() + ".Prefix", "");
            plugin.getpConfig().getConfig().set("Players." + uuid.toString() + ".Suffix", "");
            plugin.getpConfig().getConfig().set("Players." + uuid.toString() + ".Permissions", "");
            plugin.getpConfig().saveConfig();
        }
    }

    public void setPrefix(UUID uuid, String prefix) {
        plugin.getpConfig().getConfig().set("Players." + uuid.toString() + ".Prefix", prefix);
        plugin.getpConfig().saveConfig();
    }

    public void setSuffix(UUID uuid, String suffix) {
        plugin.getpConfig().getConfig().set("Players." + uuid.toString() + ".Suffix", suffix);
        plugin.getpConfig().saveConfig();
    }

    public void addPermission(Player player, String permission) {

        PermissionAttachment attachment = plugin.getAttachment().get(player.getUniqueId());

        List<String> permissions = plugin.getpConfig().getConfig().getStringList("Players." + player.getUniqueId().toString() + ".Permissions");

        if (!permissions.contains(permission.toLowerCase())) {
            permissions.add(permission.toLowerCase());

            plugin.getpConfig().getConfig().set("Players." + player.getUniqueId().toString() + ".Permissions", permissions);
            plugin.getpConfig().saveConfig();

            if (attachment != null) {
                attachment.setPermission(permission, true);
            }
        }
    }

    public void addPermission(OfflinePlayer player, String permission) {

        List<String> permissions = plugin.getpConfig().getConfig().getStringList("Players." + player.getUniqueId().toString() + ".Permissions");

        if (!permissions.contains(permission.toLowerCase())) {
            permissions.add(permission.toLowerCase());
            plugin.getpConfig().getConfig().set("Players." + player.getUniqueId().toString() + ".Permissions", permissions);
            plugin.getpConfig().saveConfig();
        }
    }

    public void removePermission(Player player, String permission) {

        PermissionAttachment attachment = plugin.getAttachment().get(player.getUniqueId());

        List<String> permissions = plugin.getpConfig().getConfig().getStringList("Players." + player.getUniqueId().toString() + ".Permissions");

        if (permissions.contains(permission.toLowerCase())) {
            permissions.remove(permission.toLowerCase());

            plugin.getpConfig().getConfig().set("Players." + player.getUniqueId().toString() + ".Permissions", permissions);
            plugin.getpConfig().saveConfig();

            if (attachment != null) {
                attachment.setPermission(permission, false);
            }
        }
    }

    public void removePermission(OfflinePlayer player, String permission) {

        List<String> permissions = plugin.getpConfig().getConfig().getStringList("Players." + player.getUniqueId().toString() + ".Permissions");

        if (permissions.contains(permission.toLowerCase())) {
            permissions.remove(permission.toLowerCase());
            plugin.getpConfig().getConfig().set("Players." + player.getUniqueId().toString() + ".Permissions", permissions);
            plugin.getpConfig().saveConfig();
        }
    }

    public void addPlayerPermissions(Player player) {

        PermissionAttachment permissionAttachment = plugin.getAttachment().get(player.getUniqueId());

        if (permissionAttachment == null) {

            PermissionAttachment attachment = player.addAttachment(plugin);

            plugin.getAttachment().put(player.getUniqueId(), attachment);
            permissionSetter(player.getUniqueId());

        } else {

            permissionSetter(player.getUniqueId());
        }
    }

    public void removePlayerPermissions(Player player) {

        PermissionAttachment permissionAttachment = plugin.getAttachment().get(player.getUniqueId());

        if (permissionAttachment != null) {
            for (String permissions : plugin.getpConfig().getConfig().getStringList("Players." + player.getUniqueId().toString() + ".Permissions")) {
                permissionAttachment.unsetPermission(permissions);
            }
        }
    }

    private void permissionSetter(UUID uuid) {

        PermissionAttachment permissionAttachment = plugin.getAttachment().get(uuid);

        if (permissionAttachment != null) {
            for (String permissions : plugin.getpConfig().getConfig().getStringList("Players." + uuid.toString() + ".Permissions")) {
                permissionAttachment.setPermission(permissions, true);
            }
        }
    }

    public String getPrefix(UUID uuid) {
        return plugin.getpConfig().getConfig().getString("Players." + uuid.toString() + ".Prefix");
    }

    public String getSuffix(UUID uuid) {
        return plugin.getpConfig().getConfig().getString("Players." + uuid.toString() + ".Suffix");
    }
}
