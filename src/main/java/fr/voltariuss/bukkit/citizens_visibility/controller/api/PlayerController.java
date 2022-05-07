package fr.voltariuss.bukkit.citizens_visibility.controller.api;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface PlayerController {

  void registerPlayerOrUpdateName(@NotNull UUID playerUuid, @NotNull String playerName);
}
