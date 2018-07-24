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
        return plugin.getsConfig().getConfig().contains("Groups." + group.toLowerCase());
    }

    public void createGroup(String group) {
        if (!isGroupCreated(group)) {
            plugin.getsConfig().getConfig().set("Groups." + group.toLowerCase() + ".Prefix", "");
            plugin.getsConfig().getConfig().set("Groups." + group.toLowerCase() + ".Suffix", "");
            plugin.getsConfig().getConfig().set("Groups." + group.toLowerCase() + ".Permissions", "");
            plugin.getsConfig().saveConfig();
        }
    }

    public void deleteGroup(String group) {
        if (isGroupCreated(group)) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                updatePlayerGroups(group, player);
            }
        }

        plugin.getsConfig().getConfig().set("Groups." + group.toLowerCase(), null);
        plugin.getsConfig().saveConfig();
    }

    public void setPrefix(String group, String prefix) {
        if (isGroupCreated(group)) {
            plugin.getsConfig().getConfig().set("Groups." + group.toLowerCase() + ".Prefix", prefix);
            plugin.getsConfig().saveConfig();
        }
    }

    public void setSuffix(String group, String suffix) {
        if (isGroupCreated(group)) {
            plugin.getsConfig().getConfig().set("Groups." + group.toLowerCase() + ".Suffix", suffix);
            plugin.getsConfig().saveConfig();
        }
    }

    public void addPermission(String group, String permission) {
        if (isGroupCreated(group)) {

            List<String> permissions = plugin.getsConfig().getConfig().getStringList("Groups." + group.toLowerCase() + ".Permissions");

            if (!permissions.contains(permission.toLowerCase())) {
                permissions.add(permission.toLowerCase());
                plugin.getsConfig().getConfig().set("Groups." + group.toLowerCase() + ".Permissions", permissions);
                plugin.getsConfig().saveConfig();
            }

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                updateGroupPermissions(group.toLowerCase(), permission, player, true);
            }
        }
    }

    public void removePermission(String group, String permission) {
        if (isGroupCreated(group)) {

            List<String> permissions = plugin.getsConfig().getConfig().getStringList("Groups." + group.toLowerCase() + ".Permissions");

            if (permissions.contains(permission.toLowerCase())) {
                permissions.remove(permission.toLowerCase());
                plugin.getsConfig().getConfig().set("Groups." + group.toLowerCase() + ".Permissions", permissions);
                plugin.getsConfig().saveConfig();
            }

            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                updateGroupPermissions(group.toLowerCase(), permission, player, false);
            }
        }
    }

    public void addGroupPermissions(Player player) {

        PermissionAttachment permissionAttachment = plugin.getAttachment().get(player.getUniqueId());

        if (permissionAttachment == null) {

            PermissionAttachment attachment = player.addAttachment(plugin);

            plugin.getAttachment().put(player.getUniqueId(), attachment);

            if (!isGroupCreated(plugin.getGroup().get(player.getUniqueId()))) {
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
            for (String permissions : plugin.getsConfig().getConfig().getStringList("Groups." + plugin.getGroup().get(player.getUniqueId()).toLowerCase() + ".Permissions")) {
                permissionAttachment.unsetPermission(permissions);
            }
        }
    }

    private void permissionSetter(UUID uuid) {

        PermissionAttachment permissionAttachment = plugin.getAttachment().get(uuid);

        if (permissionAttachment != null) {
            for (String permissions : plugin.getsConfig().getConfig().getStringList("Groups." + plugin.getGroup().get(uuid).toLowerCase() + ".Permissions")) {
                permissionAttachment.setPermission(permissions, true);
            }
        }
    }

    public void setPlayerGroup(String group, Player player) {
        if (isGroupCreated(group)) {
            if (plugin.getSqlConfig().hasGroup(player.getUniqueId())) {

                removeGroupPermissions(player);

                if (plugin.getGroup().containsKey(player.getUniqueId())) {
                    plugin.getGroup().remove(player.getUniqueId());
                }

                plugin.getGroup().put(player.getUniqueId(), group.toLowerCase());

                plugin.getSqlConfig().updateGroup(player.getUniqueId(), group.toLowerCase());

                addGroupPermissions(player);
            }
        }
    }

    public void setPlayerGroup(String group, OfflinePlayer player) {
        if (isGroupCreated(group)) {
            if (plugin.getSqlConfig().hasGroup(player.getUniqueId())) {

                plugin.getSqlConfig().updateGroup(player.getUniqueId(), group.toLowerCase());
            }
        }
    }

    private void updatePlayerGroups(String group, Player player) {
        if (plugin.getGroup().get(player.getUniqueId()).equalsIgnoreCase(group.toLowerCase())) {

            removeGroupPermissions(player);

            plugin.getSqlConfig().updateGroup(player.getUniqueId(), getDefaultGroup());

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

        for (String s : plugin.getsConfig().getConfig().getConfigurationSection("Groups").getKeys(false)) {
            sender.sendMessage(ChatColor.YELLOW + "  -> " + s.toLowerCase());
        }
    }

    public String getDefaultGroup() {
        return plugin.getConfig().getString("DefaultGroup").toLowerCase();
    }

    public String getPrefix(UUID uuid) {
        return plugin.getsConfig().getConfig().getString("Groups." + plugin.getGroup().get(uuid).toLowerCase() + ".Prefix");
    }

    public String getSuffix(UUID uuid) {
        return plugin.getsConfig().getConfig().getString("Groups." + plugin.getGroup().get(uuid).toLowerCase() + ".Suffix");
    }
}