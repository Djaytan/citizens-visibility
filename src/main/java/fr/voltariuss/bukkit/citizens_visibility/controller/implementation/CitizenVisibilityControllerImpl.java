package fr.voltariuss.bukkit.citizens_visibility.controller.implementation;

import com.google.common.base.Preconditions;
import fr.voltariuss.bukkit.citizens_visibility.RemakeBukkitLogger;
import fr.voltariuss.bukkit.citizens_visibility.controller.api.CitizenVisibilityController;
import fr.voltariuss.bukkit.citizens_visibility.controller.api.MessageController;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.CitizenVisibility;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.Player;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.CitizenVisibilityService;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.PlayerService;
import fr.voltariuss.bukkit.citizens_visibility.view.CitizenVisibilityMessage;
import fr.voltariuss.bukkit.citizens_visibility.view.CommonMessage;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CitizenVisibilityControllerImpl implements CitizenVisibilityController {

  private final CitizenVisibilityMessage citizenVisibilityMessage;
  private final CitizenVisibilityService citizenVisibilityService;
  private final CommonMessage commonMessage;
  private final RemakeBukkitLogger logger;
  private final Executor mainThreadExecutor;
  private final MessageController messageController;
  private final PlayerService playerService;
  private final Server server;

  @Inject
  public CitizenVisibilityControllerImpl(
      @NotNull CitizenVisibilityMessage citizenVisibilityMessage,
      @NotNull CitizenVisibilityService citizenVisibilityService,
      @NotNull CommonMessage commonMessage,
      @NotNull RemakeBukkitLogger logger,
      @NotNull Executor mainThreadExecutor,
      @NotNull MessageController messageController,
      @NotNull PlayerService playerService,
      @NotNull Server server) {
    this.citizenVisibilityMessage = citizenVisibilityMessage;
    this.citizenVisibilityService = citizenVisibilityService;
    this.commonMessage = commonMessage;
    this.logger = logger;
    this.mainThreadExecutor = mainThreadExecutor;
    this.messageController = messageController;
    this.playerService = playerService;
    this.server = server;
  }

  @Override
  public void hideCitizen(@NotNull Audience audience, @NotNull String playerName, int citizenId) {
    toggleCitizenVisibility(audience, playerName, citizenId, false);
  }

  @Override
  public void hideCitizenForAllPlayers(@NotNull Audience audience, int citizenId) {
    toggleCitizenVisibilityForAllPlayers(audience, citizenId, false);
  }

  @Override
  public void showCitizen(@NotNull Audience audience, @NotNull String playerName, int citizenId) {
    toggleCitizenVisibility(audience, playerName, citizenId, true);
  }

  @Override
  public void showCitizenForAllPlayers(@NotNull Audience audience, int citizenId) {
    toggleCitizenVisibilityForAllPlayers(audience, citizenId, true);
  }

  private void toggleCitizenVisibility(
      @NotNull Audience audience,
      @NotNull String playerName,
      int citizenId,
      boolean isCitizenVisible) {
    Preconditions.checkNotNull(audience);
    Preconditions.checkNotNull(playerName);
    Preconditions.checkArgument(!playerName.isBlank());

    CompletableFuture.runAsync(
        () -> {
          NPC npc = CitizensAPI.getNPCRegistry().getById(citizenId);

          if (npc == null) {
            messageController.sendFailureMessage(
                audience, citizenVisibilityMessage.citizenNotFound(citizenId));
            return;
          }

          Optional<UUID> playerUuidOptional = uuidFromPlayerName(playerName).join();

          if (playerUuidOptional.isEmpty()) {
            messageController.sendFailureMessage(
                audience, commonMessage.playerNotFound(playerName));
            return;
          }

          UUID playerUuid = playerUuidOptional.get();

          Optional<CitizenVisibility> citizenVisibility =
              citizenVisibilityService.fetch(playerUuid, citizenId).join();

          if (citizenVisibility.isPresent()
              && citizenVisibility.get().isCitizenVisible() == isCitizenVisible) {
            messageController.sendFailureMessage(
                audience,
                citizenVisibilityMessage.visibilityNotChanged(
                    playerName, citizenId, npc.getName(), isCitizenVisible));
            return;
          }

          if (isCitizenVisible) {
            citizenVisibilityService.showCitizen(playerUuid, citizenId);
          } else {
            citizenVisibilityService.hideCitizen(playerUuid, citizenId);
          }

          updateCitizenVisibilities(npc);

          messageController.sendInfoMessage(
              audience,
              citizenVisibilityMessage.visibilityToggled(
                  playerName, citizenId, npc.getName(), isCitizenVisible));

          logger.info(
              "Toggle visibility of the citizen with ID {} ({}) for the player {}:"
                  + " newVisibility={}",
              citizenId,
              npc.getName(),
              playerName,
              isCitizenVisible);
        });
  }

  private void toggleCitizenVisibilityForAllPlayers(
      @NotNull Audience audience, int citizenId, boolean isCitizenVisible) {
    Preconditions.checkNotNull(audience);

    CompletableFuture.runAsync(
        () -> {
          NPC npc =
              CompletableFuture.supplyAsync(
                      () -> CitizensAPI.getNPCRegistry().getById(citizenId), mainThreadExecutor)
                  .join();

          if (npc == null) {
            messageController.sendFailureMessage(
                audience, citizenVisibilityMessage.citizenNotFound(citizenId));
            return;
          }

          if (isCitizenVisible) {
            citizenVisibilityService.showCitizenForAllPlayers(citizenId);
          } else {
            citizenVisibilityService.hideCitizenForAllPlayers(citizenId);
          }
          // TODO: merge show/hideAll methods with defineDefaultVisibility one

          updateCitizenVisibilities(npc);
          citizenVisibilityService.defineDefaultVisibility(citizenId, isCitizenVisible);

          messageController.sendInfoMessage(
              audience,
              citizenVisibilityMessage.visibilityToggledForAllPlayers(
                  citizenId, npc.getName(), isCitizenVisible));

          logger.info(
              "Toggle visibility of the citizen with ID {} ({}) for all players: newVisibility={}",
              citizenId,
              npc.getName(),
              isCitizenVisible);
        });
  }

  private @NotNull CompletableFuture<Optional<UUID>> uuidFromPlayerName(
      @NotNull String playerName) {
    return CompletableFuture.supplyAsync(
        () -> {
          // Search through online players
          org.bukkit.entity.Player onlinePlayer =
              CompletableFuture.supplyAsync(
                      () -> server.getPlayerExact(playerName), mainThreadExecutor)
                  .join();

          if (onlinePlayer != null) {
            return Optional.of(onlinePlayer.getUniqueId());
          }

          // Search through plugin's data source (and ignore server cache to prevent sync issues
          // when
          // cache is older than plugin's data source)
          Optional<Player> player = playerService.fetchFromName(playerName).join();

          if (player.isPresent()) {
            return Optional.of(player.get().playerUuid());
          }

          return Optional.empty();
        });
  }

  private void updateCitizenVisibilities(@NotNull NPC npc) {
    CompletableFuture.runAsync(
        () -> {
          npc.despawn(DespawnReason.PLUGIN);
          npc.spawn(npc.getStoredLocation());
        },
        mainThreadExecutor);
  }
}
