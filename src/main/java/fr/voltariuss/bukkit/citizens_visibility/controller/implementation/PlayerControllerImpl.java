package fr.voltariuss.bukkit.citizens_visibility.controller.implementation;

import com.google.common.base.Preconditions;
import fr.voltariuss.bukkit.citizens_visibility.RemakeBukkitLogger;
import fr.voltariuss.bukkit.citizens_visibility.controller.api.PlayerController;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.CitizenVisibilityService;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.PlayerService;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.parameter.PlayerRegisterResponse;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.parameter.PlayerRegisterResponse.ResponseType;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PlayerControllerImpl implements PlayerController {

  private final CitizenVisibilityService citizenVisibilityService;
  private final RemakeBukkitLogger logger;
  private final PlayerService playerService;

  @Inject
  public PlayerControllerImpl(
      @NotNull CitizenVisibilityService citizenVisibilityService,
      @NotNull RemakeBukkitLogger logger,
      @NotNull PlayerService playerService) {
    this.citizenVisibilityService = citizenVisibilityService;
    this.logger = logger;
    this.playerService = playerService;
  }

  @Override
  public void registerPlayerOrUpdateName(@NotNull UUID playerUuid, @NotNull String playerName) {
    Preconditions.checkNotNull(playerUuid);
    Preconditions.checkNotNull(playerName);
    Preconditions.checkArgument(!playerName.isBlank());

    CompletableFuture.runAsync(
        () -> {
          PlayerRegisterResponse response =
              playerService.registerOrUpdateName(playerUuid, playerName).join();

          if (response.responseType() == ResponseType.PLAYER_REGISTERED) {
            citizenVisibilityService.registerDefaultVisibilities(playerUuid, true);
            logger.info("Successfully registered player '{}' ({})", playerName, playerUuid);
            return;
          }

          if (response.responseType() == ResponseType.PLAYER_NAME_UPDATED) {
            logger.info(
                "Updated player name from '{}' to '{}'",
                response.oldPlayerName(),
                response.newPlayerName());
          }
        });
  }
}
