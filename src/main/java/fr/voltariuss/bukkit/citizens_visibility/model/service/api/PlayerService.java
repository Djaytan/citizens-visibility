package fr.voltariuss.bukkit.citizens_visibility.model.service.api;

import fr.voltariuss.bukkit.citizens_visibility.model.entity.Player;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface PlayerService {

  @NotNull
  List<Player> findAll();
}
