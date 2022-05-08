package fr.voltariuss.bukkit.citizens_visibility.model.service.api;

import fr.voltariuss.bukkit.citizens_visibility.model.entity.Player;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.parameter.PlayerRegisterResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface PlayerService {

  // TODO: experiment CompletableFutur here
  @NotNull
  PlayerRegisterResponse registerOrUpdateName(@NotNull UUID playerUuid, @NotNull String playerName);

  Optional<Player> fetchFromId(@NotNull UUID playerUuid);

  Optional<Player> fetchFromName(@NotNull String playerName);

  @NotNull
  List<Player> fetchAll();
}
