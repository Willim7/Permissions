package me.willis.permissions.util;

import me.willis.permissions.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.List;
import java.util.UUID;

public class GroupManager {

    private Permissions plugin;

    public GroupManager(Permissions plugin) {
        this.plugin = plugin;
    }

    private boolean isGroupCreated(String group) {
        return plugin.getsConfig().getConfig().contains("Groups." + group);
    }

    public void createGroup(String group) {
        if (!isGroupCreated(group)) {
            plugin.getsConfig().getConfig().set("Groups." + group + ".Prefix", "");
            plugin.getsConfig().getConfig().set("Groups." + group + ".Suffix", "");
            plugin.getsConfig().getConfig().set("Groups." + group + ".Permissions", "");
            plugin.getsConfig().saveConfig();
        }
    }

    public void deleteGroup(String group) {
        if (isGroupCreated(group)) {
            plugin.getsConfig().getConfig().set("Groups." + group, null);
            plugin.getsConfig().saveConfig();
        }

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            updatePlayerGroups(group, player);
        }
    }

    public void setPrefix(String group, String prefix) {
        if (isGroupCreated(group)) {
            plugin.getsConfig().getConfig().set("Groups." + group + ".Prefix", prefix);
            plugin.getsConfig().saveConfig();
        }
    }

    public void setSuffix(String group, String suffix) {
        if (isGroupCreated(group)) {
            plugin.getsConfig().getConfig().set("Groups." + group + ".Suffix", suffix);
            plugin.getsConfig().saveConfig();
        }
    }

    public void addPermission(String group, String permission) {
        if (isGroupCreated(group)) {

            List<String> permissions = plugin.getsConfig().getConfig().getStringList("Groups." + group + ".Permissions");

            if (!permissions.contains(permission)) {
                permissions.add(permission);
                plugin.getsConfig().getConfig().set("Groups." + group + ".Permissions", permissions);
                plugin.getsConfig().saveConfig();
            }

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                updateGroupPermissions(group, permission, player, true);
            }
        }
    }

    public void removePermission(String group, String permission) {
        if (isGroupCreated(group)) {

            List<String> permissions = plugin.getsConfig().getConfig().getStringList("Groups." + group + ".Permissions");

            if (permissions.contains(permission)) {
                permissions.remove(permission);
                plugin.getsConfig().getConfig().set("Groups." + group + ".Permissions", permissions);
                plugin.getsConfig().saveConfig();
            }

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                updateGroupPermissions(group, permission, player, false);
            }
        }
    }

    public void addGroupPermissions(Player player) {

        PermissionAttachment permissionAttachment = plugin.getAttachment().get(player.getUniqueId());

        if (permissionAttachment == null) {

            PermissionAttachment attachment = player.addAttachment(plugin);

            plugin.getAttachment().put(player.getUniqueId(), attachment);

            if (!isGroupCreated(plugin.getSqlConfig().getGroup(player.getUniqueId()))) {
                plugin.getSqlConfig().updateGroup(player.getUniqueId(), getDefaultGroup());
            }

            permissionSetter(player.getUniqueId());

        } else {

            permissionSetter(player.getUniqueId());
        }
    }

    public void removeGroupPermissions(Player player) {

        PermissionAttachment permissionAttachment = plugin.getAttachment().get(player.getUniqueId());

        if (permissionAttachment != null) {
            for (String permissions : plugin.getsConfig().getConfig().getStringList("Groups." + plugin.getSqlConfig().getGroup(player.getUniqueId()) + ".Permissions")) {
                permissionAttachment.unsetPermission(permissions);
            }
        }
    }

    private void permissionSetter(UUID uuid) {

        PermissionAttachment permissionAttachment = plugin.getAttachment().get(uuid);

        if (permissionAttachment != null) {
            for (String permissions : plugin.getsConfig().getConfig().getStringList("Groups." + plugin.getSqlConfig().getGroup(uuid) + ".Permissions")) {
                permissionAttachment.setPermission(permissions, true);
            }
        }
    }

    public void setPlayerGroup(String group, Player player) {
        if (isGroupCreated(group)) {
            if (plugin.getSqlConfig().hasGroup(player.getUniqueId())) {

                removeGroupPermissions(player);

                plugin.getSqlConfig().updateGroup(player.getUniqueId(), group);

                addGroupPermissions(player);
            }
        }
    }

    public void updatePlayerGroups(String group, Player player) {
        if (plugin.getSqlConfig().getGroup(player.getUniqueId()).equalsIgnoreCase(group)) {

            removeGroupPermissions(player);

            plugin.getSqlConfig().updateGroup(player.getUniqueId(), getDefaultGroup());

            addGroupPermissions(player);
        }
    }

    public void updateGroupPermissions(String group, String permission, Player player, boolean adding) {
        if (adding) {

            if (plugin.getSqlConfig().getGroup(player.getUniqueId()).equalsIgnoreCase(group)) {

                PermissionAttachment permissionAttachment = plugin.getAttachment().get(player.getUniqueId());

                if (permissionAttachment != null) {

                    permissionAttachment.setPermission(permission, true);

                } else {

                    addGroupPermissions(player);
                }
            }

        } else {

            if (plugin.getSqlConfig().getGroup(player.getUniqueId()).equalsIgnoreCase(group)) {

                PermissionAttachment permissionAttachment = plugin.getAttachment().get(player.getUniqueId());

                if (permission != null) {

                    permissionAttachment.setPermission(permission, false);

                } else {

                    addGroupPermissions(player);
                }
            }
        }
    }

    public String getDefaultGroup() {
        return plugin.getConfig().getString("DefaultGroup");
    }

    public String getPrefix(UUID uuid) {
        return plugin.getsConfig().getConfig().getString("Groups." + plugin.getSqlConfig().getGroup(uuid) + ".Prefix");
    }

    public String getSuffix(UUID uuid) {
        return plugin.getsConfig().getConfig().getString("Groups." + plugin.getSqlConfig().getGroup(uuid) + ".Suffix");
    }
}