# citizens-visibility
![Target](https://img.shields.io/badge/plugin-Minecraft-blueviolet)
![Minecraft version](https://img.shields.io/badge/version-1.18.2-blue)

An extension of the Bukkit plugin [Citizens](https://www.spigotmc.org/resources/citizens.13811/) which permit to show conditionally NPCs.

## Compatibility & Tested version

This plugin use the Bukkit API maintained by the [Spigot team](https://www.spigotmc.org/) and which
can be found [here](https://hub.spigotmc.org/javadocs/bukkit/).

No tests have been done with the Spigot's Bukkit implementation. Today it's best to turn to
[PaperMC](https://papermc.io/) implementation which give enhanced performances and add some useful
additional tools.

**Tested version for this plugin :** 1.18.x

## How it works

By default, any created NPC is visible to players. When it comes the need to hide them, two ways are provided by this plugin:
* Changing the visibility of a given NPC for a specific player ;
* Changing the default visibility for a given NPC to all players.

This result is achieved by blocking some packets dispatched by the Minecraft server when a living entity must be created in the client side. The behavior of blocking or not a packet is achieved by storing the state of visibility for a given NPC in database (here SQLite).

## Commands

            Command             |         Permission         |              Description              
------------------------------- | -------------------------- | --------------------------------------
/cv hide <npc_id> <playername>  | citizensvisibility.hide    | Hide a given NPC for a specific player
/cv show <npc_id> <playername>  | citizensvisibility.show    | Show a given NPC for a specific player
/cv hideall <npc_id>            | citizensvisibility.hideall | Hide a given NPC for all players
/cv showall <npc_id>            | citizensvisibility.showall | Show a given NPC for all players

## Setup

First, you need to have set up a Minecraft server with any Bukkit implementation which correspond to
PaperMC or a forked version like Purpur. You can find help about this with the
[PaperMC documentation](https://docs.papermc.io/paper/getting-started).

When it's done, simply download the latest `.jar` file in the
[release section](https://github.com/Djaytan/mc-blacklisted-enchantments/releases/) of this
repository and put it into the `plugins/` folder, and you'll be done! After starting the server,
the plugin should now appear green in the list displayed by the `/plugins` command.

## Licence

This project is under the licence GNU GPLv3.
