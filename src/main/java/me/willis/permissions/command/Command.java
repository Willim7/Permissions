package me.willis.permissions.command;

import me.willis.permissions.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

public class Command implements CommandExecutor {

    private Permissions plugin;

    public Command(Permissions plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender.hasPermission("permissions.admin")) {
            if (args.length == 0) {

                sender.sendMessage(ChatColor.YELLOW + "Group Commands:");
                sender.sendMessage(ChatColor.YELLOW + "  -> p groups");
                sender.sendMessage(ChatColor.YELLOW + "  -> p group view (group)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p group create (group)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p group delete (group)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p group (group) set prefix (prefix)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p group (group) set suffix (suffix)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p group (group) add perm (permission)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p group (group) remove perm (permission)");
                sender.sendMessage("");
                sender.sendMessage(ChatColor.YELLOW + "Player Commands:");
                sender.sendMessage(ChatColor.YELLOW + "  -> p player view (player)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p player (player) group set (group)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p player (player) set prefix (prefix)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p player (player) set suffix (suffix)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p player (player) add perm (permission)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p player (player) remove perm (permission)");
                return true;
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("groups") || (args[0].equalsIgnoreCase("gs"))) {

                    String message = ChatColor.YELLOW + "Groups: ";

                    for (Iterator iterator = plugin.getPermissionsAPI().getGroupManager().getGroups().iterator(); iterator.hasNext(); ) {

                        String groups = (String) iterator.next();

                        message = message + groups + ", ";
                    }

                    sender.sendMessage(message);

                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("group") || (args[0].equalsIgnoreCase("g"))) {
                if (args.length == 3) {

                    if (args[1].equalsIgnoreCase("view") || (args[1].equalsIgnoreCase("v"))) {

                        if (!plugin.getPermissionsAPI().getGroupManager().isGroupCreated(args[2])) {
                            sender.sendMessage(ChatColor.RED + "[!] The group doesn't exist");
                            return true;
                        }

                        sender.sendMessage(ChatColor.YELLOW + "Group View:");
                        sender.sendMessage(ChatColor.YELLOW + "  Name: " + args[2]);
                        sender.sendMessage(ChatColor.YELLOW + "  Prefix: " + plugin.getPermissionsAPI().getGroupManager().getPrefix(args[2]));
                        sender.sendMessage(ChatColor.YELLOW + "  Suffix: " + plugin.getPermissionsAPI().getGroupManager().getSuffix(args[2]));
                        sender.sendMessage(ChatColor.YELLOW + "  Permissions:");

                        List<String> permissions = plugin.getPermissionsAPI().getGroupManager().getPermissions(args[2]);

                        for (int i = 0; i < permissions.size(); i++) {
                            sender.sendMessage(ChatColor.YELLOW + "    - " + permissions.get(i));
                        }
                    } else if (args[1].equalsIgnoreCase("create") || (args[1].equalsIgnoreCase("c"))) {

                        if (plugin.getPermissionsAPI().getGroupManager().isGroupCreated(args[2])) {
                            sender.sendMessage(ChatColor.RED + "[!] The group has already been created");
                            return true;
                        }

                        plugin.getPermissionsAPI().getGroupManager().createGroup(args[2]);

                        sender.sendMessage(ChatColor.GREEN + "[!] Group has been created");

                    } else if (args[1].equalsIgnoreCase("delete") || (args[1].equalsIgnoreCase("d"))) {

                        if (plugin.getPermissionsAPI().getGroupManager().getDefaultGroup().equalsIgnoreCase(args[2].toLowerCase())) {
                            sender.sendMessage(ChatColor.RED + "[!] You can't delete the default group");
                            return false;
                        }

                        if (!plugin.getPermissionsAPI().getGroupManager().isGroupCreated(args[2])) {
                            sender.sendMessage(ChatColor.RED + "[!] The group has yet to be created");
                            return true;
                        }

                        plugin.getPermissionsAPI().getGroupManager().deleteGroup(args[2]);

                        sender.sendMessage(ChatColor.GREEN + "[!] Group has been deleted");
                    }

                } else if (args.length == 5) {

                    if (!plugin.getPermissionsAPI().getGroupManager().isGroupCreated(args[1])) {
                        sender.sendMessage(ChatColor.RED + "[!] The group doesn't exist");
                        return true;
                    }

                    if (args[2].equalsIgnoreCase("set") || (args[2].equalsIgnoreCase("s"))) {

                        if (args[3].equalsIgnoreCase("prefix") || (args[3].equalsIgnoreCase("p"))) {

                            if (args[4].equalsIgnoreCase("\"\"")) {

                                plugin.getPermissionsAPI().getGroupManager().setGroupPrefix(args[1], "");

                            } else {

                                plugin.getPermissionsAPI().getGroupManager().setGroupPrefix(args[1], args[4]);
                            }

                            sender.sendMessage(ChatColor.GREEN + "[!] Group prefix has been changed");

                        } else if (args[3].equalsIgnoreCase("suffix") || (args[3].equalsIgnoreCase("s"))) {

                            if (args[4].equalsIgnoreCase("\"\"")) {

                                plugin.getPermissionsAPI().getGroupManager().setGroupSuffix(args[1], "");

                            } else {

                                plugin.getPermissionsAPI().getGroupManager().setGroupSuffix(args[1], args[4]);
                            }

                            sender.sendMessage(ChatColor.GREEN + "[!] Group suffix has been changed");
                        }
                    } else if (args[2].equalsIgnoreCase("add") || (args[2].equalsIgnoreCase("a"))) {

                        if (args[3].equalsIgnoreCase("perm") || (args[3].equalsIgnoreCase("p"))) {

                            plugin.getPermissionsAPI().getGroupManager().addPermission(args[1], args[4]);

                            sender.sendMessage(ChatColor.GREEN + "[!] Group permission has been added");

                        }
                    } else if (args[2].equalsIgnoreCase("remove") || (args[2].equalsIgnoreCase("r"))) {

                        if (args[3].equalsIgnoreCase("perm") || (args[3].equalsIgnoreCase("p"))) {

                            plugin.getPermissionsAPI().getGroupManager().removePermission(args[1], args[4]);

                            sender.sendMessage(ChatColor.GREEN + "[!] Group permission has been removed");

                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("player") || (args[0].equalsIgnoreCase("p"))) {

                if (args.length == 3) {
                    if (args[1].equalsIgnoreCase("view") || (args[1].equalsIgnoreCase("v"))) {

                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[2]);

                        if (offlinePlayer.isOnline()) {

                            Player player = Bukkit.getPlayer(args[2]);

                            if (plugin.getPermissionsAPI().getPlayerManager().hasAccount(player.getUniqueId())) {

                                sender.sendMessage(ChatColor.YELLOW + "Player View:");
                                sender.sendMessage(ChatColor.YELLOW + "  Name: " + player.getName());
                                sender.sendMessage(ChatColor.YELLOW + "  Prefix: " + plugin.getPermissionsAPI().getPlayerManager().getPlayerPrefix(player.getUniqueId()));
                                sender.sendMessage(ChatColor.YELLOW + "  Suffix: " + plugin.getPermissionsAPI().getPlayerManager().getPlayerSuffix(player.getUniqueId()));
                                sender.sendMessage(ChatColor.YELLOW + "  Permissions:");

                                List<String> permissions = plugin.getPermissionsAPI().getPlayerManager().getPermissions(player.getUniqueId());

                                for (int i = 0; i < permissions.size(); i++) {
                                    sender.sendMessage(ChatColor.YELLOW + "    - " + permissions.get(i));
                                }
                            }
                        } else {

                            if (!offlinePlayer.hasPlayedBefore()) {
                                sender.sendMessage(ChatColor.RED + "[!] Player has never joined this server");
                                return true;
                            }

                            if (plugin.getPermissionsAPI().getPlayerManager().hasAccount(offlinePlayer.getUniqueId())) {

                                sender.sendMessage(ChatColor.YELLOW + "Player View:");
                                sender.sendMessage(ChatColor.YELLOW + "  Name: " + offlinePlayer.getName());
                                sender.sendMessage(ChatColor.YELLOW + "  Prefix: " + plugin.getPermissionsAPI().getPlayerManager().getPlayerPrefix(offlinePlayer.getUniqueId()));
                                sender.sendMessage(ChatColor.YELLOW + "  Suffix: " + plugin.getPermissionsAPI().getPlayerManager().getPlayerSuffix(offlinePlayer.getUniqueId()));
                                sender.sendMessage(ChatColor.YELLOW + "  Permissions:");

                                List<String> permissions = plugin.getPermissionsAPI().getPlayerManager().getPermissions(offlinePlayer.getUniqueId());

                                for (int i = 0; i < permissions.size(); i++) {
                                    sender.sendMessage(ChatColor.YELLOW + "    - " + permissions.get(i));
                                }
                            }
                        }
                    }
                } else if (args.length == 5) {

                    if (args[2].equalsIgnoreCase("group") || (args[2].equalsIgnoreCase("g"))) {

                        if (args[3].equalsIgnoreCase("set") || (args[3].equalsIgnoreCase("s"))) {

                            if (!plugin.getPermissionsAPI().getGroupManager().isGroupCreated(args[4])) {
                                sender.sendMessage(ChatColor.RED + "[!] The group doesn't exist");
                                return true;
                            }

                            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

                            if (offlinePlayer.isOnline()) {

                                Player player = Bukkit.getServer().getPlayer(args[1]);

                                plugin.getPermissionsAPI().getGroupManager().setPlayerGroup(args[4], player);

                                sender.sendMessage(ChatColor.GREEN + "[!] Player group has been changed");

                            } else {

                                if (!offlinePlayer.hasPlayedBefore()) {
                                    sender.sendMessage(ChatColor.RED + "[!] Player has never joined this server");
                                    return true;
                                }

                                plugin.getPermissionsAPI().getGroupManager().setPlayerGroup(args[4], offlinePlayer);

                                sender.sendMessage(ChatColor.GREEN + "[!] Player group has been changed");
                            }
                        }
                    } else if (args[2].equalsIgnoreCase("set") || (args[2].equalsIgnoreCase("s"))) {

                        if (args[3].equalsIgnoreCase("prefix") || (args[3].equalsIgnoreCase("p"))) {

                            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

                            if (offlinePlayer.isOnline()) {

                                Player player = Bukkit.getServer().getPlayer(args[1]);

                                if (args[4].equalsIgnoreCase("\"\"")) {

                                    plugin.getPermissionsAPI().getPlayerManager().setPlayerPrefix(player.getUniqueId(), "");

                                } else {

                                    if (!offlinePlayer.hasPlayedBefore()) {
                                        sender.sendMessage(ChatColor.RED + "[!] Player has never joined this server");
                                        return true;
                                    }

                                    plugin.getPermissionsAPI().getPlayerManager().setPlayerPrefix(player.getUniqueId(), args[4]);
                                }

                                sender.sendMessage(ChatColor.GREEN + "[!] Player prefix has been changed");

                            } else {

                                if (args[4].equalsIgnoreCase("\"\"")) {

                                    plugin.getPermissionsAPI().getPlayerManager().setPlayerPrefix(offlinePlayer.getUniqueId(), "");

                                } else {

                                    plugin.getPermissionsAPI().getPlayerManager().setPlayerPrefix(offlinePlayer.getUniqueId(), args[4]);
                                }

                                sender.sendMessage(ChatColor.GREEN + "[!] Player prefix has been changed");
                            }
                        } else if (args[3].equalsIgnoreCase("suffix") || (args[3].equalsIgnoreCase("s"))) {

                            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

                            if (offlinePlayer.isOnline()) {

                                Player player = Bukkit.getServer().getPlayer(args[1]);

                                if (args[4].equalsIgnoreCase("\"\"")) {

                                    plugin.getPermissionsAPI().getPlayerManager().setPlayerSuffix(player.getUniqueId(), "");

                                } else {

                                    if (!offlinePlayer.hasPlayedBefore()) {
                                        sender.sendMessage(ChatColor.RED + " [!] Player has never joined this server");
                                        return true;
                                    }

                                    plugin.getPermissionsAPI().getPlayerManager().setPlayerSuffix(player.getUniqueId(), args[4]);
                                }

                                sender.sendMessage(ChatColor.GREEN + "[!] Player suffix has been changed");

                            } else {

                                if (args[4].equalsIgnoreCase("\"\"")) {

                                    plugin.getPermissionsAPI().getPlayerManager().setPlayerSuffix(offlinePlayer.getUniqueId(), "");

                                } else {

                                    plugin.getPermissionsAPI().getPlayerManager().setPlayerSuffix(offlinePlayer.getUniqueId(), args[4]);
                                }

                                sender.sendMessage(ChatColor.GREEN + "[!] Player suffix has been changed");
                            }
                        }
                    } else if (args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("a")) {

                        if (args[3].equalsIgnoreCase("perm") || args[3].equalsIgnoreCase("p")) {

                            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

                            if (offlinePlayer.isOnline()) {

                                Player player = Bukkit.getServer().getPlayer(args[1]);

                                plugin.getPermissionsAPI().getPlayerManager().addPlayerPermission(player, args[4]);

                                sender.sendMessage(ChatColor.GREEN + "[!] Player permission has been added");

                            } else {

                                if (!offlinePlayer.hasPlayedBefore()) {
                                    sender.sendMessage(ChatColor.RED + "[!] Player has never joined this server");
                                    return true;
                                }

                                plugin.getPermissionsAPI().getPlayerManager().addPlayerPermission(offlinePlayer, args[4]);

                                sender.sendMessage(ChatColor.GREEN + "[!] Player permission has been added");
                            }
                        }
                    } else if (args[2].equalsIgnoreCase("remove") || args[2].equalsIgnoreCase("r")) {

                        if (args[3].equalsIgnoreCase("perm") || (args[3].equalsIgnoreCase("p"))) {

                            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

                            if (offlinePlayer.isOnline()) {

                                Player player = Bukkit.getServer().getPlayer(args[1]);

                                plugin.getPermissionsAPI().getPlayerManager().removePlayerPermission(player, args[4]);

                                sender.sendMessage(ChatColor.GREEN + "[!] Player permission has been removed");

                            } else {

                                if (!offlinePlayer.hasPlayedBefore()) {
                                    sender.sendMessage(ChatColor.RED + "[!] Player has never joined this server");
                                    return true;
                                }

                                plugin.getPermissionsAPI().getPlayerManager().removePlayerPermission(offlinePlayer, args[4]);

                                sender.sendMessage(ChatColor.GREEN + "[!] Player permission has been removed");
                            }
                        }
                    }
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "[!] You may not execute this command.");
        }
        return true;
    }
}