package fr.voltariuss.bukkit.citizens_visibility.model.service.implementation;

import com.google.common.base.Preconditions;
import fr.voltariuss.bukkit.citizens_visibility.model.dao.CitizenVisibilityDao;
import fr.voltariuss.bukkit.citizens_visibility.model.dao.DefaultCitizenVisibilityDao;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.CitizenVisibility;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.DefaultCitizenVisibility;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.Player;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.CitizenVisibilityService;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.PlayerService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CitizenVisibilityServiceImpl implements CitizenVisibilityService {

  private static final boolean IS_CITIZEN_VISIBLE_BY_DEFAULT = true;

  private final CitizenVisibilityDao citizenVisibilityDao;
  private final DefaultCitizenVisibilityDao defaultCitizenVisibilityDao;
  private final PlayerService playerService;

  @Inject
  public CitizenVisibilityServiceImpl(
      @NotNull CitizenVisibilityDao citizenVisibilityDao,
      @NotNull DefaultCitizenVisibilityDao defaultCitizenVisibilityDao,
      @NotNull PlayerService playerService) {
    this.citizenVisibilityDao = citizenVisibilityDao;
    this.defaultCitizenVisibilityDao = defaultCitizenVisibilityDao;
    this.playerService = playerService;
  }

  @Override
  public @NotNull CompletableFuture<Optional<CitizenVisibility>> fetch(
      @NotNull UUID playerUuid, int citizenId) {
    Preconditions.checkNotNull(playerUuid);

    return citizenVisibilityDao.find(playerUuid, citizenId);
  }

  @Override
  public @NotNull CompletableFuture<List<CitizenVisibility>> fetchAll(int citizenId) {
    return citizenVisibilityDao.findByCitizenId(citizenId);
  }

  @Override
  public @NotNull CompletableFuture<Void> defineDefaultVisibility(
      int citizenId, boolean isVisibleByDefault) {
    return CompletableFuture.runAsync(
        () -> {
          Optional<DefaultCitizenVisibility> defaultCitizenVisibility =
              defaultCitizenVisibilityDao.findByCitizenId(citizenId).join();

          if (defaultCitizenVisibility.isPresent()) {
            DefaultCitizenVisibility dcv = defaultCitizenVisibility.get();
            dcv.setVisibleByDefault(isVisibleByDefault);
            defaultCitizenVisibilityDao.update(dcv).join();
            return;
          }

          DefaultCitizenVisibility dcv =
              DefaultCitizenVisibility.builder()
                  .citizenId(citizenId)
                  .isVisibleByDefault(isVisibleByDefault)
                  .build();
          defaultCitizenVisibilityDao.persist(dcv).join();
        });
  }

  @Override
  public @NotNull CompletableFuture<Void> hideCitizen(@NotNull UUID playerUuid, int citizenId) {
    return toggleCitizenVisibility(playerUuid, citizenId, false);
  }

  @Override
  public @NotNull CompletableFuture<Void> hideCitizenForAllPlayers(int citizenId) {
    return toggleCitizenVisibilityForAllPlayers(citizenId, false);
  }

  @Override
  public @NotNull CompletableFuture<Void> showCitizen(@NotNull UUID playerUuid, int citizenId) {
    return toggleCitizenVisibility(playerUuid, citizenId, true);
  }

  @Override
  public @NotNull CompletableFuture<Void> showCitizenForAllPlayers(int citizenId) {
    return toggleCitizenVisibilityForAllPlayers(citizenId, true);
  }

  @Override
  public @NotNull CompletableFuture<Boolean> isCitizenVisibleForPlayer(
      @NotNull UUID playerUuid, int citizenId) {
    return CompletableFuture.supplyAsync(
        () -> {
          Optional<CitizenVisibility> citizenVisibility =
              citizenVisibilityDao.find(playerUuid, citizenId).join();

          if (citizenVisibility.isPresent()) {
            return citizenVisibility.get().isCitizenVisible();
          }

          Optional<DefaultCitizenVisibility> defaultCitizenVisibility =
              defaultCitizenVisibilityDao.findByCitizenId(citizenId).join();

          if (defaultCitizenVisibility.isPresent()) {
            return defaultCitizenVisibility.get().isVisibleByDefault();
          }

          return IS_CITIZEN_VISIBLE_BY_DEFAULT;
        });
  }

  private @NotNull CompletableFuture<Void> toggleCitizenVisibility(
      @NotNull UUID playerUuid, int citizenId, boolean isCitizenVisible) {
    Preconditions.checkNotNull(playerUuid);

    return CompletableFuture.runAsync(
        () -> {
          Optional<CitizenVisibility> citizenVisibility =
              citizenVisibilityDao.find(playerUuid, citizenId).join();

          if (citizenVisibility.isEmpty()) {
            CitizenVisibility cv = new CitizenVisibility(playerUuid);
            cv.citizenId(citizenId);
            cv.isCitizenVisible(isCitizenVisible);
            citizenVisibilityDao.persist(cv).join();
            return;
          }

          CitizenVisibility cv = citizenVisibility.get();
          cv.isCitizenVisible(isCitizenVisible);
          citizenVisibilityDao.update(cv).join();
        });
  }

  private @NotNull CompletableFuture<Void> toggleCitizenVisibilityForAllPlayers(
      int citizenId, boolean isCitizenVisible) {
    return CompletableFuture.runAsync(
        () -> {
          List<Player> players = playerService.fetchAll().join();

          List<CitizenVisibility> citizenVisibilities = new ArrayList<>(players.size());

          for (Player player : players) {
            UUID playerUuid = player.playerUuid();
            Optional<CitizenVisibility> fetchedCv =
                citizenVisibilityDao.find(playerUuid, citizenId).join();

            if (fetchedCv.isPresent()) {
              citizenVisibilities.add(fetchedCv.get());
              continue;
            }

            CitizenVisibility citizenVisibility = new CitizenVisibility(playerUuid);
            citizenVisibility.citizenId(citizenId);
            citizenVisibility.isCitizenVisible(isCitizenVisible);
            citizenVisibilityDao.persist(citizenVisibility).join();
          }

          for (CitizenVisibility citizenVisibility : citizenVisibilities) {
            citizenVisibility.isCitizenVisible(isCitizenVisible);
            citizenVisibilityDao.update(citizenVisibility).join();
          }
        });
  }
}
