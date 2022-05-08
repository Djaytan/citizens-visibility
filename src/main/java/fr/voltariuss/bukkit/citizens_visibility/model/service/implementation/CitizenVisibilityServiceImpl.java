package fr.voltariuss.bukkit.citizens_visibility.model.service.implementation;

import com.google.common.base.Preconditions;
import fr.voltariuss.bukkit.citizens_visibility.model.dao.CitizenVisibilityDao;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.CitizenVisibility;
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
  private final PlayerService playerService;

  @Inject
  public CitizenVisibilityServiceImpl(
      @NotNull CitizenVisibilityDao citizenVisibilityDao, @NotNull PlayerService playerService) {
    this.citizenVisibilityDao = citizenVisibilityDao;
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
