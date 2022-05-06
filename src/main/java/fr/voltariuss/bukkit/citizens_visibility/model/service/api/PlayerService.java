package fr.voltariuss.bukkit.citizens_visibility.model.service.api;

import fr.voltariuss.bukkit.citizens_visibility.model.service.api.response.PlayerFetchResponse;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.response.PlayerResponse;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface PlayerService {

  // TODO: experiment CompletableFutur here
  PlayerResponse registerIfNotExists(@NotNull UUID playerUuid);

  @NotNull
  PlayerFetchResponse findAll();
}
