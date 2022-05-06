package fr.voltariuss.bukkit.citizens_visibility.model.service.implementation;

import fr.voltariuss.bukkit.citizens_visibility.model.dao.PlayerDao;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.Player;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.PlayerService;
import java.util.List;
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
  public @NotNull  List<Player> findAll() {
    return playerDao.findAll();
  }
}
