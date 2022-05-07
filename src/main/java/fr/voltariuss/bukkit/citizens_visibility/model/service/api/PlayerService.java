package fr.voltariuss.bukkit.citizens_visibility.model.service.api;

import fr.voltariuss.bukkit.citizens_visibility.model.entity.Player;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface PlayerService {

  // TODO: experiment CompletableFutur here
  void registerIfNotExists(@NotNull UUID playerUuid);

  @NotNull
  List<Player> fetchAll();
}
