package me.willis.permissions.command;

import me.willis.permissions.Permissions;
import me.willis.permissions.util.GroupManager;
import me.willis.permissions.util.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor {

    private Permissions plugin;
    private GroupManager groupManager;
    private PlayerManager playerManager;

    public Command(Permissions plugin) {
        this.plugin = plugin;
        this.groupManager = new GroupManager(plugin);
        this.playerManager = new PlayerManager(plugin);
    }

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender.hasPermission("permissions.admin")) {
            if (args.length == 0) {

                sender.sendMessage(ChatColor.YELLOW + "Group Commands:");
                sender.sendMessage(ChatColor.YELLOW + "  -> p groups");
                sender.sendMessage(ChatColor.YELLOW + "  -> p group create (group)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p group delete (group)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p group (group) set prefix (prefix)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p group (group) set suffix (suffix)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p group (group) add perm (permission)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p group (group) remove perm (permission)");
                sender.sendMessage("");
                sender.sendMessage(ChatColor.YELLOW + "Player Commands:");
                sender.sendMessage(ChatColor.YELLOW + "  -> p player (player) group set (group)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p player (player) set prefix (prefix)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p player (player) set suffix (suffix)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p player (player) add perm (permission)");
                sender.sendMessage(ChatColor.YELLOW + "  -> p player (player) remove perm (permission)");
                return true;
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("groups")) {
                    groupManager.getGroups(sender);
                    return true;
                }
            }

            if (args[0].equalsIgnoreCase("group")) {
                if (args.length == 3) {

                    if (args[1].equalsIgnoreCase("create")) {

                        if (groupManager.isGroupCreated(args[2])) {
                            sender.sendMessage(ChatColor.RED + "Error:");
                            sender.sendMessage(ChatColor.RED + "  -> The group has already been created");
                            return true;
                        }

                        groupManager.createGroup(args[2]);

                        sender.sendMessage(ChatColor.GREEN + "Success:");
                        sender.sendMessage(ChatColor.GREEN + "  -> Group has been created");

                    } else if (args[1].equalsIgnoreCase("delete")) {

                        if (groupManager.getDefaultGroup().equalsIgnoreCase(args[2].toLowerCase())) {
                            sender.sendMessage(ChatColor.RED + "Error:");
                            sender.sendMessage(ChatColor.RED + "  -> You can't delete the default group");
                            return false;
                        }

                        if (!groupManager.isGroupCreated(args[2])) {
                            sender.sendMessage(ChatColor.RED + "Error:");
                            sender.sendMessage(ChatColor.RED + "  -> The group has yet to be created");
                            return true;
                        }

                        groupManager.deleteGroup(args[2]);

                        sender.sendMessage(ChatColor.GREEN + "Success:");
                        sender.sendMessage(ChatColor.GREEN + "  -> Group has been deleted");
                    }

                } else if (args.length == 5) {

                    if (!groupManager.isGroupCreated(args[1])) {
                        sender.sendMessage(ChatColor.RED + "Error:");
                        sender.sendMessage(ChatColor.RED + "  -> The group doesn't exist");
                        return true;
                    }

                    if (args[2].equalsIgnoreCase("set")) {

                        if (args[3].equalsIgnoreCase("prefix")) {

                            if (args[4].equalsIgnoreCase("\"\"")) {

                                groupManager.setPrefix(args[1], "");

                            } else {

                                groupManager.setPrefix(args[1], args[4]);
                            }

                            sender.sendMessage(ChatColor.GREEN + "Success:");
                            sender.sendMessage(ChatColor.GREEN + "  -> Group prefix has been changed");

                        } else if (args[3].equalsIgnoreCase("suffix")) {

                            if (args[4].equalsIgnoreCase("\"\"")) {

                                groupManager.setSuffix(args[1], "");

                            } else {

                                groupManager.setSuffix(args[1], args[4]);
                            }

                            sender.sendMessage(ChatColor.GREEN + "Success:");
                            sender.sendMessage(ChatColor.GREEN + "  -> Group suffix has been changed");
                        }
                    } else if (args[2].equalsIgnoreCase("add")) {

                        if (args[3].equalsIgnoreCase("perm")) {

                            groupManager.addPermission(args[1], args[4]);

                            sender.sendMessage(ChatColor.GREEN + "Success:");
                            sender.sendMessage(ChatColor.GREEN + "  -> Group permission has been added");

                        }
                    } else if (args[2].equalsIgnoreCase("remove")) {

                        if (args[3].equalsIgnoreCase("perm")) {

                            groupManager.removePermission(args[1], args[4]);

                            sender.sendMessage(ChatColor.GREEN + "Success:");
                            sender.sendMessage(ChatColor.GREEN + "  -> Group permission has been removed");

                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("player")) {

                if (args.length == 5) {

                    if (args[2].equalsIgnoreCase("group")) {

                        if (args[3].equalsIgnoreCase("set")) {

                            if (!groupManager.isGroupCreated(args[4])) {
                                sender.sendMessage(ChatColor.RED + "Error:");
                                sender.sendMessage(ChatColor.RED + "  -> The group doesn't exist");
                                return true;
                            }

                            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

                            if (!offlinePlayer.hasPlayedBefore()) {
                                sender.sendMessage(ChatColor.RED + "Error:");
                                sender.sendMessage(ChatColor.RED + "  -> Player has never joined this server");
                                return true;
                            }

                            if (offlinePlayer.isOnline()) {

                                Player player = Bukkit.getServer().getPlayer(args[1]);

                                groupManager.setPlayerGroup(args[4], player);

                                sender.sendMessage(ChatColor.GREEN + "Success:");
                                sender.sendMessage(ChatColor.GREEN + "  -> Player group has been changed");

                            } else {

                                groupManager.setPlayerGroup(args[4], offlinePlayer);

                                sender.sendMessage(ChatColor.GREEN + "Success:");
                                sender.sendMessage(ChatColor.GREEN + "  -> Player group has been changed");
                            }
                        }
                    } else if (args[2].equalsIgnoreCase("set")) {

                        if (args[3].equalsIgnoreCase("prefix")) {

                            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

                            if (!offlinePlayer.hasPlayedBefore()) {
                                sender.sendMessage(ChatColor.RED + "Error:");
                                sender.sendMessage(ChatColor.RED + "  -> Player has never joined this server");
                                return true;
                            }

                            if (offlinePlayer.isOnline()) {

                                Player player = Bukkit.getServer().getPlayer(args[1]);

                                if (args[4].equalsIgnoreCase("\"\"")) {

                                    playerManager.setPrefix(player.getUniqueId(), "");

                                } else {

                                    playerManager.setPrefix(player.getUniqueId(), args[4]);
                                }

                                sender.sendMessage(ChatColor.GREEN + "Success:");
                                sender.sendMessage(ChatColor.GREEN + "  -> Player prefix has been changed");

                            } else {

                                if (args[4].equalsIgnoreCase("\"\"")) {

                                    playerManager.setPrefix(offlinePlayer.getUniqueId(), "");

                                } else {

                                    playerManager.setPrefix(offlinePlayer.getUniqueId(), args[4]);
                                }

                                sender.sendMessage(ChatColor.GREEN + "Success:");
                                sender.sendMessage(ChatColor.GREEN + "  -> Player prefix has been changed");
                            }
                        } else if (args[3].equalsIgnoreCase("suffix")) {

                            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

                            if (!offlinePlayer.hasPlayedBefore()) {
                                sender.sendMessage(ChatColor.RED + "Error:");
                                sender.sendMessage(ChatColor.RED + "  -> Player has never joined this server");
                                return true;
                            }

                            if (offlinePlayer.isOnline()) {

                                Player player = Bukkit.getServer().getPlayer(args[1]);

                                if (args[4].equalsIgnoreCase("\"\"")) {

                                    playerManager.setSuffix(player.getUniqueId(), "");

                                } else {

                                    playerManager.setSuffix(player.getUniqueId(), args[4]);
                                }

                                sender.sendMessage(ChatColor.GREEN + "Success:");
                                sender.sendMessage(ChatColor.GREEN + "  -> Player suffix has been changed");

                            } else {

                                if (args[4].equalsIgnoreCase("\"\"")) {

                                    playerManager.setSuffix(offlinePlayer.getUniqueId(), "");

                                } else {

                                    playerManager.setSuffix(offlinePlayer.getUniqueId(), args[4]);
                                }

                                sender.sendMessage(ChatColor.GREEN + "Success:");
                                sender.sendMessage(ChatColor.GREEN + "  -> Player suffix has been changed");
                            }
                        }
                    } else if (args[2].equalsIgnoreCase("add")) {

                        if (args[3].equalsIgnoreCase("perm")) {

                            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

                            if (!offlinePlayer.hasPlayedBefore()) {
                                sender.sendMessage(ChatColor.RED + "Error:");
                                sender.sendMessage(ChatColor.RED + "  -> Player has never joined this server");
                                return true;
                            }

                            if (offlinePlayer.isOnline()) {

                                Player player = Bukkit.getServer().getPlayer(args[1]);

                                playerManager.addPermission(player, args[4]);

                                sender.sendMessage(ChatColor.GREEN + "Success:");
                                sender.sendMessage(ChatColor.GREEN + "  -> Player permission has been added");

                            } else {

                                playerManager.addPermission(offlinePlayer, args[4]);

                                sender.sendMessage(ChatColor.GREEN + "Success:");
                                sender.sendMessage(ChatColor.GREEN + "  -> Player permission has been added");
                            }
                        }
                    } else if (args[2].equalsIgnoreCase("remove")) {

                        if (args[3].equalsIgnoreCase("perm")) {

                            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(args[1]);

                            if (!offlinePlayer.hasPlayedBefore()) {
                                sender.sendMessage(ChatColor.RED + "Error:");
                                sender.sendMessage(ChatColor.RED + "  -> Player has never joined this server");
                                return true;
                            }

                            if (offlinePlayer.isOnline()) {

                                Player player = Bukkit.getServer().getPlayer(args[1]);

                                playerManager.removePermission(player, args[4]);

                                sender.sendMessage(ChatColor.GREEN + "Success:");
                                sender.sendMessage(ChatColor.GREEN + "  -> Player permission has been removed");

                            } else {

                                playerManager.removePermission(offlinePlayer, args[4]);

                                sender.sendMessage(ChatColor.GREEN + "Success:");
                                sender.sendMessage(ChatColor.GREEN + "  -> Player permission has been removed");
                            }
                        }
                    }
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You may not execute this command.");
        }
        return false;
    }
}