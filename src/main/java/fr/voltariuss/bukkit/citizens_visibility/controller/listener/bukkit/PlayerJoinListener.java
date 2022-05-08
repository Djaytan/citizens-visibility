package fr.voltariuss.bukkit.citizens_visibility.controller.listener.bukkit;

import fr.voltariuss.bukkit.citizens_visibility.controller.api.PlayerController;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PlayerJoinListener implements Listener {

  private final PlayerController playerController;

  @Inject
  public PlayerJoinListener(@NotNull PlayerController playerController) {
    this.playerController = playerController;
  }

  @EventHandler
  public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
    Player player = event.getPlayer();
    playerController.registerPlayerOrUpdateName(player.getUniqueId(), player.getName());
  }
}
