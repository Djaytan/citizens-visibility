package fr.voltariuss.bukkit.citizens_visibility.model.service.implementation;

import fr.voltariuss.bukkit.citizens_visibility.RemakeBukkitLogger;
import fr.voltariuss.bukkit.citizens_visibility.model.dao.PlayerDao;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.Player;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.PlayerService;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.response.PlayerFetchResponse;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.response.PlayerResponse;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.response.PlayerResponse.ResponseType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.PersistenceException;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PlayerServiceImpl implements PlayerService {

  private final RemakeBukkitLogger logger;
  private final PlayerDao playerDao;

  @Inject
  public PlayerServiceImpl(@NotNull RemakeBukkitLogger logger, @NotNull PlayerDao playerDao) {
    this.logger = logger;
    this.playerDao = playerDao;
  }

  @Override
  public PlayerResponse registerIfNotExists(@NotNull UUID playerUuid) {
    try {
      Optional<Player> player = playerDao.findById(playerUuid);

      if (player.isEmpty()) {
        Player p = new Player(playerUuid);
        playerDao.persist(p);
      }
    } catch (PersistenceException e) {
      logger.error(
          String.format(
              "Something went wrong when attempting to fetch and eventually register player with"
                  + " UUID '%s'.",
              playerUuid),
          e);
      return PlayerResponse.builder()
          .responseType(ResponseType.SERVER_ERROR)
          .failureMessage(
              String.format(
                  "Failed to fetch and eventually register player with UUID '%s'", playerUuid))
          .build();
    }

    return PlayerResponse.builder().responseType(ResponseType.SUCCESS).build();
  }

  @Override
  public @NotNull PlayerFetchResponse findAll() {
    try {
      List<Player> players = playerDao.findAll();

      return PlayerFetchResponse.builder()
          .responseType(ResponseType.SUCCESS)
          .players(players)
          .build();
    } catch (PersistenceException e) {
      logger.error("Something went wrong when attempting to recover players.", e);
      return PlayerFetchResponse.builder()
          .responseType(ResponseType.SERVER_ERROR)
          .failureMessage("Failed to recover players")
          .build();
    }
  }
}
