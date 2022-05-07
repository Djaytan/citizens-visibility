package fr.voltariuss.bukkit.citizens_visibility.model.service.implementation;

import com.google.common.base.Preconditions;
import fr.voltariuss.bukkit.citizens_visibility.model.dao.PlayerDao;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.Player;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.PlayerService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
  public void registerIfNotExists(@NotNull UUID playerUuid) {
    Preconditions.checkNotNull(playerUuid);

    Optional<Player> player = playerDao.findById(playerUuid);

    if (player.isEmpty()) {
      Player p = new Player(playerUuid);
      playerDao.persist(p);
    }
  }

  @Override
  public @NotNull List<Player> fetchAll() {
    return playerDao.findAll();
  }
}
