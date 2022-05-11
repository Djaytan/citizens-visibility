package fr.voltariuss.bukkit.citizens_visibility.model.service.implementation;

import com.google.common.base.Preconditions;
import fr.voltariuss.bukkit.citizens_visibility.model.dao.PlayerDao;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.Player;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.PlayerService;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.parameter.PlayerRegisterResponse;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.parameter.PlayerRegisterResponse.ResponseType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PlayerServiceImpl implements PlayerService {

  private final PlayerDao playerDao;

  @Inject
  public PlayerServiceImpl(@NotNull PlayerDao playerDao) {
    this.playerDao = playerDao;
  }

  @Override
  public @NotNull CompletableFuture<PlayerRegisterResponse> registerOrUpdateName(
      @NotNull UUID playerUuid, @NotNull String playerName) {
    Preconditions.checkNotNull(playerUuid);
    Preconditions.checkNotNull(playerName);
    Preconditions.checkArgument(!playerName.isBlank());

    return CompletableFuture.supplyAsync(
        () -> {
          Optional<Player> player = playerDao.findById(playerUuid).join();

          if (player.isEmpty()) {
            Player p = new Player(playerUuid, playerName);
            playerDao.persist(p).join();
            return PlayerRegisterResponse.builder()
                .responseType(ResponseType.PLAYER_REGISTERED)
                .build();
          }

          String fetchedPlayerName = player.get().playerName();

          // According to CraftBukkit sources, lower/upper cases aren't taken into account for
          // player names. See UserCache#get(String) method
          if (playerName.equals(fetchedPlayerName)) {
            return PlayerRegisterResponse.builder().responseType(ResponseType.NOTHING).build();
          }

          // Remove outdated binding UUID -> name (name must be unique)
          Optional<Player> playerWithName = playerDao.findByName(playerName).join();
          playerWithName.ifPresent(
              p -> {
                p.playerName(null);
                playerDao.update(p).join();
              });

          Player p = player.get();
          p.playerName(playerName);
          playerDao.update(p).join();

          return PlayerRegisterResponse.builder()
              .responseType(ResponseType.PLAYER_NAME_UPDATED)
              .oldPlayerName(fetchedPlayerName)
              .newPlayerName(playerName)
              .build();
        });
  }

  @Override
  public @NotNull CompletableFuture<Optional<Player>> fetchFromId(@NotNull UUID playerUuid) {
    return playerDao.findById(playerUuid);
  }

  @Override
  public @NotNull CompletableFuture<Optional<Player>> fetchFromName(@NotNull String playerName) {
    return playerDao.findByName(playerName);
  }

  @Override
  public @NotNull CompletableFuture<List<Player>> fetchAll() {
    return playerDao.findAll();
  }
}
