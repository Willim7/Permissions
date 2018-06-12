package me.willis.permissions.util;

import me.willis.permissions.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.List;
import java.util.UUID;

public class PlayerManager {

    private Permissions plugin;

    public PlayerManager(Permissions plugin) {
        this.plugin = plugin;
    }

    public void setPrefix(UUID uuid, String prefix) {
        plugin.getpConfig().getConfig().set(uuid.toString() + ".Prefix", prefix);
        plugin.getpConfig().saveConfig();
    }

    public void setSuffix(UUID uuid, String suffix) {
        plugin.getpConfig().getConfig().set(uuid.toString() + ".Suffix", suffix);
        plugin.getpConfig().saveConfig();
    }

    public void addPermission(UUID uuid, String permission) {

        List<String> permissions = plugin.getpConfig().getConfig().getStringList(uuid.toString() + ".Permissions");

        if (!permissions.contains(permission)) {
            permissions.add(permission);
            plugin.getpConfig().getConfig().set(uuid.toString() + ".Permissions", permissions);
            plugin.getpConfig().saveConfig();
        }
    }

    public void removePermission(UUID uuid, String permission) {

        List<String> permissions = plugin.getpConfig().getConfig().getStringList(uuid.toString() + ".Permissions");

        if (permissions.contains(permission)) {
            permissions.remove(permission);
            plugin.getpConfig().getConfig().set(uuid.toString() + ".Permissions", permissions);
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
            for (String permissions : plugin.getpConfig().getConfig().getStringList(player.getUniqueId().toString() + ".Permissions")) {
                permissionAttachment.unsetPermission(permissions);
            }
        }
    }

    private void permissionSetter(UUID uuid) {

        PermissionAttachment permissionAttachment = plugin.getAttachment().get(uuid);

        if (permissionAttachment != null) {
            for (String permissions : plugin.getpConfig().getConfig().getStringList(uuid.toString() + ".Permissions")) {
                permissionAttachment.setPermission(permissions, true);
            }
        }
    }

    public String getPrefix(UUID uuid) {
        return plugin.getpConfig().getConfig().getString(uuid.toString() + ".Prefix");
    }

    public String getSuffix(UUID uuid) {
        return plugin.getpConfig().getConfig().getString(uuid.toString() + ".Suffix");
    }
}
