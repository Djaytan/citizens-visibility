package fr.voltariuss.bukkit.citizens_visibility.controller.implementation;

import com.google.common.base.Preconditions;
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
import javax.inject.Inject;
import javax.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CitizenVisibilityControllerImpl implements CitizenVisibilityController {

  private final CitizenVisibilityMessage citizenVisibilityMessage;
  private final CitizenVisibilityService citizenVisibilityService;
  private final CommonMessage commonMessage;
  private final MessageController messageController;
  private final PlayerService playerService;
  private final Server server;

  @Inject
  public CitizenVisibilityControllerImpl(
      @NotNull CitizenVisibilityMessage citizenVisibilityMessage,
      @NotNull CitizenVisibilityService citizenVisibilityService,
      @NotNull CommonMessage commonMessage,
      @NotNull MessageController messageController,
      @NotNull PlayerService playerService,
      @NotNull Server server) {
    this.citizenVisibilityMessage = citizenVisibilityMessage;
    this.citizenVisibilityService = citizenVisibilityService;
    this.commonMessage = commonMessage;
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

    Optional<UUID> playerUuidOptional = uuidFromPlayerName(playerName);

    if (playerUuidOptional.isEmpty()) {
      messageController.sendErrorMessage(audience, commonMessage.playerNotFound(playerName));
      return;
    }

    UUID playerUuid = playerUuidOptional.get();

    Optional<CitizenVisibility> citizenVisibility =
        citizenVisibilityService.fetch(playerUuid, citizenId);

    if (citizenVisibility.isPresent()
        && citizenVisibility.get().isCitizenVisible() == isCitizenVisible) {
      messageController.sendFailureMessage(
          audience,
          citizenVisibilityMessage.visibilityNotChanged(playerName, citizenId, isCitizenVisible));
      return;
    }

    if (isCitizenVisible) {
      citizenVisibilityService.showCitizen(playerUuid, citizenId);
    } else {
      citizenVisibilityService.hideCitizen(playerUuid, citizenId);
    }

    messageController.sendInfoMessage(
        audience,
        citizenVisibilityMessage.visibilityToggled(playerName, citizenId, isCitizenVisible));
  }

  private void toggleCitizenVisibilityForAllPlayers(
      @NotNull Audience audience, int citizenId, boolean isCitizenVisible) {
    Preconditions.checkNotNull(audience);

    if (isCitizenVisible) {
      citizenVisibilityService.showCitizenForAllPlayers(citizenId);
    } else {
      citizenVisibilityService.hideCitizenForAllPlayers(citizenId);
    }

    messageController.sendInfoMessage(
        audience,
        citizenVisibilityMessage.visibilityToggledForAllPlayers(citizenId, isCitizenVisible));
  }

  private @NotNull Optional<UUID> uuidFromPlayerName(@NotNull String playerName) {
    // Search through online players
    org.bukkit.entity.Player onlinePlayer = server.getPlayerExact(playerName);

    if (onlinePlayer != null) {
      return Optional.of(onlinePlayer.getUniqueId());
    }

    // Search through user cache's server
    OfflinePlayer offlinePlayer = server.getOfflinePlayerIfCached(playerName);

    if (offlinePlayer != null) {
      return Optional.of(offlinePlayer.getUniqueId());
    }

    // Search through plugin's data source
    Optional<Player> player = playerService.fetchFromName(playerName);

    if (player.isPresent()) {
      return Optional.of(player.get().playerUuid());
    }

    return Optional.empty();
  }
}
