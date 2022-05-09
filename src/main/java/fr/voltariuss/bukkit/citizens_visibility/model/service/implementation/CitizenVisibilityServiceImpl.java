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
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CitizenVisibilityServiceImpl implements CitizenVisibilityService {

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
  public @NotNull Optional<CitizenVisibility> fetch(@NotNull UUID playerUuid, int citizenId) {
    Preconditions.checkNotNull(playerUuid);

    return citizenVisibilityDao.find(playerUuid, citizenId);
  }

  @Override
  public @NotNull List<CitizenVisibility> fetchAll(int citizenId) {
    return citizenVisibilityDao.findByCitizenId(citizenId);
  }

  @Override
  public void defineDefaultVisibility(int citizenId, boolean isVisibleByDefault) {
    Optional<DefaultCitizenVisibility> defaultCitizenVisibility =
        defaultCitizenVisibilityDao.findByCitizenId(citizenId);

    if (defaultCitizenVisibility.isPresent()) {
      DefaultCitizenVisibility dcv = defaultCitizenVisibility.get();
      dcv.setVisibleByDefault(isVisibleByDefault);
      defaultCitizenVisibilityDao.update(dcv);
      return;
    }

    DefaultCitizenVisibility dcv =
        DefaultCitizenVisibility.builder()
            .citizenId(citizenId)
            .isVisibleByDefault(isVisibleByDefault)
            .build();
    defaultCitizenVisibilityDao.persist(dcv);
  }

  @Override
  public void registerDefaultVisibilities(@NotNull UUID playerUuid, boolean forceDefault) {
    Preconditions.checkNotNull(playerUuid);

    List<DefaultCitizenVisibility> defaultCitizenVisibilities =
        defaultCitizenVisibilityDao.findAll();

    for (DefaultCitizenVisibility defaultCitizenVisibility : defaultCitizenVisibilities) {
      int citizenId = defaultCitizenVisibility.getCitizenId();

      Optional<CitizenVisibility> citizenVisibility =
          citizenVisibilityDao.find(playerUuid, citizenId);

      if (citizenVisibility.isPresent() && forceDefault) {
        citizenVisibility.get().isCitizenVisible(defaultCitizenVisibility.isVisibleByDefault());
        citizenVisibilityDao.update(citizenVisibility.get());
        continue;
      }

      if (citizenVisibility.isEmpty()) {
        CitizenVisibility newCv = new CitizenVisibility(playerUuid);
        newCv.citizenId(citizenId);
        newCv.isCitizenVisible(defaultCitizenVisibility.isVisibleByDefault());
        citizenVisibilityDao.persist(newCv);
      }
    }
  }

  @Override
  public void hideCitizen(@NotNull UUID playerUuid, int citizenId) {
    toggleCitizenVisibility(playerUuid, citizenId, false);
  }

  @Override
  public void hideCitizenForAllPlayers(int citizenId) {
    toggleCitizenVisibilityForAllPlayers(citizenId, false);
  }

  @Override
  public void showCitizen(@NotNull UUID playerUuid, int citizenId) {
    toggleCitizenVisibility(playerUuid, citizenId, true);
  }

  @Override
  public void showCitizenForAllPlayers(int citizenId) {
    toggleCitizenVisibilityForAllPlayers(citizenId, true);
  }

  @Override
  public boolean isCitizenVisibleForPlayer(@NotNull UUID playerUuid, int citizenId) {
    Optional<CitizenVisibility> citizenVisibility =
        citizenVisibilityDao.find(playerUuid, citizenId);
    // TODO: default visibility value
    return citizenVisibility.isEmpty() || citizenVisibility.get().isCitizenVisible();
  }

  private void toggleCitizenVisibility(
      @NotNull UUID playerUuid, int citizenId, boolean isCitizenVisible) {
    Preconditions.checkNotNull(playerUuid);

    Optional<CitizenVisibility> citizenVisibility =
        citizenVisibilityDao.find(playerUuid, citizenId);

    if (citizenVisibility.isEmpty()) {
      CitizenVisibility cv = new CitizenVisibility(playerUuid);
      cv.citizenId(citizenId);
      cv.isCitizenVisible(isCitizenVisible);
      citizenVisibilityDao.persist(cv);
    } else {
      CitizenVisibility cv = citizenVisibility.get();
      cv.isCitizenVisible(isCitizenVisible);
      citizenVisibilityDao.update(cv);
    }
  }

  private void toggleCitizenVisibilityForAllPlayers(int citizenId, boolean isCitizenVisible) {
    List<Player> players = playerService.fetchAll();

    List<CitizenVisibility> citizenVisibilities = new ArrayList<>(players.size());

    for (Player player : players) {
      UUID playerUuid = player.playerUuid();
      Optional<CitizenVisibility> fetchedCv = citizenVisibilityDao.find(playerUuid, citizenId);

      if (fetchedCv.isPresent()) {
        citizenVisibilities.add(fetchedCv.get());
        continue;
      }

      CitizenVisibility citizenVisibility = new CitizenVisibility(playerUuid);
      citizenVisibility.citizenId(citizenId);
      citizenVisibility.isCitizenVisible(isCitizenVisible);
      citizenVisibilityDao.persist(citizenVisibility);
    }

    for (CitizenVisibility citizenVisibility : citizenVisibilities) {
      citizenVisibility.isCitizenVisible(isCitizenVisible);
      citizenVisibilityDao.update(citizenVisibility);
    }
  }
}
