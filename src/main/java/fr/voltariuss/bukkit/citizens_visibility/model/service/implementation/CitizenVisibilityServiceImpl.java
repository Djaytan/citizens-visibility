package fr.voltariuss.bukkit.citizens_visibility.model.service.implementation;

import fr.voltariuss.bukkit.citizens_visibility.RemakeBukkitLogger;
import fr.voltariuss.bukkit.citizens_visibility.model.dao.CitizenVisibilityDao;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.CitizenVisibility;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.Player;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.CitizenVisibilityService;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.PlayerService;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.response.CitizenVisibilityFetchResponse;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.response.CitizenVisibilityResponse;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.response.CitizenVisibilityResponse.ResponseType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.PersistenceException;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CitizenVisibilityServiceImpl implements CitizenVisibilityService {

  private final CitizenVisibilityDao citizenVisibilityDao;
  private final RemakeBukkitLogger logger;
  private final PlayerService playerService;

  @Inject
  public CitizenVisibilityServiceImpl(
      @NotNull CitizenVisibilityDao citizenVisibilityDao,
      @NotNull RemakeBukkitLogger logger,
      @NotNull PlayerService playerService) {
    this.citizenVisibilityDao = citizenVisibilityDao;
    this.logger = logger;
    this.playerService = playerService;
  }

  @Override
  public @NotNull CitizenVisibilityFetchResponse find(@NotNull UUID playerUuid, int citizenId) {
    if (playerUuid == null) {
      return CitizenVisibilityFetchResponse.builder()
          .responseType(ResponseType.BAD_INPUT)
          .failureMessage("Player's UUID must be specified.")
          .build();
    }

    try {
      Optional<CitizenVisibility> citizenVisibility =
          citizenVisibilityDao.find(playerUuid, citizenId);

      return CitizenVisibilityFetchResponse.builder()
          .responseType(ResponseType.SUCCESS)
          .citizenVisibility(citizenVisibility.orElse(null))
          .build();
    } catch (PersistenceException e) {
      logger.error(
          String.format(
              "Something went wrong when fetching citizen visibility for the player's UUID %s",
              playerUuid),
          e);
      return CitizenVisibilityFetchResponse.builder()
          .responseType(ResponseType.SERVER_ERROR)
          .failureMessage("")
          .build();
    }
  }

  @Override
  public @NotNull CitizenVisibilityResponse hideCitizen(@NotNull UUID playerUuid, int citizenId) {
    return toggleCitizenVisibility(playerUuid, citizenId, false);
  }

  @Override
  public @NotNull CitizenVisibilityResponse hideCitizen(int citizenId) {
    return toggleCitizenVisibility(citizenId, false);
  }

  @Override
  public @NotNull CitizenVisibilityResponse showCitizen(@NotNull UUID playerUuid, int citizenId) {
    return toggleCitizenVisibility(playerUuid, citizenId, true);
  }

  @Override
  public @NotNull CitizenVisibilityResponse showCitizen(int citizenId) {
    return toggleCitizenVisibility(citizenId, true);
  }

  private @NotNull CitizenVisibilityResponse toggleCitizenVisibility(
      @NotNull UUID playerUuid, int citizenId, boolean isCitizenVisible) {
    if (playerUuid == null) {
      return CitizenVisibilityResponse.builder()
          .responseType(ResponseType.BAD_INPUT)
          .failureMessage("The player UUID must be specified.")
          .build();
    }

    try {
      CitizenVisibilityFetchResponse citizenVisibilityFetchResponse = find(playerUuid, citizenId);

      if (citizenVisibilityFetchResponse.isFailure()) {
        return CitizenVisibilityResponse.builder()
            .responseType(citizenVisibilityFetchResponse.responseType())
            .failureMessage(citizenVisibilityFetchResponse.failureMessage())
            .build();
      }

      if (citizenVisibilityFetchResponse.isValueEmpty()) {
        CitizenVisibility cv = new CitizenVisibility(playerUuid);
        cv.citizenId(citizenId);
        cv.isCitizenVisible(isCitizenVisible);
        citizenVisibilityDao.persist(cv);
      } else {
        CitizenVisibility cv = citizenVisibilityFetchResponse.citizenVisibility();
        cv.isCitizenVisible(isCitizenVisible);
        citizenVisibilityDao.update(cv);
      }
    } catch (PersistenceException e) {
      logger.error(
          String.format(
              "Something went wrong when toggling citizen visibility for the player's UUID %s",
              playerUuid),
          e);
      return CitizenVisibilityResponse.builder()
          .responseType(ResponseType.SERVER_ERROR)
          .failureMessage(
              String.format(
                  "Failed to toggling citizen visibility for player's UUID %s.", playerUuid))
          .build();
    }

    return CitizenVisibilityResponse.builder().responseType(ResponseType.SUCCESS).build();
  }

  private @NotNull CitizenVisibilityResponse toggleCitizenVisibility(
      int citizenId, boolean isCitizenVisible) {
    List<Player> players = playerService.findAll();
    List<CitizenVisibility> citizenVisibilities = new ArrayList<>(players.size());

    try {
      for (Player player : players) {
        UUID playerUuid = player.playerUuid();
        Optional<CitizenVisibility> fetchedCv = citizenVisibilityDao.find(playerUuid, citizenId);

        if (fetchedCv.isEmpty()) {
          CitizenVisibility citizenVisibility = new CitizenVisibility(playerUuid);
          citizenVisibility.citizenId(citizenId);
          citizenVisibility.isCitizenVisible(isCitizenVisible);
          citizenVisibilityDao.persist(citizenVisibility);
          citizenVisibilities.add(citizenVisibility);
        }
      }

      for (CitizenVisibility citizenVisibility : citizenVisibilities) {
        citizenVisibility.isCitizenVisible(isCitizenVisible);
        citizenVisibilityDao.update(citizenVisibility);
      }
    } catch (PersistenceException e) {
      logger.error(
          "Something went wrong when toggling citizen visibility for all registered players.", e);
      return CitizenVisibilityResponse.builder()
          .responseType(ResponseType.SERVER_ERROR)
          .failureMessage("Failed to toggling citizen visibility for all registered players.")
          .build();
    }

    return CitizenVisibilityResponse.builder().responseType(ResponseType.SUCCESS).build();
  }
}
