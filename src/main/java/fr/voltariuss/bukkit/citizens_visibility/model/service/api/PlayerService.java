package fr.voltariuss.bukkit.citizens_visibility.model.service.api;

import fr.voltariuss.bukkit.citizens_visibility.model.entity.Player;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.parameter.PlayerRegisterResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

public interface PlayerService {

  @NotNull
  CompletableFuture<PlayerRegisterResponse> registerOrUpdateName(
      @NotNull UUID playerUuid, @NotNull String playerName);

  @NotNull
  CompletableFuture<Optional<Player>> fetchFromId(@NotNull UUID playerUuid);

  @NotNull
  CompletableFuture<Optional<Player>> fetchFromName(@NotNull String playerName);

  @NotNull
  CompletableFuture<List<Player>> fetchAll();
}
