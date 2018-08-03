package me.willis.permissions.util;

import me.willis.permissions.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.List;
import java.util.UUID;

public class GroupManager {

    private Permissions plugin;

    public GroupManager(Permissions plugin) {
        this.plugin = plugin;
    }

    public boolean isGroupCreated(String group) {
        return plugin.getgConfig().getConfig().contains("Groups." + group.toLowerCase());
    }

    public void createGroup(String group) {
        if (!isGroupCreated(group)) {
            plugin.getgConfig().getConfig().set("Groups." + group.toLowerCase() + ".Prefix", "");
            plugin.getgConfig().getConfig().set("Groups." + group.toLowerCase() + ".Suffix", "");
            plugin.getgConfig().getConfig().set("Groups." + group.toLowerCase() + ".Permissions", "");
            plugin.getgConfig().saveConfig();
        }
    }

    public void deleteGroup(String group) {
        if (isGroupCreated(group)) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                updatePlayerGroups(group, player);
            }
        }

        plugin.getgConfig().getConfig().set("Groups." + group.toLowerCase(), null);
        plugin.getgConfig().saveConfig();
    }

    public void setPrefix(String group, String prefix) {
        if (isGroupCreated(group)) {
            plugin.getgConfig().getConfig().set("Groups." + group.toLowerCase() + ".Prefix", prefix);
            plugin.getgConfig().saveConfig();
        }
    }

    public void setSuffix(String group, String suffix) {
        if (isGroupCreated(group)) {
            plugin.getgConfig().getConfig().set("Groups." + group.toLowerCase() + ".Suffix", suffix);
            plugin.getgConfig().saveConfig();
        }
    }

    public void addPermission(String group, String permission) {
        if (isGroupCreated(group)) {

            List<String> permissions = plugin.getgConfig().getConfig().getStringList("Groups." + group.toLowerCase() + ".Permissions");

            if (!permissions.contains(permission.toLowerCase())) {
                permissions.add(permission.toLowerCase());
                plugin.getgConfig().getConfig().set("Groups." + group.toLowerCase() + ".Permissions", permissions);
                plugin.getgConfig().saveConfig();
            }

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                updateGroupPermissions(group.toLowerCase(), permission, player, true);
            }
        }
    }

    public void removePermission(String group, String permission) {
        if (isGroupCreated(group)) {

            List<String> permissions = plugin.getgConfig().getConfig().getStringList("Groups." + group.toLowerCase() + ".Permissions");

            if (permissions.contains(permission.toLowerCase())) {
                permissions.remove(permission.toLowerCase());
                plugin.getgConfig().getConfig().set("Groups." + group.toLowerCase() + ".Permissions", permissions);
                plugin.getgConfig().saveConfig();
            }

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                updateGroupPermissions(group.toLowerCase(), permission, player, false);
            }
        }
    }

    public void addGroupPermissions(Player player) {

        if (!isGroupCreated(plugin.getGroup().get(player.getUniqueId()))) {
            plugin.getSql().updateGroup(player.getUniqueId(), getDefaultGroup());
        }

        PermissionAttachment permissionAttachment = plugin.getAttachment().get(player.getUniqueId());

        if (permissionAttachment == null) {

            PermissionAttachment attachment = player.addAttachment(plugin);

            plugin.getAttachment().put(player.getUniqueId(), attachment);

            for (String permissions : plugin.getgConfig().getConfig().getStringList("Groups." + plugin.getGroup().get(player.getUniqueId()).toLowerCase() + ".Permissions")) {
                permissionAttachment.setPermission(permissions, true);
            }

        } else {

            for (String permissions : plugin.getgConfig().getConfig().getStringList("Groups." + plugin.getGroup().get(player.getUniqueId()).toLowerCase() + ".Permissions")) {
                permissionAttachment.setPermission(permissions, true);
            }
        }
    }

    public void removeGroupPermissions(Player player) {

        PermissionAttachment permissionAttachment = plugin.getAttachment().get(player.getUniqueId());

        if (permissionAttachment != null) {
            for (String permissions : plugin.getgConfig().getConfig().getStringList("Groups." + plugin.getGroup().get(player.getUniqueId()).toLowerCase() + ".Permissions")) {
                permissionAttachment.unsetPermission(permissions);
            }
        }
    }

    public void setPlayerGroup(String group, Player player) {
        if (isGroupCreated(group)) {
            removeGroupPermissions(player);

            if (plugin.getGroup().containsKey(player.getUniqueId())) {
                plugin.getGroup().remove(player.getUniqueId());
            }

            plugin.getGroup().put(player.getUniqueId(), group.toLowerCase());

            plugin.getSql().updateGroup(player.getUniqueId(), group.toLowerCase());

            addGroupPermissions(player);
        }
    }

    public void setPlayerGroup(String group, OfflinePlayer player) {
        if (isGroupCreated(group)) {
            plugin.getSql().updateGroup(player.getUniqueId(), group.toLowerCase());
        }
    }

    private void updatePlayerGroups(String group, Player player) {
        if (plugin.getGroup().get(player.getUniqueId()).equalsIgnoreCase(group.toLowerCase())) {

            removeGroupPermissions(player);

            plugin.getSql().updateGroup(player.getUniqueId(), getDefaultGroup());

            addGroupPermissions(player);
        }
    }

    private void updateGroupPermissions(String group, String permission, Player player, boolean adding) {
        if (adding) {

            if (plugin.getGroup().get(player.getUniqueId()).toLowerCase().equalsIgnoreCase(group.toLowerCase())) {

                PermissionAttachment permissionAttachment = plugin.getAttachment().get(player.getUniqueId());

                if (permissionAttachment != null) {

                    permissionAttachment.setPermission(permission, true);

                } else {

                    addGroupPermissions(player);
                }
            }

        } else {

            if (plugin.getGroup().get(player.getUniqueId()).toLowerCase().equalsIgnoreCase(group.toLowerCase())) {

                PermissionAttachment permissionAttachment = plugin.getAttachment().get(player.getUniqueId());

                if (permission != null) {

                    permissionAttachment.setPermission(permission, false);

                } else {

                    addGroupPermissions(player);
                }
            }
        }
    }


    public void getGroups(CommandSender sender) {

        sender.sendMessage(ChatColor.YELLOW + "Available Groups:");

        for (String s : plugin.getgConfig().getConfig().getConfigurationSection("Groups").getKeys(false)) {
            sender.sendMessage(ChatColor.YELLOW + "  -> " + s.toLowerCase());
        }
    }

    public String getDefaultGroup() {
        return plugin.getConfig().getString("DefaultGroup").toLowerCase();
    }

    public String getPrefix(UUID uuid) {
        return plugin.getgConfig().getConfig().getString("Groups." + plugin.getGroup().get(uuid).toLowerCase() + ".Prefix");
    }

    public String getSuffix(UUID uuid) {
        return plugin.getgConfig().getConfig().getString("Groups." + plugin.getGroup().get(uuid).toLowerCase() + ".Suffix");
    }
}
