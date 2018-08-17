# Permissions
Global ranks, per-server permissions, simple and easy!

## How to use
<br>

There is only a single permission a player needs to gain access to all commands

|Permissions|Description|
|-----------|-----------|
|permissions.admin|Gives access to all group & player commands|

<br>

These are all the group commands. Here you can create & remove groups, set the groups prefix & suffix, as well as add and remove permissions.

|Group Commands|Description|
|-------|-----------|
|/p groups|Shows all created groups|
|/p group create (group)|Create a new group|
|/p group delete (group)|Delete the selected group|
|/p group (group) set prefix (prefix)|Set a prefix for the selected group|
|/p group (group) set suffix (suffix)|Set a suffix for the selected group|
|/p group (group) add perm (permission)|Add a permission to the selected group|
|/p group (group) remove perm (permission)|Remove a permission from the selected group|

<br>

These are all the player commands. Here you can change a players group, set their prefix & suffix as well as add and remove permissions.


|Player Commands|Description|
|---------------|-----------|
|/p player (player) group set (group)|Change the players group|
|/p player (player) set prefix (prefix)|Set a player prefix|
|/p player (player) set suffix (suffix)|Set a player suffix|
|/p player (player) add perm (permission)|Add a permission to the player|
|/p player (player) remove permm (permissions)|Remove a permission from the player|

<br>

## Seems like a lot of typing? Well, you'll enjoy this section!
<br>


These commands will output the same result as the non-shortened version. This is just a simple solution to allow for faster group & player modification (and less typing)

|Group Commands Shortened|Description|
|-------|-----------|
|/p gs|Shows all created groups|
|/p g c (group)|Create a new group|
|/p g d (group)|Delete the selected group|
|/p g (group) s p (prefix)|Set a prefix for the selected group|
|/p g (group) s s (suffix)|Set a suffix for the selected group|
|/p g (group) a p (permission)|Add a permission to the selected group|
|/p g (group) r p (permission)|Remove a permission from the selected group|

<br>

Hope you enjoy this feature :)

|Player Commands Shortened|Description|
|---------------|-----------|
|/p p (player) g s (group)|Change the players group|
|/p p (player) s p (prefix)|Set a player prefix|
|/p p (player) s s (suffix)|Set a player suffix|
|/p p (player) a p (permission)|Add a permission to the player|
|/p p (player) r p (permissions)|Remove a permission from the player|

<br>

## Prefixes & Suffixes

If you'd like to set a group or players prefix to nothing simply put "" as the prefix and it will set it to nothing!

|Example|
|-------|
|/p group (group) set prefix ""|

<br>

## Group Syncing 
Yes, this plugin features group syncing. When a players group is changed from a different server (let's assume from the hub) but the player is currently playing on the survival server connected in a Bungee-cord network the plugin will see this group change and update the player's group and their permissions. Same goes for group creation and deletion. If a group is created every other server will know of this new group and simply create it on every other server running the plugin. If a group gets deleted and a player online is within the group that was deleted the player's group will change to the default group and their permissions will be changed to the default groups permission.

<br>

### Configuration

```# MySQL Server Host
Host: "localhost"

# MySQL Server Port
Port: "3306"

# MySQL Server Database
Database: "database"

# MySQL Server Username
Username: "username"

# MySQL Server Password
Password: "password"

# This is the default group that will be created when the plugin first starts up.
# Please note that prefix's & suffix's are not set by default!
DefaultGroup: "member"

# This time is measure in SECONDS! This time is how often the plugin will sync groups.
# For example, if a group was deleted on your hub server then the rest of the servers connected to the
# same database will realize this group deletion and delete the same group that was deleted on the hub.
UpdateTime: 5

# This is the chat format available to you! Here you can customize the output of in-game
# chat to make it suit your style. (Colour Codes are supported)

# Available Placeholders:
#  | ${WORLD} - Grab the world the player is in
#  | ${PLAYER} - Grab the player's name
#  | ${DISPLAYNAME} - Grab the player's display name
#  | ${PREFIX} - Grab the player's prefix
#  | ${SUFFIX} - Grab the player's suffix
#  | ${MESSAGE} - Grab the player's message

ChatFormat: '[${WORLD}] ${PREFIX} ${PLAYER} ${SUFFIX}: ${MESSAGE}'
```

### Please note
You can use this plugin for only a single non-bungee'd server although it's true purpose is meant for bungee'd servers.

